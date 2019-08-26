package com.qwert2603.permesso_coroutines

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking

internal object ActivityProvider {
    data class Wrapper<T>(val value: T?)

    val activityChanges = ConflatedBroadcastChannel<Wrapper<AppCompatActivity>>()

    fun onActivityResumed(activity: AppCompatActivity) {
        activityChanges.sendBlocking(Wrapper(activity))
    }

    fun onActivityPaused(activity: AppCompatActivity) {
        activityChanges.sendBlocking(Wrapper<AppCompatActivity>(null))
    }
}