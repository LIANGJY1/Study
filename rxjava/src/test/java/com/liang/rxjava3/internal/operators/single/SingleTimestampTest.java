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

package com.liang.rxjava3.internal.operators.single;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.liang.rxjava3.core.Single;
import com.liang.rxjava3.exceptions.TestException;
import com.liang.rxjava3.observers.TestObserver;
import com.liang.rxjava3.schedulers.*;
import com.liang.rxjava3.subjects.SingleSubject;
import com.liang.rxjava3.testsupport.TestHelper;

public class SingleTimestampTest {

    @Test
    public void just() {
        Single.just(1)
        .timeInterval()
        .test()
        .assertValueCount(1)
        .assertNoErrors()
        .assertComplete();
    }

    @Test
    public void error() {
        Single.error(new TestException())
        .timeInterval()
        .test()
        .assertFailure(TestException.class);
    }

    @Test
    public void justSeconds() {
        Single.just(1)
        .timeInterval(TimeUnit.SECONDS)
        .test()
        .assertValueCount(1)
        .assertNoErrors()
        .assertComplete();
    }

    @Test
    public void justScheduler() {
        Single.just(1)
        .timeInterval(Schedulers.single())
        .test()
        .assertValueCount(1)
        .assertNoErrors()
        .assertComplete();
    }

    @Test
    public void justSecondsScheduler() {
        Single.just(1)
        .timeInterval(TimeUnit.SECONDS, Schedulers.single())
        .test()
        .assertValueCount(1)
        .assertNoErrors()
        .assertComplete();
    }

    @Test
    public void doubleOnSubscribe() {
        TestHelper.checkDoubleOnSubscribeSingle(m -> m.timeInterval());
    }

    @Test
    public void dispose() {
        TestHelper.checkDisposed(SingleSubject.create().timeInterval());
    }

    @Test
    public void timeInfo() {
        TestScheduler scheduler = new TestScheduler();

        SingleSubject<Integer> ss = SingleSubject.create();

        TestObserver<Timed<Integer>> to = ss
        .timeInterval(scheduler)
        .test();

        scheduler.advanceTimeBy(1000, TimeUnit.MILLISECONDS);

        ss.onSuccess(1);

        to.assertResult(new Timed<>(1, 1000L, TimeUnit.MILLISECONDS));
    }
}
