package com.qwert2603.permesso.internal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.qwert2603.permesso.exception.PermissionCancelledException;
import com.qwert2603.permesso.exception.PermissionDeniedException;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public final class PermissionHelper {

    private final int MIN_REQUEST_CODE = 1000;
    private final int MAX_REQUEST_CODE = 1999;

    private final Context appContext;
    private final ActivityProvider activityProvider;

    private int lastRequestCode = MIN_REQUEST_CODE;

    @SuppressLint("UseSparseArrays")
    private final Map<Integer, SingleEmitter<String>> emitters = new HashMap<>();

    public PermissionHelper(@NonNull Context appContext, @NonNull ActivityProvider activityProvider) {
        this.appContext = appContext;
        this.activityProvider = activityProvider;
    }

    @NonNull
    public Single<String> requestPermission(@NonNull final String permission) {
        if (ContextCompat.checkSelfPermission(appContext, permission) != PackageManager.PERMISSION_GRANTED) {
            return activityProvider.resumedActivity()
                    .flatMap(new Function<Activity, SingleSource<? extends String>>() {
                        @Override
                        public SingleSource<? extends String> apply(final Activity activity) {
                            return Single.create(new SingleOnSubscribe<String>() {
                                @Override
                                public void subscribe(SingleEmitter<String> emitter) {
                                    emitters.put(lastRequestCode, emitter);
                                    ActivityCompat.requestPermissions(activity, new String[]{permission}, lastRequestCode);
                                    lastRequestCode = getNextRequestCode(lastRequestCode);
                                }
                            });
                        }
                    });
        } else {
            return Single.just(permission);
        }
    }

    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
