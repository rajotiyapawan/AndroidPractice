package com.rajotiyapawan.androidpractice.background_work

/**
 * Created by Pawan Rajotiya on 17-11-2025.
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class WorkManagerViewModel(application: Application) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)

    private val _uiState = MutableStateFlow<WorkManagerUiState>(WorkManagerUiState.Idle)
    val uiState: StateFlow<WorkManagerUiState> = _uiState.asStateFlow()

    private val _workProgress = MutableStateFlow<WorkProgress?>(null)
    val workProgress: StateFlow<WorkProgress?> = _workProgress.asStateFlow()

    init {
        observeWorkStatus()
    }

    fun scheduleOneTimeWork() {
        viewModelScope.launch {
            try {
                _uiState.value = WorkManagerUiState.Loading("Scheduling one-time work...")

                val inputData = workDataOf(
                    NotificationWorker.KEY_TITLE to "Task Completed!",
                    NotificationWorker.KEY_MESSAGE to "Your one-time background work has finished successfully."
                )

                val constraints = Constraints.Builder()
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
                    .setInputData(inputData)
                    .setConstraints(constraints)
                    .addTag("one_time_work")
                    .build()

                workManager.enqueue(notificationWork)
                _uiState.value = WorkManagerUiState.Success("One-time work scheduled!")

            } catch (e: Exception) {
                _uiState.value = WorkManagerUiState.Error("Failed to schedule work: ${e.message}")
            }
        }
    }

    fun startProgressWork() {
        viewModelScope.launch {
            try {
                _uiState.value = WorkManagerUiState.Loading("Starting progress work...")

                val progressWork = OneTimeWorkRequestBuilder<ProgressNotificationWorker>()
                    .addTag("progress_work")
                    .build()

                workManager.enqueue(progressWork)
                _uiState.value = WorkManagerUiState.Success("Progress work started!")

            } catch (e: Exception) {
                _uiState.value = WorkManagerUiState.Error("Failed to start progress work: ${e.message}")
            }
        }
    }

    fun scheduleDelayedWork() {
        viewModelScope.launch {
            try {
                _uiState.value = WorkManagerUiState.Loading("Scheduling delayed work...")

                val inputData = workDataOf(
                    NotificationWorker.KEY_TITLE to "Delayed Work Done!",
                    NotificationWorker.KEY_MESSAGE to "Your delayed work executed after 1 minute."
                )

                val delayedWork = OneTimeWorkRequestBuilder<NotificationWorker>()
                    .setInputData(inputData)
                    .setInitialDelay(1, TimeUnit.MINUTES)
                    .addTag("delayed_work")
                    .build()

                workManager.enqueue(delayedWork)
                _uiState.value = WorkManagerUiState.Success("Delayed work scheduled (1 minute)!")

            } catch (e: Exception) {
                _uiState.value = WorkManagerUiState.Error("Failed to schedule delayed work: ${e.message}")
            }
        }
    }

    fun cancelAllWork() {
        viewModelScope.launch {
            try {
                workManager.cancelAllWork()
                _uiState.value = WorkManagerUiState.Success("All work cancelled!")
                _workProgress.value = null
            } catch (e: Exception) {
                _uiState.value = WorkManagerUiState.Error("Failed to cancel work: ${e.message}")
            }
        }
    }

    private fun observeWorkStatus() {
        viewModelScope.launch {
            workManager.getWorkInfosByTagLiveData("progress_work").observeForever { workInfos ->
                workInfos?.let { infos ->
                    if (infos.isNotEmpty()) {
                        val workInfo = infos[0]
                        when (workInfo.state) {
                            WorkInfo.State.RUNNING -> {
                                val progress = workInfo.progress.getInt("progress", 0)
                                val max = workInfo.progress.getInt("max", 100)
                                _workProgress.value = WorkProgress(progress, max)
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                _workProgress.value = null
                            }
                            WorkInfo.State.FAILED, WorkInfo.State.CANCELLED -> {
                                _workProgress.value = null
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

sealed class WorkManagerUiState {
    object Idle : WorkManagerUiState()
    data class Loading(val message: String) : WorkManagerUiState()
    data class Success(val message: String) : WorkManagerUiState()
    data class Error(val message: String) : WorkManagerUiState()
}

data class WorkProgress(
    val current: Int,
    val max: Int
) {
    val progress: Float = if (max > 0) current.toFloat() / max else 0f
    val percentage: Int = (progress * 100).toInt()
}