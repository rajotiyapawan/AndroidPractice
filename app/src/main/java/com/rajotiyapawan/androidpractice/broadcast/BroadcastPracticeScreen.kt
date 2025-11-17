package com.rajotiyapawan.androidpractice.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun BroadcastPracticeScreen() {
    val context = LocalContext.current
    var message by remember { mutableStateOf("Waiting for broadcasts...") }

    // Create custom receiver
    val receiver = remember {
        object : ExampleBroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                super.onReceive(context, intent)
                message = "Received: ${intent?.action}"
            }
        }
    }
    // Create a basic receiver
    val receiver1 = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                message = "Received: ${intent?.action}"
            }
        }
    }


    // Register / Unregister dynamically
    DisposableEffect(Unit) {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction("com.rajotiyapawan.CUSTOM_ACTION")
        }

        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    // UI
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, modifier = Modifier.padding(16.dp))

        Button(onClick = {
            val intent = Intent("com.rajotiyapawan.CUSTOM_ACTION")
            intent.setPackage(context.packageName)
            context.sendBroadcast(intent)
        }) {
            Text("Send Custom Broadcast")
        }
    }
}
