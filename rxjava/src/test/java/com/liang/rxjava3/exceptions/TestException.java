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
 * Exception for testing if unchecked exceptions propagate as-is without confusing with
 * other type of common exceptions.
 */
public final class TestException extends RuntimeException {

    private static final long serialVersionUID = -1438148770465406172L;

    /**
     * Constructs a TestException without message or cause.
     */
    public TestException() {
        super();
    }

    /**
     * Counstructs a TestException with message and cause.
     * @param message the message
     * @param cause the cause
     */
    public TestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a TestException with a message only.
     * @param message the message
     */
    public TestException(String message) {
        super(message);
    }

    /**
     * Constructs a TestException with a cause only.
     * @param cause the cause
     */
    public TestException(Throwable cause) {
        super(cause);
    }
}
