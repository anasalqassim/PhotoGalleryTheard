package com.tuwaiq.photogallery.workers

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tuwaiq.photogallery.NEW_PHOTOS_NOTIFICATION_CHANNEL_ID
import com.tuwaiq.photogallery.PhotoGalleryActivity
import com.tuwaiq.photogallery.QueryPreferences
import com.tuwaiq.photogallery.R
import com.tuwaiq.photogallery.flickr.models.GalleryItem
import com.tuwaiq.photogallery.flickr.repo.FlickrFetcherRepo

private const val TAG = "PollWorker"
class PollWorker(private val context: Context, workerParameters: WorkerParameters)
    : Worker(context, workerParameters) {

    override fun doWork(): Result {

        //to check if the user have made a search before or not
        val query = QueryPreferences.getStoredQuery(context)
        val lastResultId = QueryPreferences.getLastResultId(context)

        val items: List<GalleryItem> = if (query.isEmpty()){
            FlickrFetcherRepo().fetchPhotosRequest()
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        }else{
            FlickrFetcherRepo().searchPhotosRequest(query)
                .execute()
                .body()
                ?.photos
                ?.galleryItems

        } ?: emptyList()

        if (items.isEmpty()) return Result.success()

        val resultId = items.first().id

        if (resultId == lastResultId){
            Log.d(TAG, "Got an old result")
        }else{
            Log.d(TAG, "Got new result id: $resultId")
            QueryPreferences.setLastResultId(context, lastResultId)


            //6- Notification
            val resources = context.resources

            //7- Notification
            val intent = PhotoGalleryActivity.newIntent(context)

            //8- Notification
            val pendingIntent = PendingIntent.getActivity(context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE)


            //9- Notification
            val notification = NotificationCompat.Builder(context, NEW_PHOTOS_NOTIFICATION_CHANNEL_ID)
                .setTicker(resources.getString(R.string.new_pictures_title))
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentText(resources.getString(R.string.new_pictures_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .build()

            //10- Notification
            val notificationManger = NotificationManagerCompat.from(context)
            notificationManger.notify(0, notification)
        }

        return Result.success()
    }
}