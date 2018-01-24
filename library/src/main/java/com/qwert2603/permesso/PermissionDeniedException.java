package com.qwert2603.permesso;

@SuppressWarnings("WeakerAccess")
public final class PermissionDeniedException extends Exception {
    public final String permission;

    public PermissionDeniedException(String permission) {
        this.permission = permission;
    }
}
