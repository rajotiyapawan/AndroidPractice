package com.rajotiyapawan.androidpractice.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Created by Pawan Rajotiya on 16-10-2025.
 */

interface AnalyticsLogger{
    fun registerLifecycleOwner(owner: LifecycleOwner)
}

class AnalyticsLoggerImpl: AnalyticsLogger, LifecycleEventObserver{
    override fun registerLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        val ownerType = when (source) {
            is androidx.activity.ComponentActivity -> "Activity"
            is androidx.fragment.app.Fragment -> "Fragment"
            else -> "ViewLifecycleOwner"
        }

        val name = source::class.simpleName ?: "Unknown"
        when(event){
            Lifecycle.Event.ON_CREATE -> println("Logger: $ownerType $name → onCreate() called.")
            Lifecycle.Event.ON_RESUME -> println("Logger: $ownerType $name → onResume() called.")
            Lifecycle.Event.ON_PAUSE -> println("Logger: $ownerType $name → onPause() called.")
            Lifecycle.Event.ON_DESTROY -> println("Logger: $ownerType $name → onDestroy() called.")
            else -> Unit
        }
    }

}