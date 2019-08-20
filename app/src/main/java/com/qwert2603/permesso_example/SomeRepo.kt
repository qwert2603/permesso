package com.qwert2603.permesso_example

object SomeRepo {
    fun doSmth() {
        MainActivity.permesso.permissionRequester
                .requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { s: String?, t: Throwable? ->
                    s?.let { MainActivity.logD("SomeRepo $it") }
                    t?.let { MainActivity.logE("SomeRepo error! $it", it) }
                }
    }
}