package com.rajotiyapawan.androidpractice

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rajotiyapawan.androidpractice.background_work.WorkManagerScreen
import com.rajotiyapawan.androidpractice.base.BaseActivity
import com.rajotiyapawan.androidpractice.service.ExampleService
import com.rajotiyapawan.androidpractice.service.ForegroundExampleService
import com.rajotiyapawan.androidpractice.service.ServicePracticeScreen
import com.rajotiyapawan.androidpractice.ui.theme.AndroidPracticeTheme

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPracticeTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = navController.context
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Greeting(
                modifier = modifier,
                onNavigate = { navController.navigate(it) }
            )
        }
        composable("service") {
            ServicePracticeScreen(modifier,
                onStartServiceClick = {
                    navController.context.startService(
                        Intent(navController.context, ExampleService::class.java)
                    )
                },
                onStopServiceClick = {
                    navController.context.stopService(
                        Intent(navController.context, ExampleService::class.java)
                    )
                }, context = context,

                onStartForegroundClick = {
                    val intent = Intent(context, ForegroundExampleService::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intent)
                    } else {
                        context.startService(intent)
                    }
                },
                onStopForegroundClick = {
                    val intent = Intent(context, ForegroundExampleService::class.java)
                    context.stopService(intent)
                }
            )
        }
        composable("work_manager") {
            WorkManagerScreen(modifier)
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, onNavigate: (route:String) -> Unit) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Column {
            Button(onClick = { onNavigate("service") }) {
                Text("Go to Service Practice Screen")
            }
            Button(onClick = { onNavigate("work_manager") }) {
                Text("Go to WorkManager Screen")
            }
        }
    }
}