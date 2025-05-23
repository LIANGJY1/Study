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

package com.liang.rxjava3.internal.operators.flowable;

import org.junit.Test;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.functions.BiFunction;
import com.liang.rxjava3.internal.functions.Functions;
import com.liang.rxjava3.testsupport.TestHelper;

public class FlowableReduceWithSingleTest extends RxJavaTest {

    @Test
    public void normal() {
        Flowable.range(1, 5)
        .reduceWith(Functions.justSupplier(1), new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) throws Exception {
                return a + b;
            }
        })
        .test()
        .assertResult(16);
    }

    @Test
    public void disposed() {
        TestHelper.checkDisposed(Flowable.range(1, 5)
        .reduceWith(Functions.justSupplier(1), new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) throws Exception {
                return a + b;
            }
        }));
    }
}
