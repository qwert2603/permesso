package com.qwert2603.permesso_example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.qwert2603.permesso.Permesso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var permesso: Permesso

        fun logD(s: String) {
            Log.d("AASSDD", s)
        }

        fun logE(s: String, t: Throwable?) {
            Log.e("AASSDD", s, t)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logD("MainActivity onCreate ${hashCode()}")

        if (savedInstanceState == null) {
            permesso = Permesso.create(this.applicationContext)
        }
        lifecycle.addObserver(permesso.activityCallbacks.createActivityLifecycleObserver())

        askPermission_Button.setOnClickListener { SomeRepo.doSmth() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        logD("MainActivity onRequestPermissionsResult ${hashCode()}")
        permesso.activityCallbacks.onPermissionResult(requestCode, permissions, grantResults)
    }
}
