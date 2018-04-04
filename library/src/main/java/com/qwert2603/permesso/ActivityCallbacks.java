package com.qwert2603.permesso;

import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.NonNull;

@SuppressWarnings("unused")
public interface ActivityCallbacks {
    @NonNull LifecycleObserver createActivityLifecycleObserver();
    void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
