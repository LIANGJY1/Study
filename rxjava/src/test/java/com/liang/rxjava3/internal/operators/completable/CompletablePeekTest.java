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

package com.liang.rxjava3.internal.operators.completable;

import java.util.List;

import org.junit.Test;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.exceptions.TestException;
import com.liang.rxjava3.functions.Action;
import com.liang.rxjava3.internal.functions.Functions;
import com.liang.rxjava3.plugins.RxJavaPlugins;
import com.liang.rxjava3.subjects.CompletableSubject;
import com.liang.rxjava3.testsupport.TestHelper;

public class CompletablePeekTest extends RxJavaTest {

    @Test
    public void onAfterTerminateCrashes() {
        List<Throwable> errors = TestHelper.trackPluginErrors();
        try {
            Completable.complete()
            .doAfterTerminate(new Action() {
                @Override
                public void run() throws Exception {
                    throw new TestException();
                }
            })
            .test()
            .assertResult();

            TestHelper.assertUndeliverable(errors, 0, TestException.class);
        } finally {
            RxJavaPlugins.reset();
        }
    }

    @Test
    public void disposed() {
        TestHelper.checkDisposed(CompletableSubject.create().doOnComplete(Functions.EMPTY_ACTION));
    }
}
