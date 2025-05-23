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

package com.liang.rxjava3.functions;

import com.liang.rxjava3.annotations.NonNull;

/**
 * A functional interface (callback) that returns true or false for the given input value.
 * @param <T> the first value
 */
@FunctionalInterface
public interface Predicate<@NonNull T> {
    /**
     * Test the given input value and return a boolean.
     * @param t the value
     * @return the boolean result
     * @throws Throwable if the implementation wishes to throw any type of exception
     */
    boolean test(T t) throws Throwable;
}
