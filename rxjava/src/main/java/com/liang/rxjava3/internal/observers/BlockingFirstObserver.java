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

package com.liang.rxjava3.internal.observers;

/**
 * Blocks until the upstream signals its first value or completes.
 *
 * @param <T> the value type
 */
public final class BlockingFirstObserver<T> extends BlockingBaseObserver<T> {

    @Override
    public void onNext(T t) {
        if (value == null) {
            value = t;
            upstream.dispose();
            countDown();
        }
    }

    @Override
    public void onError(Throwable t) {
        if (value == null) {
            error = t;
        }
        countDown();
    }
}
