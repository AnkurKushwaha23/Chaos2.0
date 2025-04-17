package com.ankurkushwaha.chaos20.worker

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class SleepTimerWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Create an intent to broadcast
        val intent = Intent("com.ankurkushwaha.chaos20.STOP_MUSIC_SERVICE")

        // Send the broadcast to be received by dynamically registered receivers
        applicationContext.sendBroadcast(intent)

        // Return success
        return Result.success()
    }

}