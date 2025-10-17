package com.rajotiyapawan.androidpractice.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity

/**
 * Created by Pawan Rajotiya on 16-10-2025.
 */
abstract class BaseActivity :
    ComponentActivity(),
    AnalyticsLogger by AnalyticsLoggerImpl() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLifecycleOwner(this)
    }
}
