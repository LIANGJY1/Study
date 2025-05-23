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

package com.liang.rxjava3.disposables;

import com.liang.rxjava3.annotations.NonNull;
import com.liang.rxjava3.functions.Action;
import com.liang.rxjava3.internal.util.ExceptionHelper;

/**
 * A Disposable container that manages an {@link Action} instance.
 */
final class ActionDisposable extends ReferenceDisposable<Action> {

    private static final long serialVersionUID = -8219729196779211169L;

    ActionDisposable(Action value) {
        super(value);
    }

    @Override
    protected void onDisposed(@NonNull Action value) {
        try {
            value.run();
        } catch (Throwable ex) {
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @Override
    public String toString() {
        return "ActionDisposable(disposed=" + isDisposed() + ", " + get() + ")";
    }
}
