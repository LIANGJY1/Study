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

package com.liang.rxjava3.internal.fuseable;

import org.reactivestreams.Publisher;

import com.liang.rxjava3.annotations.NonNull;

/**
 * Interface indicating the implementor has an upstream Publisher-like source available
 * via {@link #source()} method.
 *
 * @param <T> the value type
 */
public interface HasUpstreamPublisher<@NonNull T> {
    /**
     * Returns the source Publisher.
     * <p>
     * This method is intended to discover the assembly
     * graph of sequences.
     * @return the source Publisher
     */
    @NonNull
    Publisher<T> source();
}
