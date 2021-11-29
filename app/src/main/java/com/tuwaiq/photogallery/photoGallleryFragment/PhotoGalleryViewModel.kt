package com.tuwaiq.photogallery.photoGallleryFragment

import android.app.Application
import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.*
import com.tuwaiq.photogallery.QueryPreferences
import com.tuwaiq.photogallery.flickr.models.GalleryItem
import com.tuwaiq.photogallery.flickr.repo.FlickrFetcherRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "PhotoGalleryViewModel"
class PhotoGalleryViewModel(private val app:Application) : AndroidViewModel(app) {

    private val flickrFetcherRepo = FlickrFetcherRepo()

    private val liveDataSearchTerm:MutableLiveData<String> = MutableLiveData()


    var responseLiveData:MutableLiveData<List<GalleryItem>> = MutableLiveData()

     val searchTerm: String
                get() = QueryPreferences.getStoredQuery(app)

    init {

        liveDataSearchTerm.value = searchTerm


//           Transformations.switchMap(liveDataSearchTerm) { searchTerm ->
//
//                if (searchTerm.isBlank()){
//                    flickrFetcherRepo.fetchPhotos()
//                } else{
//                    flickrFetcherRepo.searchPhotos(searchTerm)
//                }
//
//
//           }


    }

    fun fetchPhotos():LiveData<List<GalleryItem>>{
        var galleryItems:List<GalleryItem> =  emptyList()
        viewModelScope.launch(Dispatchers.IO){
           galleryItems = flickrFetcherRepo.fetchPhotos()
            Log.d(TAG , this.coroutineContext.toString())
        }.invokeOnCompletion {
            viewModelScope.launch {
                Log.d(TAG , this.coroutineContext.toString())
                responseLiveData.value = galleryItems
            }
        }




      return  responseLiveData
    }

    fun sendQueryFetchPhotos(query: String){
        QueryPreferences.setStoredQuery(app,query)
        liveDataSearchTerm.value = query

    }




}