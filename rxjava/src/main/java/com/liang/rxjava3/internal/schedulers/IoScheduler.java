/*
 * Copyright (c) 2016-present, RxJava Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package com.liang.rxjava3.internal.schedulers;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import com.liang.rxjava3.annotations.NonNull;
import com.liang.rxjava3.core.Scheduler;
import com.liang.rxjava3.disposables.*;
import com.liang.rxjava3.internal.disposables.EmptyDisposable;

/**
 * Scheduler that creates and caches a set of thread pools and reuses them if possible.
 */
// 动态管理线程池，适应I/O密集型任务的高吞吐需求。
public final class IoScheduler extends Scheduler {
    // 工作线程
    private static final String WORKER_THREAD_NAME_PREFIX = "RxCachedThreadScheduler";
    static final RxThreadFactory WORKER_THREAD_FACTORY;
    // 清理线程
    private static final String EVICTOR_THREAD_NAME_PREFIX = "RxCachedWorkerPoolEvictor";
    static final RxThreadFactory EVICTOR_THREAD_FACTORY;

    /**
     * The name of the system property for setting the keep-alive time (in seconds) for this Scheduler workers.
     */
    private static final String KEY_KEEP_ALIVE_TIME = "rx3.io-keep-alive-time";
    public static final long KEEP_ALIVE_TIME_DEFAULT = 60;// 线程空闲存活时间

    private static final long KEEP_ALIVE_TIME;
    private static final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;

    static final ThreadWorker SHUTDOWN_THREAD_WORKER;
    final ThreadFactory threadFactory;
    final AtomicReference<CachedWorkerPool> pool;

    /**
     * The name of the system property for setting the thread priority for this Scheduler.
     */
    // System.setProperty("rx3.io-priority", String.valueOf(6));
    private static final String KEY_IO_PRIORITY = "rx3.io-priority";

    /**
     * The name of the system property for setting the release behaviour for this Scheduler.
     */
    private static final String KEY_SCHEDULED_RELEASE = "rx3.io-scheduled-release";
    static boolean USE_SCHEDULED_RELEASE;// 控制线程释放策略，立即释放/延迟调度

    static final CachedWorkerPool NONE;

    static {
        KEEP_ALIVE_TIME = Long.getLong(KEY_KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_DEFAULT);// 线程空闲存活时间，默认 60s

        SHUTDOWN_THREAD_WORKER = new ThreadWorker(new RxThreadFactory("RxCachedThreadSchedulerShutdown"));// 预初始化，用于占位
        SHUTDOWN_THREAD_WORKER.dispose();

        // 默认为 Thread.NORM_PRIORITY
        // System.setProperty("rx3.io-priority", String.valueOf(6));
        int priority = Math.max(Thread.MIN_PRIORITY, Math.min(Thread.MAX_PRIORITY,
                Integer.getInteger(KEY_IO_PRIORITY, Thread.NORM_PRIORITY)));

        WORKER_THREAD_FACTORY = new RxThreadFactory(WORKER_THREAD_NAME_PREFIX, priority);
        System.out.println("IoScheduler priority: " + priority);

        EVICTOR_THREAD_FACTORY = new RxThreadFactory(EVICTOR_THREAD_NAME_PREFIX, priority);

        USE_SCHEDULED_RELEASE = Boolean.getBoolean(KEY_SCHEDULED_RELEASE);

        NONE = new CachedWorkerPool(0, null, WORKER_THREAD_FACTORY);// 初始化的空线程池，标记未激活状态
        NONE.shutdown();
    }

    static final class CachedWorkerPool implements Runnable {
        private final long keepAliveTime;
        private final ConcurrentLinkedQueue<ThreadWorker> expiringWorkerQueue;// 线程安全队列，按过期时间排序存储空闲线程。
        final CompositeDisposable allWorkers;// 组合式Disposable，跟踪所有活跃线程。
        private final ScheduledExecutorService evictorService;
        private final Future<?> evictorTask;
        private final ThreadFactory threadFactory;

        CachedWorkerPool(long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
            this.keepAliveTime = unit != null ? unit.toNanos(keepAliveTime) : 0L;// 0
            this.expiringWorkerQueue = new ConcurrentLinkedQueue<>();// 基于链接节点的无界线程安全队列
            this.allWorkers = new CompositeDisposable();
            this.threadFactory = threadFactory;// RxThreadFactory

            ScheduledExecutorService evictor = null;
            Future<?> task = null;
            if (unit != null) {
                evictor = Executors.newScheduledThreadPool(1, EVICTOR_THREAD_FACTORY);// 定时线程池，周期性执行 evictExpiredWorkers 清理过期线程。
                task = evictor.scheduleWithFixedDelay(this, this.keepAliveTime, this.keepAliveTime, TimeUnit.NANOSECONDS);
            }
            evictorService = evictor;
            evictorTask = task;
        }

        @Override
        public void run() {
            evictExpiredWorkers(expiringWorkerQueue, allWorkers);
        }

        ThreadWorker get() {
            if (allWorkers.isDisposed()) {// allWorkers CompositeDisposable, false
                return SHUTDOWN_THREAD_WORKER;
            }
            while (!expiringWorkerQueue.isEmpty()) {//  expiringWorkerQueue size = 0, false
                ThreadWorker threadWorker = expiringWorkerQueue.poll();
                if (threadWorker != null) {
                    return threadWorker;
                }
            }

            // No cached worker found, so create a new one.
            ThreadWorker w = new ThreadWorker(threadFactory);
            System.out.println("IoScheduler CachedWorkerPool ThreadWorker get: " + threadFactory.toString());
            allWorkers.add(w);
            return w;
        }

        void release(ThreadWorker threadWorker) {
            // Refresh expire time before putting worker back in pool
            threadWorker.setExpirationTime(now() + keepAliveTime);

            expiringWorkerQueue.offer(threadWorker);
        }

        static void evictExpiredWorkers(ConcurrentLinkedQueue<ThreadWorker> expiringWorkerQueue, CompositeDisposable allWorkers) {
            if (!expiringWorkerQueue.isEmpty()) {
                long currentTimestamp = now();

                for (ThreadWorker threadWorker : expiringWorkerQueue) {
                    if (threadWorker.getExpirationTime() <= currentTimestamp) {
                        if (expiringWorkerQueue.remove(threadWorker)) {
                            allWorkers.remove(threadWorker);
                        }
                    } else {
                        // Queue is ordered with the worker that will expire first in the beginning, so when we
                        // find a non-expired worker we can stop evicting.
                        break;
                    }
                }
            }
        }

        static long now() {
            return System.nanoTime();
        }

        void shutdown() {
            allWorkers.dispose();
            if (evictorTask != null) {
                evictorTask.cancel(true);
            }
            if (evictorService != null) {
                evictorService.shutdownNow();
            }
        }
    }

    public IoScheduler() {
        this(WORKER_THREAD_FACTORY);// 使用默认线程工厂
    }

    /**
     * Constructs an IoScheduler with the given thread factory and starts the pool of workers.
     *
     * @param threadFactory thread factory to use for creating worker threads. Note that this takes precedence over any
     *                      system properties for configuring new thread creation. Cannot be null.
     */
    public IoScheduler(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;// RxThreadFactory
        this.pool = new AtomicReference<>(NONE);// CachedWorkerPool
        start();
    }

    @Override
    public void start() {
        CachedWorkerPool update = new CachedWorkerPool(KEEP_ALIVE_TIME, KEEP_ALIVE_UNIT, threadFactory);// 60s RxThreadFactory
        if (!pool.compareAndSet(NONE, update)) {// pool 为 NONE，则替换为 update
            update.shutdown();// 如果没有替换为 update，执行 shutdown
        }
    }

    @Override
    public void shutdown() {
        CachedWorkerPool curr = pool.getAndSet(NONE);
        if (curr != NONE) {
            curr.shutdown();
        }
    }

    @NonNull
    @Override
    public Worker createWorker() {
        System.out.println("IoScheduler createWorker pool.get(): " + pool.get());// CachedWorkerPool
        return new EventLoopWorker(pool.get());
    }

    public int size() {
        return pool.get().allWorkers.size();
    }

    static final class EventLoopWorker extends Scheduler.Worker implements Runnable {
        private final CompositeDisposable tasks;
        private final CachedWorkerPool pool;
        private final ThreadWorker threadWorker;

        final AtomicBoolean once = new AtomicBoolean();

        EventLoopWorker(CachedWorkerPool pool) {
            this.pool = pool;
            this.tasks = new CompositeDisposable();
            this.threadWorker = pool.get();
            System.out.println("IoScheduler EventLoopWorker threadWorker: " + this.threadWorker.toString());
        }

        @Override
        public void dispose() {
            if (once.compareAndSet(false, true)) {
                tasks.dispose();

                if (USE_SCHEDULED_RELEASE) {
                    threadWorker.scheduleActual(this, 0, TimeUnit.NANOSECONDS, null);
                } else {
                    // releasing the pool should be the last action
                    pool.release(threadWorker);
                }
            }
        }

        @Override
        public void run() {
            pool.release(threadWorker);
        }

        @Override
        public boolean isDisposed() {
            return once.get();
        }

        @NonNull
        @Override
        public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
            if (tasks.isDisposed()) {// CompositeDisposable false
                // don't schedule, we are unsubscribed
                return EmptyDisposable.INSTANCE;
            }

            return threadWorker.scheduleActual(action, delayTime, unit, tasks);// action 1 threadWorker:IoScheduler ThreadWorker
        }
    }

    static final class ThreadWorker extends NewThreadWorker {

        long expirationTime;

        ThreadWorker(ThreadFactory threadFactory) {
            super(threadFactory);
            this.expirationTime = 0L;
        }

        public long getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }
    }
}
