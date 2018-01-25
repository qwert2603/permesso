package com.qwert2603.permesso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

final class PermissionHelper {

    private final int MIN_REQUEST_CODE = 1000;
    private final int MAX_REQUEST_CODE = 1999;

    private final Context appContext;
    private final ActivityProvider activityProvider;

    private int lastRequestCode = MIN_REQUEST_CODE;

    @SuppressLint("UseSparseArrays")
    private final Map<Integer, SingleEmitter<String>> emitters = new HashMap<>();

    PermissionHelper(Context appContext, ActivityProvider activityProvider) {
        this.appContext = appContext;
        this.activityProvider = activityProvider;
    }

    Single<String> requestPermission(final String permission) {
        if (ContextCompat.checkSelfPermission(appContext, permission) != PackageManager.PERMISSION_GRANTED) {
            return activityProvider.resumedActivity()
                    .flatMap(new Function<AppCompatActivity, SingleSource<? extends String>>() {
                        @Override
                        public SingleSource<? extends String> apply(final AppCompatActivity appCompatActivity) throws Exception {
                            return Single.create(new SingleOnSubscribe<String>() {
                                @Override
                                public void subscribe(SingleEmitter<String> emitter) throws Exception {
                                    emitters.put(lastRequestCode, emitter);
                                    ActivityCompat.requestPermissions(appCompatActivity, new String[]{permission}, lastRequestCode);
                                    lastRequestCode = getNextRequestCode(lastRequestCode);
                                }
                            });
                        }
                    });
        } else {
            return Single.just(permission);
        }
    }

    void onPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        SingleEmitter<String> emitter = emitters.remove(requestCode);
        if (emitter == null || emitter.isDisposed()) return;
        if (permissions.length == 0 || grantResults.length == 0) {
            emitter.onError(new PermissionCancelledException());
        } else {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                emitter.onSuccess(permissions[0]);
            } else {
                emitter.onError(new PermissionDeniedException(permissions[0]));
            }
        }
    }

    private int getNextRequestCode(int currentRequestCode) {
        return currentRequestCode == MAX_REQUEST_CODE ? MIN_REQUEST_CODE : currentRequestCode + 1;
    }
}
