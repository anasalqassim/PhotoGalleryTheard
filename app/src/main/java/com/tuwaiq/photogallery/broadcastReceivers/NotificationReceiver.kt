package com.tuwaiq.photogallery.broadcastReceivers

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.tuwaiq.photogallery.workers.PollWorker

private const val TAG = "NotificationRecevier"
class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG , "received broadcast ${intent?.action}")


        if (resultCode != Activity.RESULT_OK ){
            return
        }

        val notification:Notification = intent?.getParcelableExtra(
            PollWorker.NOTIFICATIN
        )!!

        val notificationManager = NotificationManagerCompat
            .from(context!!)

        notificationManager.notify(0,notification)



    }
}