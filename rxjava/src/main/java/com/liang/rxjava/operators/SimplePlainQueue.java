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

package com.liang.rxjava.operators;

import io.reactivex.rxjava3.annotations.*;

/**
 * Override of the {@link SimpleQueue} interface with no {@code throws Throwable} on {@code poll()}.
 *
 * @param <T> the value type to offer and poll, not null
 * @since 3.1.1
 */
public interface SimplePlainQueue<@NonNull T> extends SimpleQueue<T> {

    @Nullable
    @Override
    T poll();
}
