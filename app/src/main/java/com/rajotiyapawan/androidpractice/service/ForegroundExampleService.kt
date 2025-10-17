package com.rajotiyapawan.androidpractice.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class ForegroundExampleService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var isRunning = false
    private var counter = 1

    companion object {
        const val CHANNEL_ID = "foreground_service_channel"
        const val NOTIF_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.d("ForegroundService", "Service Created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ForegroundService", "Service Started")
        startForeground(NOTIF_ID, buildNotification("Service starting..."))

        if (!isRunning) {
            isRunning = true
            startLoggingLoop()
        }

        // If killed by system, restart with intent
        return START_STICKY
    }

    private fun startLoggingLoop() {
        serviceScope.launch {
            while (isRunning) {
                val message = "Foreground log #$counter"
                Log.d("ForegroundService", message)
                updateNotification(message)
                counter++
                delay(2000)
            }
        }
    }

    private fun buildNotification(content: String): Notification {
        val intent = Intent(this, com.rajotiyapawan.androidpractice.MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            else
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true) // cannot swipe away
            .build()
    }

    private fun updateNotification(content: String) {
        val notif = buildNotification(content)
        val notifManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notifManager.notify(NOTIF_ID, notif)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ForegroundService", "Service Destroyed")
        isRunning = false
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
