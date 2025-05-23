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

package com.liang.rxjava3.exceptions;

/**
 * Indicates an overflow happened because the upstream disregarded backpressure completely or
 * {@link org.reactivestreams.Subscriber#onNext(Object)} was called concurrently from multiple threads
 * without synchronization. Rarely, it is an indication of bugs inside an operator.
 * @since 3.1.6
 */
public final class QueueOverflowException extends RuntimeException {

    private static final long serialVersionUID = 8517344746016032542L;

    /**
     * The message for queue overflows.
     * <p>
     * This can happen if the upstream disregards backpressure completely or calls
     * {@link org.reactivestreams.Subscriber#onNext(Object)} concurrently from multiple threads
     * without synchronization. Rarely, it is an indication of bugs inside an operator.
     */
    private static final String DEFAULT_MESSAGE = "Queue overflow due to illegal concurrent onNext calls or a bug in an operator";

    /**
     * Constructs a QueueOverflowException with the default message.
     */
    public QueueOverflowException() {
        this(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a QueueOverflowException with the given message but no cause.
     * @param message the error message
     */
    public QueueOverflowException(String message) {
        super(message);
    }
}
