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

import static org.junit.Assert.*;

import org.junit.Test;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.exceptions.TestException;
import com.liang.rxjava3.operators.QueueFuseable;
import com.liang.rxjava3.subjects.SingleSubject;
import com.liang.rxjava3.subscribers.TestSubscriber;
import com.liang.rxjava3.testsupport.TestSubscriberEx;

public class FlowableFromSingleTest extends RxJavaTest {

    @Test
    public void success() {
        Flowable.fromSingle(Single.just(1).hide())
        .test()
        .assertResult(1);
    }

    @Test
    public void error() {
        Flowable.fromSingle(Single.error(new TestException()).hide())
        .test()
        .assertFailure(TestException.class);
    }

    @Test
    public void cancelComposes() {
        SingleSubject<Integer> ms = SingleSubject.create();

        TestSubscriber<Integer> ts = Flowable.fromSingle(ms)
        .test();

        ts.assertEmpty();

        assertTrue(ms.hasObservers());

        ts.cancel();

        assertFalse(ms.hasObservers());
    }

    @Test
    public void asyncFusion() {
        TestSubscriberEx<Integer> ts = new TestSubscriberEx<>();
        ts.setInitialFusionMode(QueueFuseable.ASYNC);

        Flowable.fromSingle(Single.just(1))
        .subscribe(ts);

        ts
        .assertFuseable()
        .assertFusionMode(QueueFuseable.ASYNC)
        .assertResult(1);
    }

    @Test
    public void syncFusionRejected() {
        TestSubscriberEx<Integer> ts = new TestSubscriberEx<>();
        ts.setInitialFusionMode(QueueFuseable.SYNC);

        Flowable.fromSingle(Single.just(1))
        .subscribe(ts);

        ts
        .assertFuseable()
        .assertFusionMode(QueueFuseable.NONE)
        .assertResult(1);
    }
}
