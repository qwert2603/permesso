package com.qwert2603.permesso;

import androidx.annotation.NonNull;

import io.reactivex.Single;

@SuppressWarnings("unused")
public interface PermissionRequester {
    @NonNull Single<String> requestPermission(@NonNull final String permission);
}
