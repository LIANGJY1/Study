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

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.exceptions.TestException;
import com.liang.rxjava3.functions.Function;
import com.liang.rxjava3.processors.PublishProcessor;
import com.liang.rxjava3.subjects.CompletableSubject;
import com.liang.rxjava3.testsupport.TestHelper;

public class CompletableHideTest extends RxJavaTest {

    @Test
    public void never() {
        Completable.never()
        .hide()
        .test()
        .assertNotComplete()
        .assertNoErrors();
    }

    @Test
    public void complete() {
        Completable.complete()
        .hide()
        .test()
        .assertResult();
    }

    @Test
    public void error() {
        Completable.error(new TestException())
        .hide()
        .test()
        .assertFailure(TestException.class);
    }

    @Test
    public void hidden() {
        assertFalse(CompletableSubject.create().hide() instanceof CompletableSubject);
    }

    @Test
    public void dispose() {
        TestHelper.checkDisposedCompletable(new Function<Completable, CompletableSource>() {
            @Override
            public CompletableSource apply(Completable m) throws Exception {
                return m.hide();
            }
        });
    }

    @Test
    public void isDisposed() {
        PublishProcessor<Integer> pp = PublishProcessor.create();

        TestHelper.checkDisposed(pp.ignoreElements().hide());
    }

    @Test
    public void doubleOnSubscribe() {
        TestHelper.checkDoubleOnSubscribeCompletable(new Function<Completable, Completable>() {
            @Override
            public Completable apply(Completable f) throws Exception {
                return f.hide();
            }
        });
    }
}
