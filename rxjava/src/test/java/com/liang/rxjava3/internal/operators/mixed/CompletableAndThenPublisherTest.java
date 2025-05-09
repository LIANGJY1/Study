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

package com.liang.rxjava3.internal.operators.mixed;

import static org.junit.Assert.*;

import org.junit.Test;
import org.reactivestreams.Publisher;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.functions.Function;
import com.liang.rxjava3.processors.PublishProcessor;
import com.liang.rxjava3.subjects.CompletableSubject;
import com.liang.rxjava3.subscribers.TestSubscriber;
import com.liang.rxjava3.testsupport.TestHelper;

public class CompletableAndThenPublisherTest extends RxJavaTest {

    @Test
    public void cancelMain() {
        CompletableSubject cs = CompletableSubject.create();
        PublishProcessor<Integer> pp = PublishProcessor.create();

        TestSubscriber<Integer> ts = cs.andThen(pp)
                .test();

        assertTrue(cs.hasObservers());
        assertFalse(pp.hasSubscribers());

        ts.cancel();

        assertFalse(cs.hasObservers());
        assertFalse(pp.hasSubscribers());
    }

    @Test
    public void cancelOther() {
        CompletableSubject cs = CompletableSubject.create();
        PublishProcessor<Integer> pp = PublishProcessor.create();

        TestSubscriber<Integer> ts = cs.andThen(pp)
                .test();

        assertTrue(cs.hasObservers());
        assertFalse(pp.hasSubscribers());

        cs.onComplete();

        assertFalse(cs.hasObservers());
        assertTrue(pp.hasSubscribers());

        ts.cancel();

        assertFalse(cs.hasObservers());
        assertFalse(pp.hasSubscribers());
    }

    @Test
    public void doubleOnSubscribe() {
        TestHelper.checkDoubleOnSubscribeCompletableToFlowable(new Function<Completable, Publisher<Object>>() {
            @Override
            public Publisher<Object> apply(Completable m) throws Exception {
                return m.andThen(Flowable.never());
            }
        });
    }
}
