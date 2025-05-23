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

import com.liang.rxjava3.core.Flowable;
import com.liang.rxjava3.exceptions.Exceptions;
import com.liang.rxjava3.functions.Consumer;
import org.reactivestreams.Subscriber;

public final class FlowableOnBackpressureLatest<T> extends AbstractFlowableWithUpstream<T, T> {

    final Consumer<? super T> onDropped;

    public FlowableOnBackpressureLatest(Flowable<T> source, Consumer<? super T> onDropped) {
        super(source);
        this.onDropped = onDropped;
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        source.subscribe(new BackpressureLatestSubscriber<>(s, onDropped));
    }

    static final class BackpressureLatestSubscriber<T> extends AbstractBackpressureThrottlingSubscriber<T, T> {

        private static final long serialVersionUID = 163080509307634843L;

        final Consumer<? super T> onDropped;

        BackpressureLatestSubscriber(Subscriber<? super T> downstream,
                                     Consumer<? super T> onDropped) {
            super(downstream);
            this.onDropped = onDropped;
        }

        @Override
        public void onNext(T t) {
            T oldValue = current.getAndSet(t);
            if (onDropped != null && oldValue != null) {
                try {
                    onDropped.accept(oldValue);
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.cancel();
                    downstream.onError(ex);
                }
            }
            drain();
        }
    }
}
