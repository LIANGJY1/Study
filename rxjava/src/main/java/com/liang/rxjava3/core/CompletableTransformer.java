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

package com.liang.rxjava3.core;

import com.liang.rxjava3.annotations.NonNull;

/**
 * Convenience interface and callback used by the compose operator to turn a {@link Completable} into another
 * {@code Completable} fluently.
 */
@FunctionalInterface
public interface CompletableTransformer {
    /**
     * Applies a function to the upstream {@link Completable} and returns a {@link CompletableSource}.
     * @param upstream the upstream {@code Completable} instance
     * @return the transformed {@code CompletableSource} instance
     */
    @NonNull
    CompletableSource apply(@NonNull Completable upstream);
}
