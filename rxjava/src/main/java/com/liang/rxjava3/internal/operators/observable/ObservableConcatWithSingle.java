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

package com.liang.rxjava3.internal.operators.observable;

import java.util.concurrent.atomic.AtomicReference;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.disposables.Disposable;
import com.liang.rxjava3.internal.disposables.DisposableHelper;

/**
 * Subscribe to a main Observable first, then when it completes normally, subscribe to a Single,
 * signal its success value followed by a completion or signal its error as is.
 * <p>History: 2.1.10 - experimental
 * @param <T> the element type of the main source and output type
 * @since 2.2
 */
public final class ObservableConcatWithSingle<T> extends AbstractObservableWithUpstream<T, T> {

    final SingleSource<? extends T> other;

    public ObservableConcatWithSingle(Observable<T> source, SingleSource<? extends T> other) {
        super(source);
        this.other = other;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        source.subscribe(new ConcatWithObserver<>(observer, other));
    }

    static final class ConcatWithObserver<T>
    extends AtomicReference<Disposable>
    implements Observer<T>, SingleObserver<T>, Disposable {

        private static final long serialVersionUID = -1953724749712440952L;

        final Observer<? super T> downstream;

        SingleSource<? extends T> other;

        boolean inSingle;

        ConcatWithObserver(Observer<? super T> actual, SingleSource<? extends T> other) {
            this.downstream = actual;
            this.other = other;
        }

        @Override
        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d) && !inSingle) {
                downstream.onSubscribe(this);
            }
        }

        @Override
        public void onNext(T t) {
            downstream.onNext(t);
        }

        @Override
        public void onSuccess(T t) {
            downstream.onNext(t);
            downstream.onComplete();
        }

        @Override
        public void onError(Throwable e) {
            downstream.onError(e);
        }

        @Override
        public void onComplete() {
            inSingle = true;
            DisposableHelper.replace(this, null);
            SingleSource<? extends T> ss = other;
            other = null;
            ss.subscribe(this);
        }

        @Override
        public void dispose() {
            DisposableHelper.dispose(this);
        }

        @Override
        public boolean isDisposed() {
            return DisposableHelper.isDisposed(get());
        }
    }
}
