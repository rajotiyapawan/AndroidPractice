package com.rajotiyapawan.androidpractice.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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

@Composable
fun ServicePracticeScreen(modifier: Modifier,
    onStartServiceClick: () -> Unit,
    onStopServiceClick: () -> Unit,
    context: Context,
    onStartForegroundClick: () -> Unit,
    onStopForegroundClick: () -> Unit
) {
    var isBound by remember { mutableStateOf(false) }

    // Connection to the bound service
    val connection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as BoundExampleService.LocalBinder
                val myService = binder.getService()
                isBound = true
                // We could call functions on service here
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isBound = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            context.unbindService(connection)
        }
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Android Services Practice",style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(20.dp))
        // --- Started Service ---
        Text("Started Service", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onStartServiceClick) { Text("Start StartedService") }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = onStopServiceClick) { Text("Stop StartedService") }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Bound Service ---
        Text("Bound Service", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            val intent = Intent(context, BoundExampleService::class.java)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        },
            enabled = !isBound) { Text("Bind Service") }

        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                if (isBound) {
                    context.unbindService(connection)
                    isBound = false
                }
            },
            enabled = isBound
        ) {
            Text("Unbind Service")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Foreground Service ---
        Text("Foreground Service", style = MaterialTheme.typography.titleMedium)
        Button(onClick = onStartForegroundClick) { Text("Start Foreground Service") }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = onStopForegroundClick) { Text("Stop Foreground Service") }

    }
}
