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

package com.liang.rxjava3.internal.schedulers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.liang.rxjava3.core.RxJavaTest;
import com.liang.rxjava3.internal.functions.Functions;

public class RxThreadFactoryTest extends RxJavaTest {

    @Test
    public void normal() {
        RxThreadFactory tf = new RxThreadFactory("Test", 1);

        assertEquals("RxThreadFactory[Test]", tf.toString());

        Thread t = tf.newThread(Functions.EMPTY_RUNNABLE);

        assertTrue(t.isDaemon());
        assertEquals(1, t.getPriority());
    }
}
