package com.qwert2603.permesso;

import android.support.v7.app.AppCompatActivity;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

@SuppressWarnings("WeakerAccess")
final class ActivityProvider {

    private final BehaviorSubject<Wrapper<AppCompatActivity>> activityChanges = BehaviorSubject.createDefault(new Wrapper<AppCompatActivity>(null));

    Single<AppCompatActivity> resumedActivity() {
        return activityChanges
                .filter(new Predicate<Wrapper<AppCompatActivity>>() {
                    @Override
                    public boolean test(Wrapper<AppCompatActivity> appCompatActivityWrapper) throws Exception {
                        return appCompatActivityWrapper.value != null;
                    }
                })
                .map(new Function<Wrapper<AppCompatActivity>, AppCompatActivity>() {
                    @Override
                    public AppCompatActivity apply(Wrapper<AppCompatActivity> appCompatActivityWrapper) throws Exception {
                        return appCompatActivityWrapper.value;
                    }
                })
                .firstOrError();
    }

    void onActivityResumed(AppCompatActivity appCompatActivity) {
        activityChanges.onNext(new Wrapper<>(appCompatActivity));
    }

    void onActivityPaused(@SuppressWarnings("unused") AppCompatActivity appCompatActivity) {
        activityChanges.onNext(new Wrapper<AppCompatActivity>(null));
    }
}
