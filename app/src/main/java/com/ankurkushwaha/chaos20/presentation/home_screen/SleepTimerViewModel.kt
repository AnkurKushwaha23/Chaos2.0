package com.ankurkushwaha.chaos20.presentation.home_screen

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ankurkushwaha.chaos20.worker.SleepTimerWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SleepTimerViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val workManager: WorkManager
) : ViewModel() {

    // Sleep timer state
    private val _timerEnabled = MutableStateFlow(false)
    val timerEnabled: StateFlow<Boolean> = _timerEnabled

    private val _timerMinutes = MutableStateFlow(23L)
    val timerMinutes: StateFlow<Long> = _timerMinutes

    private val _showSleepDialog = MutableStateFlow(false)
    val showSleepDialog: StateFlow<Boolean> = _showSleepDialog

    // LiveData observer reference for proper cleanup
    private var workInfoObserver: Observer<List<WorkInfo>>? = null

    init {
        // Check if timer is already running when ViewModel is created
        checkIfWorkerIsRunning()
    }

    // Show sleep timer dialog
    fun showSleepDialog() {
        _showSleepDialog.value = true
    }

    // Hide sleep timer dialog
    fun hideSleepDialog() {
        _showSleepDialog.value = false
    }

    // Check if sleep timer worker is running
    private fun checkIfWorkerIsRunning() {
        workInfoObserver = Observer { workInfoList ->
            if (workInfoList != null && workInfoList.isNotEmpty()) {
                val isRunning = workInfoList.any { workInfo ->
                    workInfo.state == WorkInfo.State.RUNNING || workInfo.state == WorkInfo.State.ENQUEUED
                }
                // Update the state if the worker is running
                _timerEnabled.value = isRunning
            }
        }
        workManager.getWorkInfosByTagLiveData("SleepTimerWorkerTag")
            .observeForever(workInfoObserver!!)
    }

    // Cancel any previously running SleepTimerWorker
    fun cancelExistingWorker() {
        workManager.cancelAllWorkByTag("SleepTimerWorkerTag")
        _timerEnabled.value = false
    }

    // Initialize the Sleep Timer Worker
    fun initSleepTimerWorker(minutes: Long) {
        val stopMusicRequest =
            OneTimeWorkRequestBuilder<SleepTimerWorker>() // Added worker class type
                .setInitialDelay(minutes, TimeUnit.MINUTES) // Sleep timer duration
                .addTag("SleepTimerWorkerTag") // Add a tag to manage this worker
                .build()

        // Enqueue the work
        workManager.enqueue(stopMusicRequest)
        _timerEnabled.value = true
        _timerMinutes.value = minutes

        // Show toast
        Toast.makeText(appContext, "Sleep Timer set for $minutes minutes", Toast.LENGTH_SHORT)
            .show()
    }

    // Handle dialog confirmation
    fun onSleepTimerConfirm(enabled: Boolean, minutes: Long) {
        _timerEnabled.value = enabled
        _timerMinutes.value = minutes
        hideSleepDialog()

        // Sleep timer functionality
        if (enabled) {
            // Start the timer for the given minutes
            cancelExistingWorker() // Cancel any existing timer first
            initSleepTimerWorker(minutes) // Start a new timer
        } else {
            // Cancel any existing timer
            cancelExistingWorker()
            Toast.makeText(appContext, "Sleep Timer cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    // Clean up resources when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        // Remove the observer to prevent memory leaks
        workInfoObserver?.let {
            workManager.getWorkInfosByTagLiveData("SleepTimerWorkerTag").removeObserver(it)
        }
        workInfoObserver = null
    }
}