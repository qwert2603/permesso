package com.qwert2603.permesso_crtn

import android.app.Activity
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

internal class PermessoContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        val application = context!!.applicationContext as Application

        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacksAdapter() {
            override fun onActivityResumed(activity: Activity) {
                ActivityProvider.onActivityResumed(activity as AppCompatActivity)
            }

            override fun onActivityPaused(activity: Activity) {
                ActivityProvider.onActivityPaused(activity as AppCompatActivity)
            }
        })
        return true
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun query(p0: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? {
        return null
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return 0
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return 0
    }

    override fun getType(p0: Uri): String? {
        return null
    }
}