package com.qwert2603.permesso.internal;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public final class ActivityProviderLifecycle implements LifecycleObserver {

    @NonNull
    private final ActivityProvider activityProvider;

    public ActivityProviderLifecycle(@NonNull ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @SuppressWarnings("unused")
    void onResume(LifecycleOwner lifecycleOwner) {
        activityProvider.onActivityResumed((AppCompatActivity) lifecycleOwner);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    @SuppressWarnings("unused")
    void onPause(LifecycleOwner lifecycleOwner) {
        activityProvider.onActivityPaused((AppCompatActivity) lifecycleOwner);
    }
}
