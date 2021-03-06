package com.qwert2603.permesso_coroutines

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull

//fun logD(s: String) {
//    Log.d("AASSDD", s)
//}
//
//fun logE(s: String, t: Throwable?) {
//    Log.e("AASSDD", s, t)
//}

object PermessoCoroutines {

    private const val MIN_REQUEST_CODE = 1000
    private const val MAX_REQUEST_CODE = 1999

    private var lastRequestCode = MIN_REQUEST_CODE

    private val chs = mutableMapOf<Int, SendChannel<String>>()

    suspend fun requestPermission(permission: String): String {
        val activity = ActivityProvider.activityChanges
                .asFlow()
                .mapNotNull { it.value }
                .first()

        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            return permission
        } else {
            val channel = Channel<String>()

            val requestCode = lastRequestCode
            lastRequestCode = getNextRequestCode(lastRequestCode)

            chs[requestCode] = channel

            val permessoFragment = PermessoFragment()
            activity.supportFragmentManager
                    .beginTransaction()
                    .add(permessoFragment, "permessoFragment")
                    .runOnCommit { permessoFragment.requestPermissions(arrayOf(permission), requestCode) }
                    .commitAllowingStateLoss()

            return channel.receive()
        }
    }

    internal fun onPermissionResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val channel = chs.remove(requestCode) ?: return
        if (permissions.isEmpty() || grantResults.isEmpty()) {
            channel.close(PermissionCancelledException())
        } else {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                channel.sendBlocking(permissions[0])
                channel.close()
            } else {
                channel.close(PermissionDeniedException(permissions[0]))
            }
        }
    }

    private fun getNextRequestCode(currentRequestCode: Int): Int =
            when (currentRequestCode) {
                MAX_REQUEST_CODE -> MIN_REQUEST_CODE
                else -> currentRequestCode + 1
            }
}