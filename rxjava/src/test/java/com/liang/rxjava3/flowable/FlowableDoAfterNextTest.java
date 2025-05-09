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

package com.liang.rxjava3.flowable;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.liang.rxjava3.core.RxJavaTest;
import com.liang.rxjava3.functions.Consumer;

public class FlowableDoAfterNextTest extends RxJavaTest {

    @Test
    public void ifFunctionThrowsThatNoMoreEventsAreProcessed() {
        final AtomicInteger count = new AtomicInteger();
        final RuntimeException e = new RuntimeException();
        Burst.items(1, 2).create()
            .doAfterNext(new Consumer<Integer>() {
                @Override
                public void accept(Integer t) throws Exception {
                    count.incrementAndGet();
                    throw e;
                }})
            .test()
            .assertError(e)
            .assertValue(1);
        assertEquals(1, count.get());
    }
}
