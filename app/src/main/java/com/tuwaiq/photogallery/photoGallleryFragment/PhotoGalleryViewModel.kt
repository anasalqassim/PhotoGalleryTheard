package com.tuwaiq.photogallery.photoGallleryFragment

import android.app.Application
import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.*
import com.tuwaiq.photogallery.QueryPreferences
import com.tuwaiq.photogallery.flickr.models.GalleryItem
import com.tuwaiq.photogallery.flickr.repo.FlickrFetcherRepo


class PhotoGalleryViewModel(private val app:Application) : AndroidViewModel(app) {


    private val flickrFetcherRepo = FlickrFetcherRepo()

    private val liveDataSearchTerm:MutableLiveData<String> = MutableLiveData()

    var responseLiveData:LiveData<List<GalleryItem>>

     val searchTerm: String
                get() = QueryPreferences.getStoredQuery(app)

    init {

        liveDataSearchTerm.value = searchTerm

       responseLiveData =
           Transformations.switchMap(liveDataSearchTerm) { searchTerm ->

                if (searchTerm.isBlank()){
                    flickrFetcherRepo.fetchPhotos()
                } else{
                    flickrFetcherRepo.searchPhotos(searchTerm)
                }


           }


    }

    fun sendQueryFetchPhotos(query: String){
        QueryPreferences.setStoredQuery(app,query)
        liveDataSearchTerm.value = query

    }




}