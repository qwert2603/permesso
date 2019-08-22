package com.qwert2603.permesso;

import androidx.annotation.NonNull;

import com.qwert2603.permesso.internal.PermissionHelper;

import io.reactivex.Single;

@SuppressWarnings("unused")
public final class Permesso {

    private final PermissionRequester permissionRequester;

    private Permesso(final PermissionHelper permissionHelper) {
        permissionRequester = new PermissionRequester() {
            @NonNull
            @Override
            public Single<String> requestPermission(@NonNull String permission) {
                return permissionHelper.requestPermission(permission);
            }
        };
    }

    @NonNull
    public static Permesso create() {
        return new Permesso(PermissionHelper.INSTANCE);
    }

    @NonNull
    public PermissionRequester getPermissionRequester() {
        return permissionRequester;
    }
}
