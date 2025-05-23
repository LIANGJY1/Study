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

package com.liang.rxjava3.internal.operators.maybe;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.liang.rxjava3.core.Maybe;
import com.liang.rxjava3.exceptions.TestException;
import com.liang.rxjava3.observers.TestObserver;
import com.liang.rxjava3.schedulers.*;
import com.liang.rxjava3.subjects.MaybeSubject;
import com.liang.rxjava3.testsupport.TestHelper;

public class MaybeTimestampTest {

    @Test
    public void just() {
        Maybe.just(1)
        .timestamp()
        .test()
        .assertValueCount(1)
        .assertNoErrors()
        .assertComplete();
    }

    @Test
    public void empty() {
        Maybe.empty()
        .timestamp()
        .test()
        .assertResult();
    }

    @Test
    public void error() {
        Maybe.error(new TestException())
        .timestamp()
        .test()
        .assertFailure(TestException.class);
    }

    @Test
    public void justSeconds() {
        Maybe.just(1)
        .timestamp(TimeUnit.SECONDS)
        .test()
        .assertValueCount(1)
        .assertNoErrors()
        .assertComplete();
    }

    @Test
    public void justScheduler() {
        Maybe.just(1)
        .timestamp(Schedulers.single())
        .test()
        .assertValueCount(1)
        .assertNoErrors()
        .assertComplete();
    }

    @Test
    public void justSecondsScheduler() {
        Maybe.just(1)
        .timestamp(TimeUnit.SECONDS, Schedulers.single())
        .test()
        .assertValueCount(1)
        .assertNoErrors()
        .assertComplete();
    }

    @Test
    public void doubleOnSubscribe() {
        TestHelper.checkDoubleOnSubscribeMaybe(m -> m.timestamp());
    }

    @Test
    public void dispose() {
        TestHelper.checkDisposed(MaybeSubject.create().timestamp());
    }

    @Test
    public void timeInfo() {
        TestScheduler scheduler = new TestScheduler();

        MaybeSubject<Integer> ms = MaybeSubject.create();

        TestObserver<Timed<Integer>> to = ms
        .timestamp(scheduler)
        .test();

        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS);

        ms.onSuccess(1);

        to.assertResult(new Timed<>(1, 1000L, TimeUnit.MILLISECONDS));
    }
}
