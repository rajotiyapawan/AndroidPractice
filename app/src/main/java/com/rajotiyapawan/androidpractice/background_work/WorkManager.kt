package com.rajotiyapawan.androidpractice.background_work

/**
 * Created by Pawan Rajotiya on 17-11-2025.
 */

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.rajotiyapawan.androidpractice.R
import kotlinx.coroutines.delay

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val CHANNEL_ID = "work_manager_channel"
        const val NOTIFICATION_ID = 1
        const val KEY_TITLE = "notification_title"
        const val KEY_MESSAGE = "notification_message"
    }

    override suspend fun doWork(): Result {
        // Simulate some work
        delay(5000) // 5 seconds delay to simulate work

        // Get input data
        val title = inputData.getString(KEY_TITLE) ?: "WorkManager Notification"
        val message = inputData.getString(KEY_MESSAGE) ?: "Your background work is completed!"

        // Create and show notification
        showNotification(title, message)

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background) // Create this drawable
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "WorkManager Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications from WorkManager background work"
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

class ProgressNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val CHANNEL_ID = "progress_work_channel"
        const val NOTIFICATION_ID = 2
    }

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo("Starting work...", 0))

        for (i in 1..100) {
            // Update progress for observation
            setProgress(workDataOf("progress" to i, "max" to 100))

            // Update notification
            setForeground(createForegroundInfo("Processing item $i of 100", i))

            delay(1000) // Simulate work
        }

        showCompletionNotification()
        return Result.success()
    }

    private fun createForegroundInfo(contentText: String, progress: Int): ForegroundInfo {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Background Work in Progress")
            .setContentText(contentText)
            .setProgress(100, progress, false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification)
    }

    private fun showCompletionNotification() {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Work Completed!")
            .setContentText("Background work finished successfully")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID + 1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Progress Work Notifications",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifications showing work progress"
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

