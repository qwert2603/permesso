package com.qwert2603.permesso;

import androidx.lifecycle.LifecycleObserver;
import android.content.Context;
import androidx.annotation.NonNull;

import com.qwert2603.permesso.internal.ActivityProvider;
import com.qwert2603.permesso.internal.ActivityProviderLifecycle;
import com.qwert2603.permesso.internal.PermissionHelper;

import io.reactivex.Single;

@SuppressWarnings("unused")
public final class Permesso {

    private final PermissionRequester permissionRequester;
    private final ActivityCallbacks activityCallbacks;

    private Permesso(final PermissionHelper permissionHelper, final ActivityProvider activityProvider) {
        permissionRequester = new PermissionRequester() {
            @NonNull
            @Override
            public Single<String> requestPermission(@NonNull String permission) {
                return permissionHelper.requestPermission(permission);
            }
        };
        activityCallbacks = new ActivityCallbacks() {
            @NonNull
            @Override
            public LifecycleObserver createActivityLifecycleObserver() {
                return new ActivityProviderLifecycle(activityProvider);
            }

            @Override
            public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                permissionHelper.onPermissionResult(requestCode, permissions, grantResults);
            }
        };
    }

    @NonNull
    public static Permesso create(@NonNull Context appContext) {
        ActivityProvider activityProvider = new ActivityProvider();
        return new Permesso(
                new PermissionHelper(appContext.getApplicationContext(), activityProvider),
                activityProvider
        );
    }

    @NonNull
    public PermissionRequester getPermissionRequester() {
        return permissionRequester;
    }

    @NonNull
    public ActivityCallbacks getActivityCallbacks() {
        return activityCallbacks;
    }
}
