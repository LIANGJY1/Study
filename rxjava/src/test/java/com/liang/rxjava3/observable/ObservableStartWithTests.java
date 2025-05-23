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

package com.liang.rxjava3.observable;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;

import com.liang.rxjava3.core.Observable;
import com.liang.rxjava3.core.RxJavaTest;

public class ObservableStartWithTests extends RxJavaTest {

    @Test
    public void startWith1() {
        List<String> values = Observable.just("one", "two")
                .startWithArray("zero").toList().blockingGet();

        assertEquals("zero", values.get(0));
        assertEquals("two", values.get(2));
    }

    @Test
    public void startWithIterable() {
        List<String> li = new ArrayList<>();
        li.add("alpha");
        li.add("beta");
        List<String> values = Observable.just("one", "two").startWithIterable(li).toList().blockingGet();

        assertEquals("alpha", values.get(0));
        assertEquals("beta", values.get(1));
        assertEquals("one", values.get(2));
        assertEquals("two", values.get(3));
    }

    @Test
    public void startWithObservable() {
        List<String> li = new ArrayList<>();
        li.add("alpha");
        li.add("beta");
        List<String> values = Observable.just("one", "two")
                .startWith(Observable.fromIterable(li))
                .toList()
                .blockingGet();

        assertEquals("alpha", values.get(0));
        assertEquals("beta", values.get(1));
        assertEquals("one", values.get(2));
        assertEquals("two", values.get(3));
    }

    @Test
    public void startWithEmpty() {
        Observable.just(1).startWithArray().test().assertResult(1);
    }
}
