package com.tuwaiq.photogallery.workers

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tuwaiq.photogallery.NOTIFICATION_CHANNEL_ID
import com.tuwaiq.photogallery.PhotoGalleryActivity
import com.tuwaiq.photogallery.QueryPreferences
import com.tuwaiq.photogallery.R
import com.tuwaiq.photogallery.flickr.models.GalleryItem
import com.tuwaiq.photogallery.flickr.repo.FlickrFetcherRepo

private const val TAG = "PollWorker"

class PollWorker(private val context: Context, workerParameters: WorkerParameters):Worker(context,workerParameters) {
    override fun doWork(): Result {


        val query = QueryPreferences.getStoredQuery(context)
        val lastResultId = QueryPreferences.getLastPhotoId(context)

        val items:List<GalleryItem> = if (query.isEmpty()){
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


        if (items.isEmpty()){
            return Result.success()
        }

        val resultId = items.first().id
        if (resultId == lastResultId) {
            Log.d(TAG, "Got an old result: $resultId")
        } else {
            Log.i(TAG, "Got a new result: $resultId")
            QueryPreferences.setLastPhotoId(context, resultId)


            val intent = PhotoGalleryActivity.newIntent(context)
            val pendingIntent = PendingIntent.getActivity(context,0,intent, FLAG_IMMUTABLE)

          val resources =   context.resources

            val notification = NotificationCompat
                .Builder(context,NOTIFICATION_CHANNEL_ID)
                    //not required
                .setTicker(resources.getString(R.string.new_pictures_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setContentText(resources.getString(R.string.new_pictures_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(0, notification)
        }

       return Result.success()
    }

}