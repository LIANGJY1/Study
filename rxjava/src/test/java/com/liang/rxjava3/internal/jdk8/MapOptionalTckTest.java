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

import java.util.Optional;

import org.reactivestreams.Publisher;
import org.testng.annotations.Test;

import com.liang.rxjava3.core.Flowable;
import com.liang.rxjava3.tck.BaseTck;

@Test
public class MapOptionalTckTest extends BaseTck<Integer> {

    @Override
    public Publisher<Integer> createPublisher(final long elements) {
        return
                Flowable.range(0, (int)(2 * elements)).mapOptional(v -> v % 2 == 0 ? Optional.of(v) : Optional.empty())
            ;
    }

    @Override
    public Publisher<Integer> createFailedPublisher() {
        return Flowable.just(1).<Integer>mapOptional(v -> null).onBackpressureDrop();
    }
}
