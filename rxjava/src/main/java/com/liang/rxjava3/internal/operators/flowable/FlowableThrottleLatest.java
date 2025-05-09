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

package com.liang.rxjava3.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;

import org.reactivestreams.*;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.exceptions.*;
import com.liang.rxjava3.functions.Consumer;
import com.liang.rxjava3.internal.subscriptions.SubscriptionHelper;
import com.liang.rxjava3.internal.util.BackpressureHelper;
import com.liang.rxjava3.plugins.RxJavaPlugins;

/**
 * Emits the next or latest item when the given time elapses.
 * <p>
 * The operator emits the next item, then starts a timer. When the timer fires,
 * it tries to emit the latest item from upstream. If there was no upstream item,
 * in the meantime, the next upstream item is emitted immediately and the
 * timed process repeats.
 * <p>History: 2.1.14 - experimental
 * @param <T> the upstream and downstream value type
 * @since 2.2
 */
public final class FlowableThrottleLatest<T> extends AbstractFlowableWithUpstream<T, T> {

    final long timeout;

    final TimeUnit unit;

    final Scheduler scheduler;

    final boolean emitLast;

    final Consumer<? super T> onDropped;

    public FlowableThrottleLatest(Flowable<T> source,
            long timeout, TimeUnit unit,
            Scheduler scheduler,
            boolean emitLast,
            Consumer<? super T> onDropped) {
        super(source);
        this.timeout = timeout;
        this.unit = unit;
        this.scheduler = scheduler;
        this.emitLast = emitLast;
        this.onDropped = onDropped;
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        source.subscribe(new ThrottleLatestSubscriber<>(s, timeout, unit, scheduler.createWorker(), emitLast, onDropped));
    }

    static final class ThrottleLatestSubscriber<T>
    extends AtomicInteger
    implements FlowableSubscriber<T>, Subscription, Runnable {

        private static final long serialVersionUID = -8296689127439125014L;

        final Subscriber<? super T> downstream;

        final long timeout;

        final TimeUnit unit;

        final Scheduler.Worker worker;

        final boolean emitLast;

        final AtomicReference<T> latest;

        final AtomicLong requested;

        final Consumer<? super T> onDropped;

        Subscription upstream;

        volatile boolean done;
        Throwable error;

        volatile boolean cancelled;

        volatile boolean timerFired;

        long emitted;

        boolean timerRunning;

        ThrottleLatestSubscriber(Subscriber<? super T> downstream,
                long timeout, TimeUnit unit,
                Scheduler.Worker worker,
                boolean emitLast,
                Consumer<? super T> onDropped) {
            this.downstream = downstream;
            this.timeout = timeout;
            this.unit = unit;
            this.worker = worker;
            this.emitLast = emitLast;
            this.latest = new AtomicReference<>();
            this.requested = new AtomicLong();
            this.onDropped = onDropped;
        }

        @Override
        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(upstream, s)) {
                upstream = s;
                downstream.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        @Override
        public void onNext(T t) {
            T old = latest.getAndSet(t);
            if (onDropped != null && old != null) {
                try {
                    onDropped.accept(old);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.cancel();
                    error = ex;
                    done = true;
                }
            }
            drain();
        }

        @Override
        public void onError(Throwable t) {
            error = t;
            done = true;
            drain();
        }

        @Override
        public void onComplete() {
            done = true;
            drain();
        }

        @Override
        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(requested, n);
            }
        }

        @Override
        public void cancel() {
            cancelled = true;
            upstream.cancel();
            worker.dispose();
            if (getAndIncrement() == 0) {
                clear();
            }
        }

        void clear() {
            if (onDropped != null) {
                T v = latest.getAndSet(null);
                if (v != null) {
                    try {
                        onDropped.accept(v);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        RxJavaPlugins.onError(ex);
                    }
                }
            } else {
                latest.lazySet(null);
            }
        }

        @Override
        public void run() {
            timerFired = true;
            drain();
        }

        void drain() {
            if (getAndIncrement() != 0) {
                return;
            }

            int missed = 1;

            AtomicReference<T> latest = this.latest;
            AtomicLong requested = this.requested;
            Subscriber<? super T> downstream = this.downstream;

            for (;;) {

                for (;;) {
                    if (cancelled) {
                        clear();
                        return;
                    }

                    boolean d = done;
                    Throwable error = this.error;

                    if (d && error != null) {
                        if (onDropped != null) {
                            T v = latest.getAndSet(null);
                            if (v != null) {
                                try {
                                    onDropped.accept(v);
                                } catch (Throwable ex) {
                                    Exceptions.throwIfFatal(ex);
                                    error = new CompositeException(error, ex);
                                }
                            }
                        } else {
                            latest.lazySet(null);
                        }
                        downstream.onError(error);
                        worker.dispose();
                        return;
                    }

                    T v = latest.get();
                    boolean empty = v == null;

                    if (d) {
                        if (!empty) {
                            v = latest.getAndSet(null);
                            if (emitLast) {
                                long e = emitted;
                                if (e != requested.get()) {
                                    emitted = e + 1;
                                    downstream.onNext(v);
                                    downstream.onComplete();
                                } else {
                                    tryDropAndSignalMBE(v);
                                }
                            } else {
                                if (onDropped != null) {
                                    try {
                                        onDropped.accept(v);
                                    } catch (Throwable ex) {
                                        Exceptions.throwIfFatal(ex);
                                        downstream.onError(ex);
                                        worker.dispose();
                                        return;
                                    }
                                }
                                downstream.onComplete();
                            }
                        } else {
                            downstream.onComplete();
                        }
                        worker.dispose();
                        return;
                    }

                    if (empty) {
                        if (timerFired) {
                            timerRunning = false;
                            timerFired = false;
                        }
                        break;
                    }

                    if (!timerRunning || timerFired) {
                        v = latest.getAndSet(null);
                        long e = emitted;
                        if (e != requested.get()) {
                            downstream.onNext(v);
                            emitted = e + 1;
                        } else {
                            upstream.cancel();
                            tryDropAndSignalMBE(v);
                            worker.dispose();
                            return;
                        }

                        timerFired = false;
                        timerRunning = true;
                        worker.schedule(this, timeout, unit);
                    } else {
                        break;
                    }
                }

                missed = addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }

        void tryDropAndSignalMBE(T valueToDrop) {
            Throwable errorToSignal = MissingBackpressureException.createDefault();
            if (onDropped != null) {
                try {
                    onDropped.accept(valueToDrop);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    errorToSignal = new CompositeException(errorToSignal, ex);
                }
            }
            downstream.onError(errorToSignal);
        }
    }
}
