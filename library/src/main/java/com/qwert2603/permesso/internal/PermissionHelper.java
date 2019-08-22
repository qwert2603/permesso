package com.qwert2603.permesso.internal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

    private static final int MIN_REQUEST_CODE = 1000;
    private static final int MAX_REQUEST_CODE = 1999;

    private final ActivityProvider activityProvider;

    private int lastRequestCode = MIN_REQUEST_CODE;

    public static final PermissionHelper INSTANCE = new PermissionHelper(ActivityProvider.INSTANCE);

    @SuppressLint("UseSparseArrays")
    private final Map<Integer, SingleEmitter<String>> emitters = new HashMap<>();

    private PermissionHelper(@NonNull ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
    }

    @NonNull
    public Single<String> requestPermission(@NonNull final String permission) {
        return activityProvider.resumedActivity()
                .flatMap(new Function<AppCompatActivity, SingleSource<? extends String>>() {
                    @Override
                    public SingleSource<? extends String> apply(final AppCompatActivity activity) {
                        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                            return Single.just(permission);
                        }
                        return Single.create(new SingleOnSubscribe<String>() {
                            @Override
                            public void subscribe(SingleEmitter<String> emitter) {
                                emitters.put(lastRequestCode, emitter);
                                final PermessoFragment permessoFragment = new PermessoFragment();
                                activity.getSupportFragmentManager()
                                        .beginTransaction()
                                        .add(permessoFragment, "permessoFragment")
                                        .commitNow();
                                permessoFragment.requestPermissions(new String[]{permission}, lastRequestCode);
                                lastRequestCode = getNextRequestCode(lastRequestCode);
                            }
                        });
                    }
                });
    }

    private void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    public static class PermessoFragment extends Fragment {
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            PermissionHelper.INSTANCE.onPermissionResult(requestCode, permissions, grantResults);
            requireFragmentManager().beginTransaction()
                    .remove(this)
                    .commitNow();
        }
    }
}
