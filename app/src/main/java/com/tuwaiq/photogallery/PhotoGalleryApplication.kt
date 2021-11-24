package com.tuwaiq.photogallery

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.content.getSystemService

const val NEW_PHOTOS_NOTIFICATION_CHANNEL_ID = "new photos channel"

class PhotoGalleryApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        //1- Notification
        val channelName = resources.getString(R.string.notification_channel_name)

        //2- Notification
        val channelImportance = NotificationManager.IMPORTANCE_DEFAULT

        //3- Notification
        val channel = NotificationChannel(NEW_PHOTOS_NOTIFICATION_CHANNEL_ID, channelName, channelImportance)

        //4- Notification
        val notificationManager = getSystemService(NotificationManager::class.java)

        //5- Notification
        notificationManager.createNotificationChannel(channel)
    }
}