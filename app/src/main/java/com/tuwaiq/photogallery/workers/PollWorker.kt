package com.tuwaiq.photogallery.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tuwaiq.photogallery.QueryPreferences
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
        }

        return Result.success()
    }
}