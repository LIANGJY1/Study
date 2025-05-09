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

package com.liang.rxjava3.internal.disposables;

import static org.junit.Assert.*;

import org.junit.Test;

import com.liang.rxjava3.core.RxJavaTest;
import com.liang.rxjava3.operators.QueueFuseable;
import com.liang.rxjava3.testsupport.TestHelper;

public class EmptyDisposableTest extends RxJavaTest {

    @Test
    public void noOffer() {
        TestHelper.assertNoOffer(EmptyDisposable.INSTANCE);
    }

    @Test
    public void asyncFusion() {
        assertEquals(QueueFuseable.NONE, EmptyDisposable.INSTANCE.requestFusion(QueueFuseable.SYNC));
        assertEquals(QueueFuseable.ASYNC, EmptyDisposable.INSTANCE.requestFusion(QueueFuseable.ASYNC));
    }

    @Test
    public void checkEnum() {
        assertEquals(2, EmptyDisposable.values().length);
        assertNotNull(EmptyDisposable.valueOf("INSTANCE"));
        assertNotNull(EmptyDisposable.valueOf("NEVER"));
    }
}
