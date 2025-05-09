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

package com.liang.rxjava3.observers;

import java.util.concurrent.atomic.AtomicReference;

import com.liang.rxjava3.annotations.NonNull;
import com.liang.rxjava3.core.SingleObserver;
import com.liang.rxjava3.disposables.Disposable;
import com.liang.rxjava3.internal.disposables.DisposableHelper;
import com.liang.rxjava3.internal.util.EndConsumerHelper;

/**
 * An abstract {@link SingleObserver} that allows asynchronous cancellation by implementing {@link Disposable}.
 *
 * <p>All pre-implemented final methods are thread-safe.
 *
 * <p>Like all other consumers, {@code DisposableSingleObserver} can be subscribed only once.
 * Any subsequent attempt to subscribe it to a new source will yield an
 * {@link IllegalStateException} with message {@code "It is not allowed to subscribe with a(n) <class name> multiple times."}.
 *
 * <p>Implementation of {@link #onStart()}, {@link #onSuccess(Object)} and {@link #onError(Throwable)}
 * are not allowed to throw any unchecked exceptions.
 *
 * <p>Example<pre><code>
 * Disposable d =
 *     Single.just(1).delay(1, TimeUnit.SECONDS)
 *     .subscribeWith(new DisposableSingleObserver&lt;Integer&gt;() {
 *         &#64;Override public void onStart() {
 *             System.out.println("Start!");
 *         }
 *         &#64;Override public void onSuccess(Integer t) {
 *             System.out.println(t);
 *         }
 *         &#64;Override public void onError(Throwable t) {
 *             t.printStackTrace();
 *         }
 *     });
 * // ...
 * d.dispose();
 * </code></pre>
 *
 * @param <T> the received value type
 */
public abstract class DisposableSingleObserver<T> implements SingleObserver<T>, Disposable {

    final AtomicReference<Disposable> upstream = new AtomicReference<>();

    @Override
    public final void onSubscribe(@NonNull Disposable d) {
        if (EndConsumerHelper.setOnce(this.upstream, d, getClass())) {
            onStart();
        }
    }

    /**
     * Called once the single upstream {@link Disposable} is set via {@link #onSubscribe(Disposable)}.
     */
    protected void onStart() {
    }

    @Override
    public final boolean isDisposed() {
        return upstream.get() == DisposableHelper.DISPOSED;
    }

    @Override
    public final void dispose() {
        DisposableHelper.dispose(upstream);
    }
}
