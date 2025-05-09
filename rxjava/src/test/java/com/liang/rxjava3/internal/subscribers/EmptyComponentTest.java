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

package com.liang.rxjava3.internal.subscribers;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.liang.rxjava3.core.RxJavaTest;
import com.liang.rxjava3.disposables.*;
import com.liang.rxjava3.exceptions.TestException;
import com.liang.rxjava3.internal.subscriptions.BooleanSubscription;
import com.liang.rxjava3.internal.util.EmptyComponent;
import com.liang.rxjava3.plugins.RxJavaPlugins;
import com.liang.rxjava3.testsupport.TestHelper;

public class EmptyComponentTest extends RxJavaTest {

    @Test
    public void normal() {
        List<Throwable> errors = TestHelper.trackPluginErrors();

        try {
            TestHelper.checkEnum(EmptyComponent.class);

            EmptyComponent c = EmptyComponent.INSTANCE;

            assertTrue(c.isDisposed());

            c.request(10);

            c.request(-10);

            Disposable d = Disposable.empty();

            c.onSubscribe(d);

            assertTrue(d.isDisposed());

            BooleanSubscription s = new BooleanSubscription();

            c.onSubscribe(s);

            assertTrue(s.isCancelled());

            c.onNext(null);

            c.onNext(1);

            c.onComplete();

            c.onError(new TestException());

            c.onSuccess(2);

            c.cancel();

            TestHelper.assertUndeliverable(errors, 0, TestException.class);
        } finally {
            RxJavaPlugins.reset();
        }
    }
}
