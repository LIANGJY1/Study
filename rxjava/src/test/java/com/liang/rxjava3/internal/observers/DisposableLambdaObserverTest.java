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

package com.liang.rxjava3.internal.observers;

import static org.junit.Assert.*;

import java.util.List;

import com.liang.rxjava3.disposables.Disposable;
import org.junit.Test;

import com.liang.rxjava3.core.RxJavaTest;
import com.liang.rxjava3.exceptions.TestException;
import com.liang.rxjava3.functions.Action;
import com.liang.rxjava3.internal.functions.Functions;
import com.liang.rxjava3.observers.TestObserver;
import com.liang.rxjava3.plugins.RxJavaPlugins;
import com.liang.rxjava3.testsupport.TestHelper;

public class DisposableLambdaObserverTest extends RxJavaTest {

    @Test
    public void doubleOnSubscribe() {
        TestHelper.doubleOnSubscribe(new DisposableLambdaObserver<>(
                new TestObserver<>(), Functions.emptyConsumer(), Functions.EMPTY_ACTION
        ));
    }

    @Test
    public void disposeCrash() {
        List<Throwable> errors = TestHelper.trackPluginErrors();
        try {
            DisposableLambdaObserver<Integer> o = new DisposableLambdaObserver<>(
                    new TestObserver<>(), Functions.emptyConsumer(),
                    new Action() {
                        @Override
                        public void run() throws Exception {
                            throw new TestException();
                        }
                    }
            );

            o.onSubscribe(Disposable.empty());

            assertFalse(o.isDisposed());

            o.dispose();

            assertTrue(o.isDisposed());

            TestHelper.assertUndeliverable(errors, 0, TestException.class);
        } finally {
            RxJavaPlugins.reset();
        }
    }
}
