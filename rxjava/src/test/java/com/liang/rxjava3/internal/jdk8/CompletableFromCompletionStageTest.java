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

package com.liang.rxjava3.internal.jdk8;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.exceptions.TestException;
import com.liang.rxjava3.observers.TestObserver;
import com.liang.rxjava3.testsupport.TestHelper;

public class CompletableFromCompletionStageTest extends RxJavaTest {

    @Test
    public void syncSuccess() {
        Completable.fromCompletionStage(CompletableFuture.completedFuture(1))
        .test()
        .assertResult();
    }

    @Test
    public void syncSuccessNull() {
        Completable.fromCompletionStage(CompletableFuture.completedFuture(null))
        .test()
        .assertResult();
    }

    @Test
    public void syncFailure() {
        CompletableFuture<Integer> cf = new CompletableFuture<>();
        cf.completeExceptionally(new TestException());

        Completable.fromCompletionStage(cf)
        .test()
        .assertFailure(TestException.class);
    }

    @Test
    public void syncNull() {
        Completable.fromCompletionStage(CompletableFuture.<Integer>completedFuture(null))
        .test()
        .assertResult();
    }

    @Test
    public void dispose() {
        CompletableFuture<Integer> cf = new CompletableFuture<>();

        TestObserver<Void> to = Completable.fromCompletionStage(cf)
        .test();

        to.assertEmpty();

        to.dispose();

        cf.complete(1);

        to.assertEmpty();
    }

    @Test
    public void dispose2() {
        TestHelper.checkDisposed(Completable.fromCompletionStage(new CompletableFuture<>()));
    }
}
