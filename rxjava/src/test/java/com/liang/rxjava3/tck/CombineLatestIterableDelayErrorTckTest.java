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

package com.liang.rxjava3.tck;

import java.util.Arrays;

import org.reactivestreams.Publisher;
import org.testng.annotations.Test;

import com.liang.rxjava3.core.Flowable;
import com.liang.rxjava3.functions.Function;

@Test
public class CombineLatestIterableDelayErrorTckTest extends BaseTck<Long> {

    @Override
    public Publisher<Long> createPublisher(long elements) {
        return
            Flowable.combineLatestDelayError(Arrays.asList(
                    Flowable.just(1L),
                    Flowable.fromIterable(iterate(elements))
                ),
                new Function<Object[], Long>() {
                    @Override
                    public Long apply(Object[] a) throws Exception {
                        return (Long)a[0];
                    }
                }
            )
        ;
    }
}
