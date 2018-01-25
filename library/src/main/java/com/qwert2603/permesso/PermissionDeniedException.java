package com.qwert2603.permesso;

import android.support.annotation.NonNull;

@SuppressWarnings("WeakerAccess")
public final class PermissionDeniedException extends Exception {
    @NonNull
    @SuppressWarnings("unused")
    public final String permission;

    public PermissionDeniedException(@NonNull String permission) {
        this.permission = permission;
    }
}
