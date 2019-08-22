package com.qwert2603.permesso.internal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

final class ActivityProvider {

    private static final class Wrapper<T> {
        @Nullable
        final T value;

        Wrapper(@Nullable T value) {
            this.value = value;
        }
    }

    static final ActivityProvider INSTANCE = new ActivityProvider();

    private final BehaviorSubject<Wrapper<AppCompatActivity>> activityChanges = BehaviorSubject.createDefault(new Wrapper<AppCompatActivity>(null));

    private ActivityProvider() {
    }

    @NonNull
    Single<AppCompatActivity> resumedActivity() {
        return activityChanges
                .filter(new Predicate<Wrapper<AppCompatActivity>>() {
                    @Override
                    public boolean test(Wrapper<AppCompatActivity> activityWrapper) {
                        return activityWrapper.value != null;
                    }
                })
                .map(new Function<Wrapper<AppCompatActivity>, AppCompatActivity>() {
                    @Override
                    public AppCompatActivity apply(Wrapper<AppCompatActivity> activityWrapper) {
                        return activityWrapper.value;
                    }
                })
                .firstOrError();
    }

    void onActivityResumed(AppCompatActivity activity) {
        activityChanges.onNext(new Wrapper<>(activity));
    }

    void onActivityPaused(@SuppressWarnings("unused") AppCompatActivity activity) {
        activityChanges.onNext(new Wrapper<AppCompatActivity>(null));
    }
}
