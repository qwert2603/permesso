package com.qwert2603.permesso;

import androidx.annotation.NonNull;

import com.qwert2603.permesso.internal.PermissionHelper;

import io.reactivex.Single;

@SuppressWarnings("unused")
public final class Permesso {

    @NonNull
    public static Permesso create() {
        return new Permesso();
    }

    @NonNull public Single<String> requestPermission(@NonNull final String permission) {
        return PermissionHelper.INSTANCE.requestPermission(permission);
    }
}
