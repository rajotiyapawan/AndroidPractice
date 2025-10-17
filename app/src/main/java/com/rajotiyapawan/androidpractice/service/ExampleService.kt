package com.rajotiyapawan.androidpractice.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExampleService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        Log.d("ExampleService", "Service Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ExampleService", "Service Started")
        // do background work here
        if (!isRunning) {
            isRunning = true
            startLoggingLoop()
        }
        return START_STICKY
    }

    private fun startLoggingLoop() {
        serviceScope.launch {
            var counter = 1
            while (isRunning) {
                Log.d("ExampleService", "Logging message #$counter")
                counter++
                delay(2000) // Log every 2 seconds
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ExampleService", "Service Destroyed — stopping logs")
        isRunning = false
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
