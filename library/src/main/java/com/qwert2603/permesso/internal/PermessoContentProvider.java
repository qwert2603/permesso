package com.qwert2603.permesso.internal;

import android.app.Activity;
import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

public final class PermessoContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        final Application application = (Application) getContext().getApplicationContext();
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {
            @Override
            public void onActivityResumed(Activity activity) {
                ActivityProvider.INSTANCE.onActivityResumed(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                ActivityProvider.INSTANCE.onActivityPaused(activity);
            }
        });
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
