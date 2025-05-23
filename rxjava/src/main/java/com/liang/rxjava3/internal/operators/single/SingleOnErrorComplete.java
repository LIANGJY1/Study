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

package com.liang.rxjava3.internal.operators.single;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.functions.Predicate;
import com.liang.rxjava3.internal.operators.maybe.MaybeOnErrorComplete;

/**
 * Emits an onComplete if the source emits an onError and the predicate returns true for
 * that Throwable.
 * 
 * @param <T> the value type
 * @since 3.0.0
 */
public final class SingleOnErrorComplete<T> extends Maybe<T> {

    final Single<T> source;

    final Predicate<? super Throwable> predicate;

    public SingleOnErrorComplete(Single<T> source,
            Predicate<? super Throwable> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    protected void subscribeActual(MaybeObserver<? super T> observer) {
        source.subscribe(new MaybeOnErrorComplete.OnErrorCompleteMultiObserver<T>(observer, predicate));
    }
}
