package com.qwert2603.permesso;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

final class ActivityProviderLifecycle implements LifecycleObserver {

    @NonNull
    private final ActivityProvider activityProvider;

    ActivityProviderLifecycle(@NonNull ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume(LifecycleOwner lifecycleOwner) {
        activityProvider.onActivityResumed((AppCompatActivity) lifecycleOwner);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause(LifecycleOwner lifecycleOwner) {
        activityProvider.onActivityPaused((AppCompatActivity) lifecycleOwner);
    }
}
