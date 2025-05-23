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
import java.util.concurrent.atomic.AtomicLong;

import com.liang.rxjava3.exceptions.Exceptions;
import com.liang.rxjava3.functions.Consumer;
import org.reactivestreams.*;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.core.Scheduler.Worker;
import com.liang.rxjava3.disposables.Disposable;
import com.liang.rxjava3.exceptions.MissingBackpressureException;
import com.liang.rxjava3.internal.disposables.SequentialDisposable;
import com.liang.rxjava3.internal.subscriptions.SubscriptionHelper;
import com.liang.rxjava3.internal.util.BackpressureHelper;
import com.liang.rxjava3.plugins.RxJavaPlugins;
import com.liang.rxjava3.subscribers.SerializedSubscriber;

public final class FlowableThrottleFirstTimed<T> extends AbstractFlowableWithUpstream<T, T> {
    final long timeout;
    final TimeUnit unit;
    final Scheduler scheduler;
    final Consumer<? super T> onDropped;

    public FlowableThrottleFirstTimed(Flowable<T> source,
                                      long timeout,
                                      TimeUnit unit,
                                      Scheduler scheduler,
                                      Consumer<? super T> onDropped) {
        super(source);
        this.timeout = timeout;
        this.unit = unit;
        this.scheduler = scheduler;
        this.onDropped = onDropped;
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        source.subscribe(new DebounceTimedSubscriber<>(
                new SerializedSubscriber<>(s),
                timeout, unit, scheduler.createWorker(),
                onDropped));
    }

    static final class DebounceTimedSubscriber<T>
    extends AtomicLong
    implements FlowableSubscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = -9102637559663639004L;

        final Subscriber<? super T> downstream;
        final long timeout;
        final TimeUnit unit;
        final Scheduler.Worker worker;
        final Consumer<? super T> onDropped;
        Subscription upstream;
        final SequentialDisposable timer = new SequentialDisposable();
        volatile boolean gate;
        boolean done;

        DebounceTimedSubscriber(Subscriber<? super T> actual,
                                long timeout,
                                TimeUnit unit,
                                Worker worker,
                                Consumer<? super T> onDropped) {
            this.downstream = actual;
            this.timeout = timeout;
            this.unit = unit;
            this.worker = worker;
            this.onDropped = onDropped;
        }

        @Override
        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                downstream.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }

            if (!gate) {
                gate = true;
                long r = get();
                if (r != 0L) {
                    downstream.onNext(t);
                    BackpressureHelper.produced(this, 1);
                } else {
                    upstream.cancel();
                    done = true;
                    downstream.onError(MissingBackpressureException.createDefault());
                    worker.dispose();
                    return;
                }

                Disposable d = timer.get();
                if (d != null) {
                    d.dispose();
                }

                timer.replace(worker.schedule(this, timeout, unit));
            } else if (onDropped != null) {
                try {
                    onDropped.accept(t);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.cancel();
                    done = true;
                    downstream.onError(ex);
                    worker.dispose();
                }
            }
        }

        @Override
        public void run() {
            gate = false;
        }

        @Override
        public void onError(Throwable t) {
            if (done) {
                RxJavaPlugins.onError(t);
                return;
            }
            done = true;
            downstream.onError(t);
            worker.dispose();
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }
            done = true;
            downstream.onComplete();
            worker.dispose();
        }

        @Override
        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this, n);
            }
        }

        @Override
        public void cancel() {
            upstream.cancel();
            worker.dispose();
        }
    }
}
