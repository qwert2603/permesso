package com.qwert2603.permesso;

import android.support.annotation.NonNull;

import io.reactivex.Single;

@SuppressWarnings("unused")
public interface PermissionRequester {
    @NonNull Single<String> requestPermission(@NonNull final String permission);
}
