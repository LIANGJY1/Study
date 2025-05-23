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

import java.util.concurrent.Callable;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.exceptions.Exceptions;
import com.liang.rxjava3.functions.Supplier;
import com.liang.rxjava3.internal.observers.DeferredScalarDisposable;
import com.liang.rxjava3.internal.util.ExceptionHelper;
import com.liang.rxjava3.plugins.RxJavaPlugins;

/**
 * Calls a Callable and emits its resulting single value or signals its exception.
 * @param <T> the value type
 */
public final class ObservableFromCallable<T> extends Observable<T> implements Supplier<T> {

    final Callable<? extends T> callable;

    public ObservableFromCallable(Callable<? extends T> callable) {
        this.callable = callable;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        DeferredScalarDisposable<T> d = new DeferredScalarDisposable<>(observer);
        observer.onSubscribe(d);
        if (d.isDisposed()) {
            return;
        }
        T value;
        try {
            value = ExceptionHelper.nullCheck(callable.call(), "Callable returned a null value.");
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            if (!d.isDisposed()) {
                observer.onError(e);
            } else {
                RxJavaPlugins.onError(e);
            }
            return;
        }
        d.complete(value);
    }

    @Override
    public T get() throws Throwable {
        return ExceptionHelper.nullCheck(callable.call(), "The Callable returned a null value.");
    }
}
