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

package com.liang.rxjava3.tck;

import java.util.concurrent.*;

import org.reactivestreams.*;
import org.reactivestreams.tck.*;
import org.testng.annotations.Test;

import com.liang.rxjava3.exceptions.TestException;
import com.liang.rxjava3.processors.UnicastProcessor;

@Test
public class UnicastProcessorTckTest extends IdentityProcessorVerification<Integer> {

    public UnicastProcessorTckTest() {
        super(new TestEnvironment(50));
    }

    @Override
    public Processor<Integer, Integer> createIdentityProcessor(int bufferSize) {
        UnicastProcessor<Integer> up = UnicastProcessor.create();
        return new RefCountProcessor<>(up);
    }

    @Override
    public Publisher<Integer> createFailedPublisher() {
        UnicastProcessor<Integer> up = UnicastProcessor.create();
        up.onError(new TestException());
        return up;
    }

    @Override
    public ExecutorService publisherExecutorService() {
        return Executors.newCachedThreadPool();
    }

    @Override
    public Integer createElement(int element) {
        return element;
    }

    @Override
    public long maxSupportedSubscribers() {
        return 1;
    }

    @Override
    public long maxElementsFromPublisher() {
        return 1024;
    }
}
