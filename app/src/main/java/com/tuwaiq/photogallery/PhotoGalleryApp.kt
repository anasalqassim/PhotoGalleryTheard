package com.tuwaiq.photogallery

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

const val CHANNEL_NOTIFICATION_ID = "HELLO WOLRD"
class PhotoGalleryApp :Application() {

    override fun onCreate() {
        super.onCreate()


        val channelName = resources.getString(R.string.notification_channel_name)

        val channelImportance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_NOTIFICATION_ID,channelName,
        channelImportance)


        val notificationManager =
            getSystemService(NotificationManager::class.java)

        notificationManager.createNotificationChannel(channel)
    }
}