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

import static org.junit.Assert.*;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.disposables.Disposable;
import com.liang.rxjava3.exceptions.*;
import com.liang.rxjava3.subjects.*;
import com.liang.rxjava3.testsupport.TestHelper;

public class ObservableStageSubscriberOrDefaultTest extends RxJavaTest {

    @Test
    public void firstJust() throws Exception {
        Integer v = Observable.just(1)
                .firstStage(null)
                .toCompletableFuture()
                .get();

        assertEquals((Integer)1, v);
    }

    @Test
    public void firstEmpty() throws Exception {
        Integer v = Observable.<Integer>empty()
                .firstStage(2)
                .toCompletableFuture()
                .get();

        assertEquals((Integer)2, v);
    }

    @Test
    public void firstCancels() throws Exception {
        BehaviorSubject<Integer> source = BehaviorSubject.createDefault(1);

        Integer v = source
                .firstStage(null)
                .toCompletableFuture()
                .get();

        assertEquals((Integer)1, v);

        assertFalse(source.hasObservers());
    }

    @Test
    public void firstCompletableFutureCancels() throws Exception {
        PublishSubject<Integer> source = PublishSubject.create();

        CompletableFuture<Integer> cf = source
                .firstStage(null)
                .toCompletableFuture();

        assertTrue(source.hasObservers());

        cf.cancel(true);

        assertTrue(cf.isCancelled());

        assertFalse(source.hasObservers());
    }

    @Test
    public void firstCompletableManualCompleteCancels() throws Exception {
        PublishSubject<Integer> source = PublishSubject.create();

        CompletableFuture<Integer> cf = source
                .firstStage(null)
                .toCompletableFuture();

        assertTrue(source.hasObservers());

        cf.complete(1);

        assertTrue(cf.isDone());
        assertFalse(cf.isCompletedExceptionally());
        assertFalse(cf.isCancelled());

        assertFalse(source.hasObservers());

        assertEquals((Integer)1, cf.get());
    }

    @Test
    public void firstCompletableManualCompleteExceptionallyCancels() throws Exception {
        PublishSubject<Integer> source = PublishSubject.create();

        CompletableFuture<Integer> cf = source
                .firstStage(null)
                .toCompletableFuture();

        assertTrue(source.hasObservers());

        cf.completeExceptionally(new TestException());

        assertTrue(cf.isDone());
        assertTrue(cf.isCompletedExceptionally());
        assertFalse(cf.isCancelled());

        assertFalse(source.hasObservers());

        TestHelper.assertError(cf, TestException.class);
    }

    @Test
    public void firstError() throws Exception {
        CompletableFuture<Integer> cf = Observable.<Integer>error(new TestException())
                .firstStage(null)
                .toCompletableFuture();

        assertTrue(cf.isDone());
        assertTrue(cf.isCompletedExceptionally());
        assertFalse(cf.isCancelled());

        TestHelper.assertError(cf, TestException.class);
    }

    @Test
    public void firstSourceIgnoresCancel() throws Throwable {
        TestHelper.withErrorTracking(errors -> {
            Integer v = new Observable<Integer>() {
                @Override
                protected void subscribeActual(Observer<? super Integer> observer) {
                    observer.onSubscribe(Disposable.empty());
                    observer.onNext(1);
                    observer.onError(new TestException());
                    observer.onComplete();
                }
            }
            .firstStage(null)
            .toCompletableFuture()
            .get();

            assertEquals((Integer)1, v);

            TestHelper.assertUndeliverable(errors, 0, TestException.class);
        });
    }

    @Test
    public void firstDoubleOnSubscribe() throws Throwable {
        TestHelper.withErrorTracking(errors -> {
            Integer v = new Observable<Integer>() {
                @Override
                protected void subscribeActual(Observer<? super Integer> observer) {
                    observer.onSubscribe(Disposable.empty());
                    observer.onSubscribe(Disposable.empty());
                    observer.onNext(1);
                }
            }
            .firstStage(null)
            .toCompletableFuture()
            .get();

            assertEquals((Integer)1, v);

            TestHelper.assertError(errors, 0, ProtocolViolationException.class);
        });
    }

    @Test
    public void singleJust() throws Exception {
        Integer v = Observable.just(1)
                .singleStage(null)
                .toCompletableFuture()
                .get();

        assertEquals((Integer)1, v);
    }

    @Test
    public void singleEmpty() throws Exception {
        Integer v = Observable.<Integer>empty()
                .singleStage(2)
                .toCompletableFuture()
                .get();

        assertEquals((Integer)2, v);
    }

    @Test
    public void singleTooManyCancels() throws Exception {
        ReplaySubject<Integer> source = ReplaySubject.create();
        source.onNext(1);
        source.onNext(2);

        TestHelper.assertError(source
                .singleStage(null)
                .toCompletableFuture(), IllegalArgumentException.class);

        assertFalse(source.hasObservers());
    }

    @Test
    public void singleCompletableFutureCancels() throws Exception {
        PublishSubject<Integer> source = PublishSubject.create();

        CompletableFuture<Integer> cf = source
                .singleStage(null)
                .toCompletableFuture();

        assertTrue(source.hasObservers());

        cf.cancel(true);

        assertTrue(cf.isCancelled());

        assertFalse(source.hasObservers());
    }

    @Test
    public void singleCompletableManualCompleteCancels() throws Exception {
        PublishSubject<Integer> source = PublishSubject.create();

        CompletableFuture<Integer> cf = source
                .singleStage(null)
                .toCompletableFuture();

        assertTrue(source.hasObservers());

        cf.complete(1);

        assertTrue(cf.isDone());
        assertFalse(cf.isCompletedExceptionally());
        assertFalse(cf.isCancelled());

        assertFalse(source.hasObservers());

        assertEquals((Integer)1, cf.get());
    }

    @Test
    public void singleCompletableManualCompleteExceptionallyCancels() throws Exception {
        PublishSubject<Integer> source = PublishSubject.create();

        CompletableFuture<Integer> cf = source
                .singleStage(null)
                .toCompletableFuture();

        assertTrue(source.hasObservers());

        cf.completeExceptionally(new TestException());

        assertTrue(cf.isDone());
        assertTrue(cf.isCompletedExceptionally());
        assertFalse(cf.isCancelled());

        assertFalse(source.hasObservers());

        TestHelper.assertError(cf, TestException.class);
    }

    @Test
    public void singleError() throws Exception {
        CompletableFuture<Integer> cf = Observable.<Integer>error(new TestException())
                .singleStage(null)
                .toCompletableFuture();

        assertTrue(cf.isDone());
        assertTrue(cf.isCompletedExceptionally());
        assertFalse(cf.isCancelled());

        TestHelper.assertError(cf, TestException.class);
    }

    @Test
    public void singleSourceIgnoresCancel() throws Throwable {
        TestHelper.withErrorTracking(errors -> {
            Integer v = new Observable<Integer>() {
                @Override
                protected void subscribeActual(Observer<? super Integer> observer) {
                    observer.onSubscribe(Disposable.empty());
                    observer.onNext(1);
                    observer.onComplete();
                    observer.onError(new TestException());
                    observer.onComplete();
                }
            }
            .singleStage(null)
            .toCompletableFuture()
            .get();

            assertEquals((Integer)1, v);

            TestHelper.assertUndeliverable(errors, 0, TestException.class);
        });
    }

    @Test
    public void singleDoubleOnSubscribe() throws Throwable {
        TestHelper.withErrorTracking(errors -> {
            Integer v = new Observable<Integer>() {
                @Override
                protected void subscribeActual(Observer<? super Integer> observer) {
                    observer.onSubscribe(Disposable.empty());
                    observer.onSubscribe(Disposable.empty());
                    observer.onNext(1);
                    observer.onComplete();
                }
            }
            .singleStage(null)
            .toCompletableFuture()
            .get();

            assertEquals((Integer)1, v);

            TestHelper.assertError(errors, 0, ProtocolViolationException.class);
        });
    }

    @Test
    public void lastJust() throws Exception {
        Integer v = Observable.just(1)
                .lastStage(null)
                .toCompletableFuture()
                .get();

        assertEquals((Integer)1, v);
    }

    @Test
    public void lastRange() throws Exception {
        Integer v = Observable.range(1, 5)
                .lastStage(null)
                .toCompletableFuture()
                .get();

        assertEquals((Integer)5, v);
    }

    @Test
    public void lastEmpty() throws Exception {
        Integer v = Observable.<Integer>empty()
                .lastStage(2)
                .toCompletableFuture()
                .get();

        assertEquals((Integer)2, v);
    }

    @Test
    public void lastCompletableFutureCancels() throws Exception {
        PublishSubject<Integer> source = PublishSubject.create();

        CompletableFuture<Integer> cf = source
                .lastStage(null)
                .toCompletableFuture();

        assertTrue(source.hasObservers());

        cf.cancel(true);

        assertTrue(cf.isCancelled());

        assertFalse(source.hasObservers());
    }

    @Test
    public void lastCompletableManualCompleteCancels() throws Exception {
        PublishSubject<Integer> source = PublishSubject.create();

        CompletableFuture<Integer> cf = source
                .lastStage(null)
                .toCompletableFuture();

        assertTrue(source.hasObservers());

        cf.complete(1);

        assertTrue(cf.isDone());
        assertFalse(cf.isCompletedExceptionally());
        assertFalse(cf.isCancelled());

        assertFalse(source.hasObservers());

        assertEquals((Integer)1, cf.get());
    }

    @Test
    public void lastCompletableManualCompleteExceptionallyCancels() throws Exception {
        PublishSubject<Integer> source = PublishSubject.create();

        CompletableFuture<Integer> cf = source
                .lastStage(null)
                .toCompletableFuture();

        assertTrue(source.hasObservers());

        cf.completeExceptionally(new TestException());

        assertTrue(cf.isDone());
        assertTrue(cf.isCompletedExceptionally());
        assertFalse(cf.isCancelled());

        assertFalse(source.hasObservers());

        TestHelper.assertError(cf, TestException.class);
    }

    @Test
    public void lastError() throws Exception {
        CompletableFuture<Integer> cf = Observable.<Integer>error(new TestException())
                .lastStage(null)
                .toCompletableFuture();

        assertTrue(cf.isDone());
        assertTrue(cf.isCompletedExceptionally());
        assertFalse(cf.isCancelled());

        TestHelper.assertError(cf, TestException.class);
    }

    @Test
    public void lastSourceIgnoresCancel() throws Throwable {
        TestHelper.withErrorTracking(errors -> {
            Integer v = new Observable<Integer>() {
                @Override
                protected void subscribeActual(Observer<? super Integer> observer) {
                    observer.onSubscribe(Disposable.empty());
                    observer.onNext(1);
                    observer.onComplete();
                    observer.onError(new TestException());
                    observer.onComplete();
                }
            }
            .lastStage(null)
            .toCompletableFuture()
            .get();

            assertEquals((Integer)1, v);

            TestHelper.assertUndeliverable(errors, 0, TestException.class);
        });
    }

    @Test
    public void lastDoubleOnSubscribe() throws Throwable {
        TestHelper.withErrorTracking(errors -> {
            Integer v = new Observable<Integer>() {
                @Override
                protected void subscribeActual(Observer<? super Integer> observer) {
                    observer.onSubscribe(Disposable.empty());
                    observer.onSubscribe(Disposable.empty());
                    observer.onNext(1);
                    observer.onComplete();
                }
            }
            .lastStage(null)
            .toCompletableFuture()
            .get();

            assertEquals((Integer)1, v);

            TestHelper.assertError(errors, 0, ProtocolViolationException.class);
        });
    }
}
