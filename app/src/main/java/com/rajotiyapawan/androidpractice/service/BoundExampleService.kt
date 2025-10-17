package com.rajotiyapawan.androidpractice.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BoundExampleService : Service() {

    private val binder = LocalBinder()
    /** You can use other dispatchers (IO, Default, Main, or even a custom one) in a Service.
     * 👉 The right choice depends on the kind of work your Service is doing.
     *
     * A Service is a background component, not tied to the UI — so you never want to block its main thread.
     * That means:
     *
     * ✅ Use Default or IO dispatchers
     *
     * ❌ Avoid Main (no Compose, no Views inside Service)
     */
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var isLogging = false

    // Mutable flow for sending updates
    private val _logFlow = MutableStateFlow("Service initialized")
    val logFlow = _logFlow.asStateFlow()

    // Binder for clients
    inner class LocalBinder : Binder() {
        fun getService(): BoundExampleService = this@BoundExampleService
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("BoundService", "Service Bound")
        startLoggingLoop()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("BoundService", "Service Unbound — stopping logs")
        stopLoggingLoop()
        return super.onUnbind(intent)
    }

    private fun startLoggingLoop() {
        if (isLogging) return
        isLogging = true

        serviceScope.launch {
            var counter = 1
            while (isLogging) {
                Log.d("BoundService", "Bound logging message #$counter")
                counter++
                delay(2000)
            }
        }
    }


    private fun stopLoggingLoop() {
        isLogging = false
        serviceScope.coroutineContext.cancelChildren()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BoundService", "Service Destroyed")
        serviceScope.cancel()
    }
}
