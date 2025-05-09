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

import org.reactivestreams.Publisher;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.exceptions.Exceptions;
import com.liang.rxjava3.functions.*;
import com.liang.rxjava3.internal.disposables.EmptyDisposable;
import com.liang.rxjava3.internal.operators.flowable.FlowableReduceSeedSingle.ReduceSeedObserver;

import java.util.Objects;

/**
 * Reduce a sequence of values, starting from a generated seed value and by using
 * an accumulator function and return the last accumulated value.
 *
 * @param <T> the source value type
 * @param <R> the accumulated result type
 */
public final class FlowableReduceWithSingle<T, R> extends Single<R> {

    final Publisher<T> source;

    final Supplier<R> seedSupplier;

    final BiFunction<R, ? super T, R> reducer;

    public FlowableReduceWithSingle(Publisher<T> source, Supplier<R> seedSupplier, BiFunction<R, ? super T, R> reducer) {
        this.source = source;
        this.seedSupplier = seedSupplier;
        this.reducer = reducer;
    }

    @Override
    protected void subscribeActual(SingleObserver<? super R> observer) {
        R seed;

        try {
            seed = Objects.requireNonNull(seedSupplier.get(), "The seedSupplier returned a null value");
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return;
        }
        source.subscribe(new ReduceSeedObserver<>(observer, reducer, seed));
    }
}
