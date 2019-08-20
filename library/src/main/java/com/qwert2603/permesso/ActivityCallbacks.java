package com.qwert2603.permesso;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public interface ActivityCallbacks {
    void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
