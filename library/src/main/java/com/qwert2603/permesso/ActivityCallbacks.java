package com.qwert2603.permesso;

import androidx.lifecycle.LifecycleObserver;
import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public interface ActivityCallbacks {
    @NonNull LifecycleObserver createActivityLifecycleObserver();
    void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
