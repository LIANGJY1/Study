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

package com.liang.rxjava3.internal.operators.flowable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.functions.*;
import com.liang.rxjava3.operators.QueueFuseable;
import com.liang.rxjava3.plugins.RxJavaPlugins;
import com.liang.rxjava3.schedulers.Schedulers;
import com.liang.rxjava3.subscribers.TestSubscriber;
import com.liang.rxjava3.testsupport.*;

public class FlowableFromActionTest extends RxJavaTest {
    @Test
    public void fromAction() {
        final AtomicInteger atomicInteger = new AtomicInteger();

        Flowable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                atomicInteger.incrementAndGet();
            }
        })
            .test()
            .assertResult();

        assertEquals(1, atomicInteger.get());
    }

    @Test
    public void fromActionTwice() {
        final AtomicInteger atomicInteger = new AtomicInteger();

        Action run = new Action() {
            @Override
            public void run() throws Exception {
                atomicInteger.incrementAndGet();
            }
        };

        Flowable.fromAction(run)
            .test()
            .assertResult();

        assertEquals(1, atomicInteger.get());

        Flowable.fromAction(run)
            .test()
            .assertResult();

        assertEquals(2, atomicInteger.get());
    }

    @Test
    public void fromActionInvokesLazy() {
        final AtomicInteger atomicInteger = new AtomicInteger();

        Flowable<Object> source = Flowable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                atomicInteger.incrementAndGet();
            }
        });

        assertEquals(0, atomicInteger.get());

        source
            .test()
            .assertResult();

        assertEquals(1, atomicInteger.get());
    }

    @Test
    public void fromActionThrows() {
        Flowable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                throw new UnsupportedOperationException();
            }
        })
            .test()
            .assertFailure(UnsupportedOperationException.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void callable() throws Throwable {
        final int[] counter = { 0 };

        Flowable<Void> m = Flowable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                counter[0]++;
            }
        });

        assertTrue(m.getClass().toString(), m instanceof Supplier);

        assertNull(((Supplier<Void>)m).get());

        assertEquals(1, counter[0]);
    }

    @Test
    public void noErrorLoss() throws Exception {
        List<Throwable> errors = TestHelper.trackPluginErrors();
        try {
            final CountDownLatch cdl1 = new CountDownLatch(1);
            final CountDownLatch cdl2 = new CountDownLatch(1);

            TestSubscriber<Object> ts = Flowable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    cdl1.countDown();
                    cdl2.await(5, TimeUnit.SECONDS);
                }
            }).subscribeOn(Schedulers.single()).test();

            assertTrue(cdl1.await(5, TimeUnit.SECONDS));

            ts.cancel();

            int timeout = 10;

            while (timeout-- > 0 && errors.isEmpty()) {
                Thread.sleep(100);
            }

            TestHelper.assertUndeliverable(errors, 0, InterruptedException.class);
        } finally {
            RxJavaPlugins.reset();
        }
    }

    @Test
    public void disposedUpfront() throws Throwable {
        Action run = mock(Action.class);

        Flowable.fromAction(run)
        .test(1L, true)
        .assertEmpty();

        verify(run, never()).run();
    }

    @Test
    public void cancelWhileRunning() {
        final TestSubscriber<Object> ts = new TestSubscriber<>();

        Flowable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                ts.cancel();
            }
        })
        .subscribeWith(ts)
        .assertEmpty();

        assertTrue(ts.isCancelled());
    }

    @Test
    public void asyncFused() throws Throwable {
        TestSubscriberEx<Object> ts = new TestSubscriberEx<>();
        ts.setInitialFusionMode(QueueFuseable.ASYNC);

        Action action = mock(Action.class);

        Flowable.fromAction(action)
        .subscribe(ts);

        ts.assertFusionMode(QueueFuseable.ASYNC)
        .assertResult();

        verify(action).run();
    }

    @Test
    public void syncFusedRejected() throws Throwable {
        TestSubscriberEx<Object> ts = new TestSubscriberEx<>();
        ts.setInitialFusionMode(QueueFuseable.SYNC);

        Action action = mock(Action.class);

        Flowable.fromAction(action)
        .subscribe(ts);

        ts.assertFusionMode(QueueFuseable.NONE)
        .assertResult();

        verify(action).run();
    }
}
