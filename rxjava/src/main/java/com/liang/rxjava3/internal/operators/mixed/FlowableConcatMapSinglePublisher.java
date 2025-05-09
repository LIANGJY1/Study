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

package com.liang.rxjava3.internal.operators.mixed;

import org.reactivestreams.*;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.functions.Function;
import com.liang.rxjava3.internal.operators.mixed.FlowableConcatMapSingle.ConcatMapSingleSubscriber;
import com.liang.rxjava3.internal.util.ErrorMode;

/**
 * Maps each upstream item into a {@link SingleSource}, subscribes to them one after the other terminates
 * and relays their success values, optionally delaying any errors till the main and inner sources
 * terminate.
 * <p>History: 2.1.11 - experimental
 * @param <T> the upstream element type
 * @param <R> the output element type
 * @since 2.2
 */
public final class FlowableConcatMapSinglePublisher<T, R> extends Flowable<R> {

    final Publisher<T> source;

    final Function<? super T, ? extends SingleSource<? extends R>> mapper;

    final ErrorMode errorMode;

    final int prefetch;

    public FlowableConcatMapSinglePublisher(Publisher<T> source,
            Function<? super T, ? extends SingleSource<? extends R>> mapper,
                    ErrorMode errorMode, int prefetch) {
        this.source = source;
        this.mapper = mapper;
        this.errorMode = errorMode;
        this.prefetch = prefetch;
    }

    @Override
    protected void subscribeActual(Subscriber<? super R> s) {
        source.subscribe(new ConcatMapSingleSubscriber<>(s, mapper, prefetch, errorMode));
    }
}
