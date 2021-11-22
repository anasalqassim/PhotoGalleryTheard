package com.tuwaiq.photogallery.photoGallleryFragment

import android.app.Application
import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.*
import com.tuwaiq.photogallery.QueryPreferences
import com.tuwaiq.photogallery.flickr.models.GalleryItem
import com.tuwaiq.photogallery.flickr.repo.FlickrFetcherRepo


class PhotoGalleryViewModel() : ViewModel() {


    private val flickrFetcherRepo = FlickrFetcherRepo()

    private val liveDataSearchTerm:MutableLiveData<String> = MutableLiveData()

    val responseLiveData:LiveData<List<GalleryItem>>


    init {

        responseLiveData =
            Transformations.switchMap(liveDataSearchTerm){ searchTerm->
                flickrFetcherRepo.searchPhotos(searchTerm)
            }


    }

    fun sendQueryFetchPhotos(query: String){
        liveDataSearchTerm.value = query

    }




}