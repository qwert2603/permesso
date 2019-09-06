package com.qwert2603.permesso_coroutines

import androidx.fragment.app.Fragment

internal class PermessoFragment : Fragment() {
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermessoCoroutines.onPermissionResult(requestCode, permissions, grantResults)
        requireFragmentManager().beginTransaction()
                .remove(this)
                .commitAllowingStateLoss()
    }
}