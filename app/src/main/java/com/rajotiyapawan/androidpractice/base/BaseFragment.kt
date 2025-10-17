package com.rajotiyapawan.androidpractice.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Created by Pawan Rajotiya on 17-10-2025.
 */

abstract class BaseFragment :
    Fragment(),
    AnalyticsLogger by AnalyticsLoggerImpl() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLifecycleOwner(this) // Fragment lifecycle
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Register for the view lifecycle when available
        viewLifecycleOwnerLiveData.observe(viewLifecycleOwner) { owner ->
            registerLifecycleOwner(owner)
        }
    }
}