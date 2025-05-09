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

import java.util.concurrent.atomic.AtomicReference;

import org.reactivestreams.Subscription;

import com.liang.rxjava3.core.FlowableSubscriber;
import com.liang.rxjava3.disposables.Disposable;
import com.liang.rxjava3.exceptions.*;
import com.liang.rxjava3.functions.*;
import com.liang.rxjava3.internal.functions.Functions;
import com.liang.rxjava3.internal.subscriptions.SubscriptionHelper;
import com.liang.rxjava3.observers.LambdaConsumerIntrospection;
import com.liang.rxjava3.plugins.RxJavaPlugins;

public final class BoundedSubscriber<T> extends AtomicReference<Subscription>
        implements FlowableSubscriber<T>, Subscription, Disposable, LambdaConsumerIntrospection {

    private static final long serialVersionUID = -7251123623727029452L;
    final Consumer<? super T> onNext;
    final Consumer<? super Throwable> onError;
    final Action onComplete;
    final Consumer<? super Subscription> onSubscribe;

    final int bufferSize;
    int consumed;
    final int limit;

    public BoundedSubscriber(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
                            Action onComplete, Consumer<? super Subscription> onSubscribe, int bufferSize) {
        super();
        this.onNext = onNext;
        this.onError = onError;
        this.onComplete = onComplete;
        this.onSubscribe = onSubscribe;
        this.bufferSize = bufferSize;
        this.limit = bufferSize - (bufferSize >> 2);
    }

    @Override
    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.setOnce(this, s)) {
            try {
                onSubscribe.accept(this);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                s.cancel();
                onError(e);
            }
        }
    }

    @Override
    public void onNext(T t) {
        if (!isDisposed()) {
            try {
                onNext.accept(t);

                int c = consumed + 1;
                if (c == limit) {
                    consumed = 0;
                    get().request(limit);
                } else {
                    consumed = c;
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                get().cancel();
                onError(e);
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        if (get() != SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED);
            try {
                onError.accept(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(new CompositeException(t, e));
            }
        } else {
            RxJavaPlugins.onError(t);
        }
    }

    @Override
    public void onComplete() {
        if (get() != SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED);
            try {
                onComplete.run();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        }
    }

    @Override
    public void dispose() {
        cancel();
    }

    @Override
    public boolean isDisposed() {
        return get() == SubscriptionHelper.CANCELLED;
    }

    @Override
    public void request(long n) {
        get().request(n);
    }

    @Override
    public void cancel() {
        SubscriptionHelper.cancel(this);
    }

    @Override
    public boolean hasCustomOnError() {
        return onError != Functions.ON_ERROR_MISSING;
    }
}
