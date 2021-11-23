package com.tuwaiq.photogallery.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG = "TestWorker"
class TestWorker(val context: Context,workerPrams:WorkerParameters) : Worker(context,workerPrams) {
    override fun doWork(): Result {
        Log.d(TAG, "hello from the test worker")


        return Result.success()

    }
}