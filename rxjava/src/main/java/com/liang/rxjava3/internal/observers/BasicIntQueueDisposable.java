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

import java.util.concurrent.atomic.AtomicInteger;

import com.liang.rxjava3.operators.QueueDisposable;

/**
 * An abstract QueueDisposable implementation, extending an AtomicInteger,
 * that defaults all unnecessary Queue methods to throw UnsupportedOperationException.
 * @param <T> the output value type
 */
public abstract class BasicIntQueueDisposable<T>
extends AtomicInteger
implements QueueDisposable<T> {

    private static final long serialVersionUID = -1001730202384742097L;

    @Override
    public final boolean offer(T e) {
        throw new UnsupportedOperationException("Should not be called");
    }

    @Override
    public final boolean offer(T v1, T v2) {
        throw new UnsupportedOperationException("Should not be called");
    }
}
