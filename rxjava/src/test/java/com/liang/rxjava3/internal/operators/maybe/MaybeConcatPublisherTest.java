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

import java.util.concurrent.Callable;

import org.junit.Test;

import com.liang.rxjava3.core.*;

public class MaybeConcatPublisherTest extends RxJavaTest {

    @Test
    public void scalar() {
        Maybe.concat(Flowable.just(Maybe.just(1)))
        .test()
        .assertResult(1);
    }

    @Test
    public void callable() {
        Maybe.concat(Flowable.fromCallable(new Callable<Maybe<Integer>>() {
            @Override
            public Maybe<Integer> call() throws Exception {
                return Maybe.just(1);
            }
        }))
        .test()
        .assertResult(1);
    }
}
