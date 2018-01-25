package com.qwert2603.permesso;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.support.annotation.NonNull;

import io.reactivex.Single;

@SuppressWarnings("unused")
public final class Permesso {

    private final PermissionHelper permissionHelper;
    private final ActivityProvider activityProvider;

    private Permesso(PermissionHelper permissionHelper, ActivityProvider activityProvider) {
        this.permissionHelper = permissionHelper;
        this.activityProvider = activityProvider;
    }

    @NonNull
    public static Permesso create(@NonNull Context appContext) {
        ActivityProvider activityProvider = new ActivityProvider();
        return new Permesso(
                new PermissionHelper(appContext, activityProvider),
                activityProvider
        );
    }

    @NonNull
    public Single<String> requestPermission(@NonNull final String permission) {
        return permissionHelper.requestPermission(permission);
    }

    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onPermissionResult(requestCode, permissions, grantResults);
    }

    @NonNull
    public LifecycleObserver createLifecycleObserver() {
        return new ActivityProviderLifecycle(activityProvider);
    }
}
