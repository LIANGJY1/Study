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

import static org.mockito.Mockito.inOrder;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.InOrder;
import org.reactivestreams.Subscriber;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.processors.PublishProcessor;
import com.liang.rxjava3.schedulers.TestScheduler;
import com.liang.rxjava3.testsupport.TestHelper;

public class FlowableThrottleWithTimeoutTests extends RxJavaTest {

    @Test
    public void throttle() {
        Subscriber<Integer> subscriber = TestHelper.mockSubscriber();

        TestScheduler s = new TestScheduler();
        PublishProcessor<Integer> o = PublishProcessor.create();
        o.throttleWithTimeout(500, TimeUnit.MILLISECONDS, s)
        .subscribe(subscriber);

        // send events with simulated time increments
        s.advanceTimeTo(0, TimeUnit.MILLISECONDS);
        o.onNext(1); // skip
        o.onNext(2); // deliver
        s.advanceTimeTo(501, TimeUnit.MILLISECONDS);
        o.onNext(3); // skip
        s.advanceTimeTo(600, TimeUnit.MILLISECONDS);
        o.onNext(4); // skip
        s.advanceTimeTo(700, TimeUnit.MILLISECONDS);
        o.onNext(5); // skip
        o.onNext(6); // deliver at 1300 after 500ms has passed since onNext(5)
        s.advanceTimeTo(1300, TimeUnit.MILLISECONDS);
        o.onNext(7); // deliver
        s.advanceTimeTo(1800, TimeUnit.MILLISECONDS);
        o.onComplete();

        InOrder inOrder = inOrder(subscriber);
        inOrder.verify(subscriber).onNext(2);
        inOrder.verify(subscriber).onNext(6);
        inOrder.verify(subscriber).onNext(7);
        inOrder.verify(subscriber).onComplete();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void throttleFirstDefaultScheduler() {
        Flowable.just(1).throttleWithTimeout(100, TimeUnit.MILLISECONDS)
        .test()
        .awaitDone(5, TimeUnit.SECONDS)
        .assertResult(1);
    }
}
