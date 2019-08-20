package com.qwert2603.permesso.internal;

import android.app.Activity;

import androidx.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

public final class ActivityProvider {

    public static final ActivityProvider INSTANCE = new ActivityProvider();

    private final BehaviorSubject<Wrapper<Activity>> activityChanges = BehaviorSubject.createDefault(new Wrapper<Activity>(null));

    private ActivityProvider() {
    }

    @NonNull
    Single<Activity> resumedActivity() {
        return activityChanges
                .filter(new Predicate<Wrapper<Activity>>() {
                    @Override
                    public boolean test(Wrapper<Activity> activityWrapper) {
                        return activityWrapper.value != null;
                    }
                })
                .map(new Function<Wrapper<Activity>, Activity>() {
                    @Override
                    public Activity apply(Wrapper<Activity> activityWrapper) {
                        return activityWrapper.value;
                    }
                })
                .firstOrError();
    }

    void onActivityResumed(Activity activity) {
        activityChanges.onNext(new Wrapper<>(activity));
    }

    void onActivityPaused(@SuppressWarnings("unused") Activity activity) {
        activityChanges.onNext(new Wrapper<Activity>(null));
    }
}
