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

package com.liang.rxjava3.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;

import com.liang.rxjava3.core.*;
import com.liang.rxjava3.disposables.Disposable;
import com.liang.rxjava3.functions.Consumer;
import com.liang.rxjava3.observables.ConnectableObservable;

/**
 * Wraps a ConnectableObservable and calls its connect() method once
 * the specified number of Observers have subscribed.
 *
 * @param <T> the value type of the chain
 */
public final class ObservableAutoConnect<T> extends Observable<T> {
    final ConnectableObservable<? extends T> source;
    final int numberOfObservers;
    final Consumer<? super Disposable> connection;
    final AtomicInteger clients;

    public ObservableAutoConnect(ConnectableObservable<? extends T> source,
            int numberOfObservers,
            Consumer<? super Disposable> connection) {
        this.source = source;
        this.numberOfObservers = numberOfObservers;
        this.connection = connection;
        this.clients = new AtomicInteger();
    }

    @Override
    public void subscribeActual(Observer<? super T> child) {
        source.subscribe(child);
        if (clients.incrementAndGet() == numberOfObservers) {
            source.connect(connection);
        }
    }
}
