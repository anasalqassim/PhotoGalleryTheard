package com.tuwaiq.photogallery.workers

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tuwaiq.photogallery.*
import com.tuwaiq.photogallery.flickr.models.GalleryItem
import com.tuwaiq.photogallery.flickr.repo.FlickrFetcherRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

private const val TAG = "PollWorker"
class PollWorker(private val context: Context, workerPrams:WorkerParameters)
    : Worker(context , workerPrams) {
    override fun doWork(): Result {
//
//        val query = QueryPreferences.getStoredQuery(context)
//        val lastResultId = QueryPreferences.getLastResultId(context)
//
//        val items:List<GalleryItem> = if (query.isEmpty()){
//            FlickrFetcherRepo()
//                .fetchPhotosRequest().execute()
//                .body()
//                ?.photos
//                ?.galleryItems
//        }else{
//
//            CoroutineScope(Dispatchers.IO).launch {
//                async {
//                    FlickrFetcherRepo().searchPhotosRequest(query)
//                        .awaitResponse()
//                        .body()
//                        ?.photos
//                        ?.galleryItems
//                }
//
//            }
//
//
//
//
//        } ?: emptyList()
//
//        if (items.isEmpty()){
//            return Result.success()
//        }
//
//        val resultId = items.first().id
//
//        if (resultId == lastResultId){
//            Log.d(TAG, "GOT AN OLD RESULTID = $resultId")
//        }else {
//            Log.d(TAG, "got new result id = $resultId")
//            QueryPreferences.setLastResultId(context, resultId)
//
//            val resources = context.resources
//
//
//            val intent = PhotoGalleryActivity.newIntent(context)
//            val pendingIntent = PendingIntent.getActivity(
//                context, 0,
//                intent, PendingIntent.FLAG_IMMUTABLE
//            )
//
//
//            val notification = NotificationCompat.Builder(
//                context, CHANNEL_NOTIFICATION_ID
//            ).setTicker(resources.getString(R.string.new_pictures_title))
//                .setSmallIcon(R.drawable.ic_android_black_24dp)
//                .setContentTitle(resources.getString(R.string.new_pictures_title))
//                .setContentText(resources.getString(R.string.new_pictures_text))
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .build()
//
////
//            showBackgroundNotification(notification)
//
//        }
//
//
//
//
//
//
        return Result.success()

    }

    fun showBackgroundNotification(notification: Notification){
        val intent = Intent(ACTION_SHOW_NOTIFICATION).apply {
            putExtra(NOTIFICATIN , notification)
        }

        context.sendOrderedBroadcast(intent , PERM_PRIVATE)
    }

    companion object{
        const val ACTION_SHOW_NOTIFICATION = "com.tuwaiq.photoGallery.SHOW_NOTIFICATION"
        const val PERM_PRIVATE = "com.tuwaiq.photogallery.PRIVATE"

        const val NOTIFICATIN = "notify"
    }


}