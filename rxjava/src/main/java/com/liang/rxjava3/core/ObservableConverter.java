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
 * Convenience interface and callback used by the {@link Observable#to} operator to turn an {@link Observable} into another
 * value fluently.
 * <p>History: 2.1.7 - experimental
 * @param <T> the upstream type
 * @param <R> the output type
 * @since 2.2
 */
@FunctionalInterface
public interface ObservableConverter<@NonNull T, @NonNull R> {
    /**
     * Applies a function to the upstream {@link Observable} and returns a converted value of type {@code R}.
     *
     * @param upstream the upstream {@code Observable} instance
     * @return the converted value
     */
    R apply(@NonNull Observable<T> upstream);
}
