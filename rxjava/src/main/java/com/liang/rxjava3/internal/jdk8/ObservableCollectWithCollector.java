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

import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

import com.liang.rxjava3.annotations.NonNull;
import com.liang.rxjava3.core.*;
import com.liang.rxjava3.disposables.Disposable;
import com.liang.rxjava3.exceptions.Exceptions;
import com.liang.rxjava3.internal.disposables.*;
import com.liang.rxjava3.internal.observers.DeferredScalarDisposable;
import com.liang.rxjava3.plugins.RxJavaPlugins;

/**
 * Collect items into a container defined by a Stream {@link Collector} callback set.
 *
 * @param <T> the upstream value type
 * @param <A> the intermediate accumulator type
 * @param <R> the result type
 * @since 3.0.0
 */
public final class ObservableCollectWithCollector<T, A, R> extends Observable<R> {

    final Observable<T> source;

    final Collector<? super T, A, R> collector;

    public ObservableCollectWithCollector(Observable<T> source, Collector<? super T, A, R> collector) {
        this.source = source;
        this.collector = collector;
    }

    @Override
    protected void subscribeActual(@NonNull Observer<? super R> observer) {
        A container;
        BiConsumer<A, ? super T> accumulator;
        Function<A, R> finisher;

        try {
            container = collector.supplier().get();
            accumulator = collector.accumulator();
            finisher = collector.finisher();
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptyDisposable.error(ex, observer);
            return;
        }

        source.subscribe(new CollectorObserver<>(observer, container, accumulator, finisher));
    }

    static final class CollectorObserver<T, A, R>
    extends DeferredScalarDisposable<R>
    implements Observer<T> {

        private static final long serialVersionUID = -229544830565448758L;

        final BiConsumer<A, T> accumulator;

        final Function<A, R> finisher;

        Disposable upstream;

        boolean done;

        A container;

        CollectorObserver(Observer<? super R> downstream, A container, BiConsumer<A, T> accumulator, Function<A, R> finisher) {
            super(downstream);
            this.container = container;
            this.accumulator = accumulator;
            this.finisher = finisher;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            if (DisposableHelper.validate(this.upstream, d)) {
                this.upstream = d;

                downstream.onSubscribe(this);
            }
        }

        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
            try {
                accumulator.accept(container, t);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                upstream.dispose();
                onError(ex);
            }
        }

        @Override
        public void onError(Throwable t) {
            if (done) {
                RxJavaPlugins.onError(t);
            } else {
                done = true;
                upstream = DisposableHelper.DISPOSED;
                this.container = null;
                downstream.onError(t);
            }
        }

        @Override
        public void onComplete() {
            if (done) {
                return;
            }

            done = true;
            upstream = DisposableHelper.DISPOSED;
            A container = this.container;
            this.container = null;
            R result;
            try {
                result = Objects.requireNonNull(finisher.apply(container), "The finisher returned a null value");
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                downstream.onError(ex);
                return;
            }

            complete(result);
        }

        @Override
        public void dispose() {
            super.dispose();
            upstream.dispose();
        }
    }
}
