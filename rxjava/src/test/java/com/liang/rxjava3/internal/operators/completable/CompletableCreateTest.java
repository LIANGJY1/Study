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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.disposables.*;
import com.liang.rxjava3.exceptions.TestException;
import com.liang.rxjava3.functions.Cancellable;
import com.liang.rxjava3.plugins.RxJavaPlugins;
import com.liang.rxjava3.testsupport.TestHelper;

public class CompletableCreateTest extends RxJavaTest {

    @Test
    public void basic() {
        List<Throwable> errors = TestHelper.trackPluginErrors();
        try {
            final Disposable d = Disposable.empty();

            Completable.create(new CompletableOnSubscribe() {
                @Override
                public void subscribe(CompletableEmitter e) throws Exception {
                    e.setDisposable(d);

                    e.onComplete();
                    e.onError(new TestException());
                    e.onComplete();
                }
            })
            .test()
            .assertResult();

            assertTrue(d.isDisposed());

            TestHelper.assertUndeliverable(errors, 0, TestException.class);
        } finally {
            RxJavaPlugins.reset();
        }
    }

    @Test
    public void basicWithCancellable() {
        List<Throwable> errors = TestHelper.trackPluginErrors();
        try {
            final Disposable d1 = Disposable.empty();
            final Disposable d2 = Disposable.empty();

            Completable.create(new CompletableOnSubscribe() {
                @Override
                public void subscribe(CompletableEmitter e) throws Exception {
                    e.setDisposable(d1);
                    e.setCancellable(new Cancellable() {
                        @Override
                        public void cancel() throws Exception {
                            d2.dispose();
                        }
                    });

                    e.onComplete();
                    e.onError(new TestException());
                    e.onComplete();
                }
            })
            .test()
            .assertResult();

            assertTrue(d1.isDisposed());
            assertTrue(d2.isDisposed());

            TestHelper.assertUndeliverable(errors, 0, TestException.class);
        } finally {
            RxJavaPlugins.reset();
        }
    }

    @Test
    public void basicWithError() {
        List<Throwable> errors = TestHelper.trackPluginErrors();
        try {
            final Disposable d = Disposable.empty();

            Completable.create(new CompletableOnSubscribe() {
                @Override
                public void subscribe(CompletableEmitter e) throws Exception {
                    e.setDisposable(d);

                    e.onError(new TestException());
                    e.onComplete();
                    e.onError(new TestException("second"));
                }
            })
            .test()
            .assertFailure(TestException.class);

            assertTrue(d.isDisposed());

            TestHelper.assertUndeliverable(errors, 0, TestException.class, "second");
        } finally {
            RxJavaPlugins.reset();
        }
    }

    @Test
    public void callbackThrows() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                throw new TestException();
            }
        })
        .test()
        .assertFailure(TestException.class);
    }

    @Test
    public void onErrorNull() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                e.onError(null);
            }
        })
        .test()
        .assertFailure(NullPointerException.class);
    }

    @Test
    public void dispose() {
        TestHelper.checkDisposed(Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                e.onComplete();
            }
        }));
    }

    @Test
    public void onErrorThrows() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                Disposable d = Disposable.empty();
                e.setDisposable(d);

                try {
                    e.onError(new IOException());
                    fail("Should have thrown");
                } catch (TestException ex) {
                    // expected
                }

                assertTrue(d.isDisposed());
                assertTrue(e.isDisposed());
            }
        }).subscribe(new CompletableObserver() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                throw new TestException();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Test
    public void onCompleteThrows() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                Disposable d = Disposable.empty();
                e.setDisposable(d);

                try {
                    e.onComplete();
                    fail("Should have thrown");
                } catch (TestException ex) {
                    // expected
                }

                assertTrue(d.isDisposed());
                assertTrue(e.isDisposed());
            }
        }).subscribe(new CompletableObserver() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                throw new TestException();
            }
        });
    }

    @Test
    public void onErrorThrows2() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    e.onError(new IOException());
                    fail("Should have thrown");
                } catch (TestException ex) {
                    // expected
                }

                assertTrue(e.isDisposed());
            }
        }).subscribe(new CompletableObserver() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {
                throw new TestException();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Test
    public void onCompleteThrows2() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter e) throws Exception {
                try {
                    e.onComplete();
                    fail("Should have thrown");
                } catch (TestException ex) {
                    // expected
                }

                assertTrue(e.isDisposed());
            }
        }).subscribe(new CompletableObserver() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                throw new TestException();
            }
        });
    }

    @Test
    public void tryOnError() {
        List<Throwable> errors = TestHelper.trackPluginErrors();
        try {
            final Boolean[] response = { null };
            Completable.create(new CompletableOnSubscribe() {
                @Override
                public void subscribe(CompletableEmitter e) throws Exception {
                    e.onComplete();
                    response[0] = e.tryOnError(new TestException());
                }
            })
            .test()
            .assertResult();

            assertFalse(response[0]);

            assertTrue(errors.toString(), errors.isEmpty());
        } finally {
            RxJavaPlugins.reset();
        }
    }

    @Test
    public void emitterHasToString() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                assertTrue(emitter.toString().contains(CompletableCreate.Emitter.class.getSimpleName()));
            }
        }).test().assertEmpty();
    }
}
