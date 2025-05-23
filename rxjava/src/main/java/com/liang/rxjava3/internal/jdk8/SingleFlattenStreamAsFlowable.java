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

package com.liang.rxjava3.internal.jdk8;

import java.util.stream.Stream;

import org.reactivestreams.Subscriber;

import com.liang.rxjava3.annotations.NonNull;
import com.liang.rxjava3.core.*;
import com.liang.rxjava3.functions.Function;
import com.liang.rxjava3.internal.jdk8.MaybeFlattenStreamAsFlowable.FlattenStreamMultiObserver;

/**
 * Map the success value into a Java {@link Stream} and emits its values.
 *
 * @param <T> the source value type
 * @param <R> the output value type
 * @since 3.0.0
 */
public final class SingleFlattenStreamAsFlowable<T, R> extends Flowable<R> {

    final Single<T> source;

    final Function<? super T, ? extends Stream<? extends R>> mapper;

    public SingleFlattenStreamAsFlowable(Single<T> source, Function<? super T, ? extends Stream<? extends R>> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    protected void subscribeActual(@NonNull Subscriber<? super R> s) {
        source.subscribe(new FlattenStreamMultiObserver<>(s, mapper));
    }
}
