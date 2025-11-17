package com.rajotiyapawan.androidpractice.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

open class ExampleBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Log.d("BroadcastReceiver", "Received broadcast: $action")

        when (action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                Log.d("BroadcastReceiver", "Airplane mode changed")
            }
            Intent.ACTION_BATTERY_LOW -> {
                Log.d("BroadcastReceiver", "Battery is low!")
            }
            "com.rajotiyapawan.CUSTOM_ACTION" -> {
                Log.d("BroadcastReceiver", "Received custom broadcast!")
            }
        }
    }
}
