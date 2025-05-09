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

package com.liang.rxjava3.internal.operators.maybe;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.disposables.Disposable;
import com.liang.rxjava3.exceptions.Exceptions;
import com.liang.rxjava3.functions.Predicate;
import com.liang.rxjava3.internal.disposables.DisposableHelper;

/**
 * Filters the upstream via a predicate, returning the success item or completing if
 * the predicate returns false.
 *
 * @param <T> the upstream value type
 */
public final class MaybeFilter<T> extends AbstractMaybeWithUpstream<T, T> {

    final Predicate<? super T> predicate;

    public MaybeFilter(MaybeSource<T> source, Predicate<? super T> predicate) {
        super(source);
        this.predicate = predicate;
    }

    @Override
    protected void subscribeActual(MaybeObserver<? super T> observer) {
        source.subscribe(new FilterMaybeObserver<>(observer, predicate));
    }

    static final class FilterMaybeObserver<T> implements MaybeObserver<T>, Disposable {

        final MaybeObserver<? super T> downstream;

        final Predicate<? super T> predicate;

        Disposable upstream;

        FilterMaybeObserver(MaybeObserver<? super T> actual, Predicate<? super T> predicate) {
            this.downstream = actual;
            this.predicate = predicate;
        }

        @Override
        public void dispose() {
            Disposable d = this.upstream;
            this.upstream = DisposableHelper.DISPOSED;
            d.dispose();
        }

        @Override
        public boolean isDisposed() {
            return upstream.isDisposed();
        }

        @Override
        public void onSubscribe(Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;

                downstream.onSubscribe(this);
            }
        }

        @Override
        public void onSuccess(T value) {
            boolean b;

            try {
                b = predicate.test(value);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                downstream.onError(ex);
                return;
            }

            if (b) {
                downstream.onSuccess(value);
            } else {
                downstream.onComplete();
            }
        }

        @Override
        public void onError(Throwable e) {
            downstream.onError(e);
        }

        @Override
        public void onComplete() {
            downstream.onComplete();
        }
    }
}
