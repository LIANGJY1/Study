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

package com.liang.rxjava3.processors;

import static org.junit.Assert.assertEquals;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.*;
import org.reactivestreams.*;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.functions.Consumer;
import com.liang.rxjava3.schedulers.Schedulers;
import com.liang.rxjava3.subscribers.DefaultSubscriber;
import com.liang.rxjava3.testsupport.TestSubscriberEx;

public class ReplayProcessorConcurrencyTest extends RxJavaTest {

    @Test
    public void replaySubjectConcurrentSubscribersDoingReplayDontBlockEachOther() throws InterruptedException {
        final ReplayProcessor<Long> replay = ReplayProcessor.create();
        Thread source = new Thread(new Runnable() {

            @Override
            public void run() {
                Flowable.unsafeCreate(new Publisher<Long>() {

                    @Override
                    public void subscribe(Subscriber<? super Long> subscriber) {
                        System.out.println("********* Start Source Data ***********");
                        for (long l = 1; l <= 10000; l++) {
                            subscriber.onNext(l);
                        }
                        System.out.println("********* Finished Source Data ***********");
                        subscriber.onComplete();
                    }
                }).subscribe(replay);
            }
        });
        source.start();

        long v = replay.blockingLast();
        assertEquals(10000, v);

        // it's been played through once so now it will all be replays
        final CountDownLatch slowLatch = new CountDownLatch(1);
        Thread slowThread = new Thread(new Runnable() {

            @Override
            public void run() {
                Subscriber<Long> slow = new DefaultSubscriber<Long>() {

                    @Override
                    public void onComplete() {
                        System.out.println("*** Slow Observer completed");
                        slowLatch.countDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long args) {
                        if (args == 1) {
                            System.out.println("*** Slow Observer STARTED");
                        }
                        try {
                            if (args % 10 == 0) {
                                Thread.sleep(1);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                replay.subscribe(slow);
                try {
                    slowLatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        slowThread.start();

        Thread fastThread = new Thread(new Runnable() {

            @Override
            public void run() {
                final CountDownLatch fastLatch = new CountDownLatch(1);
                Subscriber<Long> fast = new DefaultSubscriber<Long>() {

                    @Override
                    public void onComplete() {
                        System.out.println("*** Fast Observer completed");
                        fastLatch.countDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long args) {
                        if (args == 1) {
                            System.out.println("*** Fast Observer STARTED");
                        }
                    }
                };
                replay.subscribe(fast);
                try {
                    fastLatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        fastThread.start();
        fastThread.join();

        // slow should not yet be completed when fast completes
        assertEquals(1, slowLatch.getCount());

        slowThread.join();
    }

    @Test
    public void replaySubjectConcurrentSubscriptions() throws InterruptedException {
        final ReplayProcessor<Long> replay = ReplayProcessor.create();
        Thread source = new Thread(new Runnable() {

            @Override
            public void run() {
                Flowable.unsafeCreate(new Publisher<Long>() {

                    @Override
                    public void subscribe(Subscriber<? super Long> subscriber) {
                        System.out.println("********* Start Source Data ***********");
                        for (long l = 1; l <= 10000; l++) {
                            subscriber.onNext(l);
                        }
                        System.out.println("********* Finished Source Data ***********");
                        subscriber.onComplete();
                    }
                }).subscribe(replay);
            }
        });

        // used to collect results of each thread
        final List<List<Long>> listOfListsOfValues = Collections.synchronizedList(new ArrayList<>());
        final List<Thread> threads = Collections.synchronizedList(new ArrayList<>());

        for (int i = 1; i <= 200; i++) {
            final int count = i;
            if (count == 20) {
                // start source data after we have some already subscribed
                // and while others are in process of subscribing
                source.start();
            }
            if (count == 100) {
                // wait for source to finish then keep adding after it's done
                source.join();
            }
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    List<Long> values = replay.toList().blockingGet();
                    listOfListsOfValues.add(values);
                    System.out.println("Finished thread: " + count);
                }
            });
            t.start();
            System.out.println("Started thread: " + i);
            threads.add(t);
        }

        // wait for all threads to complete
        for (Thread t : threads) {
            t.join();
        }

        // assert all threads got the same results
        List<Long> sums = new ArrayList<>();
        for (List<Long> values : listOfListsOfValues) {
            long v = 0;
            for (long l : values) {
                v += l;
            }
            sums.add(v);
        }

        long expected = sums.get(0);
        boolean success = true;
        for (long l : sums) {
            if (l != expected) {
                success = false;
                System.out.println("FAILURE => Expected " + expected + " but got: " + l);
            }
        }

        if (success) {
            System.out.println("Success! " + sums.size() + " each had the same sum of " + expected);
        } else {
            throw new RuntimeException("Concurrency Bug");
        }

    }

    /**
     * Can receive timeout if subscribe never receives an onError/onComplete ... which reveals a race condition.
     */
    @Test
    public void subscribeCompletionRaceCondition() {
        for (int i = 0; i < 50; i++) {
            final ReplayProcessor<String> processor = ReplayProcessor.create();
            final AtomicReference<String> value1 = new AtomicReference<>();

            processor.subscribe(new Consumer<String>() {

                @Override
                public void accept(String t1) {
                    try {
                        // simulate a slow observer
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    value1.set(t1);
                }

            });

            Thread t1 = new Thread(new Runnable() {

                @Override
                public void run() {
                    processor.onNext("value");
                    processor.onComplete();
                }
            });

            SubjectObserverThread t2 = new SubjectObserverThread(processor);
            SubjectObserverThread t3 = new SubjectObserverThread(processor);
            SubjectObserverThread t4 = new SubjectObserverThread(processor);
            SubjectObserverThread t5 = new SubjectObserverThread(processor);

            t2.start();
            t3.start();
            t1.start();
            t4.start();
            t5.start();
            try {
                t1.join();
                t2.join();
                t3.join();
                t4.join();
                t5.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            assertEquals("value", value1.get());
            assertEquals("value", t2.value.get());
            assertEquals("value", t3.value.get());
            assertEquals("value", t4.value.get());
            assertEquals("value", t5.value.get());
        }

    }

    /**
     * Make sure emission-subscription races are handled correctly.
     * https://github.com/ReactiveX/RxJava/issues/1147
     */
    @Test
    public void raceForTerminalState() {
        final List<Integer> expected = Arrays.asList(1);
        for (int i = 0; i < 100000; i++) {
            TestSubscriberEx<Integer> ts = new TestSubscriberEx<>();
            Flowable.just(1).subscribeOn(Schedulers.computation()).cache().subscribe(ts);
            ts.awaitDone(5, TimeUnit.SECONDS);
            ts.assertValueSequence(expected);
            ts.assertTerminated();
        }
    }

    static class SubjectObserverThread extends Thread {

        private final ReplayProcessor<String> processor;
        private final AtomicReference<String> value = new AtomicReference<>();

        SubjectObserverThread(ReplayProcessor<String> processor) {
            this.processor = processor;
        }

        @Override
        public void run() {
            try {
                // a timeout exception will happen if we don't get a terminal state
                String v = processor.timeout(2000, TimeUnit.MILLISECONDS).blockingSingle();
                value.set(v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void replaySubjectEmissionSubscriptionRace() throws Exception {
        Scheduler s = Schedulers.io();
        Scheduler.Worker worker = Schedulers.io().createWorker();
        try {
            for (int i = 0; i < 50000; i++) {
                if (i % 1000 == 0) {
                    System.out.println(i);
                }
                final ReplayProcessor<Object> rs = ReplayProcessor.create();

                final CountDownLatch finish = new CountDownLatch(1);
                final CountDownLatch start = new CountDownLatch(1);

                worker.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            start.await();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        rs.onNext(1);
                    }
                });

                final AtomicReference<Object> o = new AtomicReference<>();

                rs.subscribeOn(s).observeOn(Schedulers.io())
                .subscribe(new DefaultSubscriber<Object>() {

                    @Override
                    public void onComplete() {
                        o.set(-1);
                        finish.countDown();
                    }

                    @Override
                    public void onError(Throwable e) {
                        o.set(e);
                        finish.countDown();
                    }

                    @Override
                    public void onNext(Object t) {
                        o.set(t);
                        finish.countDown();
                    }

                });
                start.countDown();

                if (!finish.await(5, TimeUnit.SECONDS)) {
                    System.out.println(o.get());
                    System.out.println(rs.hasSubscribers());
                    rs.onComplete();
                    Assert.fail("Timeout @ " + i);
                    break;
                } else {
                    Assert.assertEquals(1, o.get());
                    worker.schedule(new Runnable() {
                        @Override
                        public void run() {
                            rs.onComplete();
                        }
                    });

                }
            }
        } finally {
            worker.dispose();
        }
    }

    @Test
    public void concurrentSizeAndHasAnyValue() throws InterruptedException {
        final ReplayProcessor<Object> rs = ReplayProcessor.create();
        final CyclicBarrier cb = new CyclicBarrier(2);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cb.await();
                } catch (InterruptedException e) {
                    return;
                } catch (BrokenBarrierException e) {
                    return;
                }
                for (int i = 0; i < 1000000; i++) {
                    rs.onNext(i);
                }
                rs.onComplete();
                System.out.println("Replay fill Thread finished!");
            }
        });
        t.start();
        try {
            cb.await();
        } catch (InterruptedException e) {
            return;
        } catch (BrokenBarrierException e) {
            return;
        }
        int lastSize = 0;
        for (; !rs.hasThrowable() && !rs.hasComplete();) {
            int size = rs.size();
            boolean hasAny = rs.hasValue();
            Object[] values = rs.getValues();
            if (size < lastSize) {
                Assert.fail("Size decreased! " + lastSize + " -> " + size);
            }
            if ((size > 0) && !hasAny) {
                Assert.fail("hasAnyValue reports emptyness but size doesn't");
            }
            if (size > values.length) {
                Assert.fail("Got fewer values than size! " + size + " -> " + values.length);
            }
            lastSize = size;
        }

        t.join();
    }
}
