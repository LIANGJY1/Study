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

import java.util.concurrent.atomic.AtomicInteger;

import org.reactivestreams.*;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.internal.subscriptions.SubscriptionArbiter;

public final class FlowableRepeat<T> extends AbstractFlowableWithUpstream<T, T> {
    final long count;
    public FlowableRepeat(Flowable<T> source, long count) {
        super(source);
        this.count = count;
    }

    @Override
    public void subscribeActual(Subscriber<? super T> s) {
        SubscriptionArbiter sa = new SubscriptionArbiter(false);
        s.onSubscribe(sa);

        RepeatSubscriber<T> rs = new RepeatSubscriber<>(s, count != Long.MAX_VALUE ? count - 1 : Long.MAX_VALUE, sa, source);
        rs.subscribeNext();
    }

    static final class RepeatSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T> {

        private static final long serialVersionUID = -7098360935104053232L;

        final Subscriber<? super T> downstream;
        final SubscriptionArbiter sa;
        final Publisher<? extends T> source;
        long remaining;

        long produced;

        RepeatSubscriber(Subscriber<? super T> actual, long count, SubscriptionArbiter sa, Publisher<? extends T> source) {
            this.downstream = actual;
            this.sa = sa;
            this.source = source;
            this.remaining = count;
        }

        @Override
        public void onSubscribe(Subscription s) {
            sa.setSubscription(s);
        }

        @Override
        public void onNext(T t) {
            produced++;
            downstream.onNext(t);
        }

        @Override
        public void onError(Throwable t) {
            downstream.onError(t);
        }

        @Override
        public void onComplete() {
            long r = remaining;
            if (r != Long.MAX_VALUE) {
                remaining = r - 1;
            }
            if (r != 0L) {
                subscribeNext();
            } else {
                downstream.onComplete();
            }
        }

        /**
         * Subscribes to the source again via trampolining.
         */
        void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                for (;;) {
                    if (sa.isCancelled()) {
                        return;
                    }
                    long p = produced;
                    if (p != 0L) {
                        produced = 0L;
                        sa.produced(p);
                    }
                    source.subscribe(this);

                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
        }
    }
}
