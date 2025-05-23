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

import org.reactivestreams.*;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.functions.Function;
import com.liang.rxjava3.internal.operators.flowable.FlowableFlatMapSingle.FlatMapSingleSubscriber;

/**
 * Maps upstream values into SingleSources and merges their signals into one sequence.
 * @param <T> the source value type
 * @param <R> the result value type
 */
public final class FlowableFlatMapSinglePublisher<T, R> extends Flowable<R> {

    final Publisher<T> source;

    final Function<? super T, ? extends SingleSource<? extends R>> mapper;

    final boolean delayErrors;

    final int maxConcurrency;

    public FlowableFlatMapSinglePublisher(Publisher<T> source, Function<? super T, ? extends SingleSource<? extends R>> mapper,
            boolean delayError, int maxConcurrency) {
        this.source = source;
        this.mapper = mapper;
        this.delayErrors = delayError;
        this.maxConcurrency = maxConcurrency;
    }

    @Override
    protected void subscribeActual(Subscriber<? super R> s) {
        source.subscribe(new FlatMapSingleSubscriber<>(s, mapper, delayErrors, maxConcurrency));
    }
}
