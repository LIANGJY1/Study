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
import java.util.concurrent.atomic.AtomicReferenceArray;

import com.liang.rxjava3.disposables.*;
import com.liang.rxjava3.plugins.RxJavaPlugins;

public final class ScheduledRunnable extends AtomicReferenceArray<Object>
implements Runnable, Callable<Object>, Disposable {

    private static final long serialVersionUID = -6120223772001106981L;
    final Runnable actual;
    final boolean interruptOnCancel;

    /** Indicates that the parent tracking this task has been notified about its completion. */
    static final Object PARENT_DISPOSED = new Object();
    /** Indicates the dispose() was called from within the run/call method. */
    static final Object SYNC_DISPOSED = new Object();
    /** Indicates the dispose() was called from another thread. */
    static final Object ASYNC_DISPOSED = new Object();

    static final Object DONE = new Object();

    static final int PARENT_INDEX = 0;
    static final int FUTURE_INDEX = 1;
    static final int THREAD_INDEX = 2;

    /**
     * Creates a ScheduledRunnable by wrapping the given action and setting
     * up the optional parent.
     * The underlying future will be interrupted if the task is disposed asynchronously.
     * @param actual the runnable to wrap, not-null (not verified)
     * @param parent the parent tracking container or null if none
     */
    public ScheduledRunnable(Runnable actual, DisposableContainer parent) {
        this(actual, parent, true);
    }

    /**
     * Creates a ScheduledRunnable by wrapping the given action and setting
     * up the optional parent.
     * @param actual the runnable to wrap, not-null (not verified)
     * @param parent the parent tracking container or null if none
     * @param interruptOnCancel if true, the underlying future will be interrupted when disposing
     *                          this task from a different thread than it is running on.
     */
    public ScheduledRunnable(Runnable actual, DisposableContainer parent, boolean interruptOnCancel) {
        super(3);
        this.actual = actual;
        this.interruptOnCancel = interruptOnCancel;
        this.lazySet(0, parent);
    }

    @Override
    public Object call() {
        // Being Callable saves an allocation in ThreadPoolExecutor
        run();
        return null;
    }

    @Override
    public void run() {
        lazySet(THREAD_INDEX, Thread.currentThread());
        try {
            try {
                actual.run();
            } catch (Throwable e) {
                // Exceptions.throwIfFatal(e); nowhere to go
                RxJavaPlugins.onError(e);
                throw e;
            }
        } finally {
            Object o = get(PARENT_INDEX);
            if (o != PARENT_DISPOSED && compareAndSet(PARENT_INDEX, o, DONE) && o != null) {
                ((DisposableContainer)o).delete(this);
            }

            for (;;) {
                o = get(FUTURE_INDEX);
                if (o == SYNC_DISPOSED || o == ASYNC_DISPOSED || compareAndSet(FUTURE_INDEX, o, DONE)) {
                    break;
                }
            }
            lazySet(THREAD_INDEX, null);
        }
    }

    public void setFuture(Future<?> f) {// f java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask@f7be3c5[Completed normally]
        for (;;) {
            Object o = get(FUTURE_INDEX);// o null
            if (o == DONE) { // 第二轮走这
                return;
            }
            if (o == SYNC_DISPOSED) {
                f.cancel(false);
                return;
            }
            if (o == ASYNC_DISPOSED) {
                f.cancel(interruptOnCancel);
                return;
            }
            if (compareAndSet(FUTURE_INDEX, o, f)) {
                return;
            }
        }
    }

    @Override
    public void dispose() {
        for (;;) {
            Object o = get(FUTURE_INDEX);
            if (o == DONE || o == SYNC_DISPOSED || o == ASYNC_DISPOSED) {
                break;
            }
            boolean async = get(THREAD_INDEX) != Thread.currentThread();
            if (compareAndSet(FUTURE_INDEX, o, async ? ASYNC_DISPOSED : SYNC_DISPOSED)) {
                if (o != null) {
                    ((Future<?>)o).cancel(async && interruptOnCancel);
                }
                break;
            }
        }

        for (;;) {
            Object o = get(PARENT_INDEX);
            if (o == DONE || o == PARENT_DISPOSED || o == null) {
                return;
            }
            if (compareAndSet(PARENT_INDEX, o, PARENT_DISPOSED)) {
                ((DisposableContainer)o).delete(this);
                return;
            }
        }
    }

    @Override
    public boolean isDisposed() {
        Object o = get(PARENT_INDEX);
        return o == PARENT_DISPOSED || o == DONE;
    }

    @Override
    public String toString() {
        String state;
        Object o = get(FUTURE_INDEX);
        if (o == DONE) {
            state = "Finished";
        } else if (o == SYNC_DISPOSED) {
            state = "Disposed(Sync)";
        } else if (o == ASYNC_DISPOSED) {
            state = "Disposed(Async)";
        } else {
            o = get(THREAD_INDEX);
            if (o == null) {
                state = "Waiting";
            } else {
                state = "Running on " + o;
            }
        }

        return getClass().getSimpleName() + "[" + state + "]";
    }
}
