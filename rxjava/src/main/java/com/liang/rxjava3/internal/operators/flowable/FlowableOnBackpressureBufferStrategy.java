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

import java.util.*;
import java.util.concurrent.atomic.*;

import org.reactivestreams.*;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.exceptions.*;
import com.liang.rxjava3.functions.*;
import com.liang.rxjava3.internal.subscriptions.SubscriptionHelper;
import com.liang.rxjava3.internal.util.BackpressureHelper;
import com.liang.rxjava3.plugins.RxJavaPlugins;

/**
 * Handle backpressure with a bounded buffer and custom strategy.
 *
 * @param <T> the input and output value type
 */
public final class FlowableOnBackpressureBufferStrategy<T> extends AbstractFlowableWithUpstream<T, T> {

    final long bufferSize;

    final Action onOverflow;

    final BackpressureOverflowStrategy strategy;

    final Consumer<? super T> onDropped;

    public FlowableOnBackpressureBufferStrategy(Flowable<T> source,
            long bufferSize, Action onOverflow, BackpressureOverflowStrategy strategy,
            Consumer<? super T> onDropped) {
        super(source);
        this.bufferSize = bufferSize;
        this.onOverflow = onOverflow;
        this.strategy = strategy;
        this.onDropped = onDropped;
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        source.subscribe(new OnBackpressureBufferStrategySubscriber<>(s, onOverflow, strategy, bufferSize, onDropped));
    }

    static final class OnBackpressureBufferStrategySubscriber<T>
    extends AtomicInteger
    implements FlowableSubscriber<T>, Subscription {

        private static final long serialVersionUID = 3240706908776709697L;

        final Subscriber<? super T> downstream;

        final Action onOverflow;

        final Consumer<? super T> onDropped;

        final BackpressureOverflowStrategy strategy;

        final long bufferSize;

        final AtomicLong requested;

        final Deque<T> deque;

        Subscription upstream;

        volatile boolean cancelled;

        volatile boolean done;
        Throwable error;

        OnBackpressureBufferStrategySubscriber(Subscriber<? super T> actual, Action onOverflow,
                BackpressureOverflowStrategy strategy, long bufferSize,
                Consumer<? super T> onDropped) {
            this.downstream = actual;
            this.onOverflow = onOverflow;
            this.strategy = strategy;
            this.bufferSize = bufferSize;
            this.requested = new AtomicLong();
            this.deque = new ArrayDeque<>();
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
            boolean callOnOverflow = false;
            boolean callError = false;
            boolean callDrain = false;
            Deque<T> dq = deque;
            T toDrop = null;
            synchronized (dq) {
               if (dq.size() == bufferSize) {
                   switch (strategy) {
                   case DROP_LATEST:
                       toDrop = dq.pollLast();
                       dq.offer(t);
                       callOnOverflow = true;
                       break;
                   case DROP_OLDEST:
                       toDrop = dq.poll();
                       dq.offer(t);
                       callOnOverflow = true;
                       break;
                   default:
                       // signal error
                       toDrop = t;
                       callError = true;
                       break;
                   }
               } else {
                   dq.offer(t);
                   callDrain = true;
               }
            }

            if (callOnOverflow && onOverflow != null) {
                try {
                    onOverflow.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.cancel();
                    onError(ex);
                }
            }

            if (onDropped != null && toDrop != null) {
                try {
                    onDropped.accept(toDrop);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.cancel();
                    onError(ex);
                }
            }

            if (callError) {
                upstream.cancel();
                onError(MissingBackpressureException.createDefault());
            }

            if (callDrain) {
                drain();
            }
        }

        @Override
        public void onError(Throwable t) {
            if (done) {
                RxJavaPlugins.onError(t);
                return;
            }
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
                drain();
            }
        }

        @Override
        public void cancel() {
            cancelled = true;
            upstream.cancel();

            if (getAndIncrement() == 0) {
                clear(deque);
            }
        }

        void clear(Deque<T> dq) {
            synchronized (dq) {
                dq.clear();
            }
        }

        void drain() {
            if (getAndIncrement() != 0) {
                return;
            }

            int missed = 1;
            Deque<T> dq = deque;
            Subscriber<? super T> a = downstream;
            for (;;) {
                long r = requested.get();
                long e = 0L;
                while (e != r) {
                    if (cancelled) {
                        clear(dq);
                        return;
                    }

                    boolean d = done;

                    T v;

                    synchronized (dq) {
                        v = dq.poll();
                    }

                    boolean empty = v == null;

                    if (d) {
                        Throwable ex = error;
                        if (ex != null) {
                            clear(dq);
                            a.onError(ex);
                            return;
                        }
                        if (empty) {
                            a.onComplete();
                            return;
                        }
                    }

                    if (empty) {
                        break;
                    }

                    a.onNext(v);

                    e++;
                }

                if (e == r) {
                    if (cancelled) {
                        clear(dq);
                        return;
                    }

                    boolean d = done;

                    boolean empty;

                    synchronized (dq) {
                        empty = dq.isEmpty();
                    }

                    if (d) {
                        Throwable ex = error;
                        if (ex != null) {
                            clear(dq);
                            a.onError(ex);
                            return;
                        }
                        if (empty) {
                            a.onComplete();
                            return;
                        }
                    }
                }

                if (e != 0L) {
                    BackpressureHelper.produced(requested, e);
                }

                missed = addAndGet(-missed);
                if (missed == 0) {
                    break;
                }
            }
        }
    }
}
