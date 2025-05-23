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

import static org.junit.Assert.assertTrue;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.functions.Action;
import com.liang.rxjava3.observers.TestObserver;
import com.liang.rxjava3.schedulers.*;
import com.liang.rxjava3.testsupport.TestHelper;

public class CompletableTimerTest extends RxJavaTest {

    @Test
    public void dispose() {
        TestHelper.checkDisposed(Completable.timer(1, TimeUnit.SECONDS, new TestScheduler()));
    }

    @Test
    public void timerInterruptible() throws Exception {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        try {
            for (Scheduler s : new Scheduler[] { Schedulers.single(), Schedulers.computation(), Schedulers.newThread(), Schedulers.io(), Schedulers.from(exec, true) }) {
                final AtomicBoolean interrupted = new AtomicBoolean();
                TestObserver<Void> to = Completable.timer(1, TimeUnit.MILLISECONDS, s)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        try {
                        Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            interrupted.set(true);
                        }
                    }
                })
                .test();

                Thread.sleep(500);

                to.dispose();

                Thread.sleep(500);

                assertTrue(s.getClass().getSimpleName(), interrupted.get());
            }
        } finally {
            exec.shutdown();
        }
    }

}
