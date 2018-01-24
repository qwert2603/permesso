package com.qwert2603.permesso;

import android.support.annotation.Nullable;

final class Wrapper<T> {
    @Nullable final T value;

    Wrapper(@Nullable T value) {
        this.value = value;
    }
}
