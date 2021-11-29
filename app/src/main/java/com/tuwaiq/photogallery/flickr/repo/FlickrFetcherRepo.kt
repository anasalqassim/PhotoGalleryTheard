package com.tuwaiq.photogallery.flickr.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tuwaiq.photogallery.flickr.api.FlickrApi
import com.tuwaiq.photogallery.flickr.api.PhotoInterceptor
import com.tuwaiq.photogallery.flickr.models.FlickrResponse
import com.tuwaiq.photogallery.flickr.models.GalleryItem
import com.tuwaiq.photogallery.flickr.models.PhotoResponse
import com.tuwaiq.photogallery.photoGallleryFragment.PhotoGalleryViewModel
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

private const val TAG = "FlickrFetcherRepo"

class FlickrFetcherRepo {

    private val client:OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(PhotoInterceptor())
        .build()


    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.flickr.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val flickrApi = retrofit.create(FlickrApi::class.java)

    fun fetchPhotosRequest():Call<FlickrResponse> = flickrApi.fetchPhotos()

  suspend  fun fetchPhotos():List<GalleryItem>{

     // var galleryItems:List<GalleryItem> = flickrApi.fetchPhotos().await().photos.galleryItems

      var galleryItems:List<GalleryItem> = emptyList()
      val response = flickrApi.fetchPhotos().awaitResponse()
      if (response.isSuccessful){
          galleryItems = response.body()!!.photos.galleryItems
      }else{
          Log.e(TAG,  "something gone wrong ${response.errorBody()}", )
      }




      return galleryItems
  }


    fun searchPhotosRequest(query: String):Call<FlickrResponse> = flickrApi.searchPhotos(query)

     fun searchPhotos(query: String):LiveData<List<GalleryItem>> {

        return fetchPhotosMetadata(flickrApi.searchPhotos(query))
    }



    private fun fetchPhotosMetadata(photoRequest:Call<FlickrResponse>):LiveData<List<GalleryItem>>{

        val responseLiveData:MutableLiveData<List<GalleryItem>> = MutableLiveData()

      //  val photoRequest:  = flickrApi.fetchPhotos()

//        photoRequest.enqueue(object : Callback<FlickrResponse>{
//            override fun onResponse(
//                call: Call<FlickrResponse>,
//                response: Response<FlickrResponse>
//            ) {
//                val flickrResponse:FlickrResponse? = response.body()
//
//                val photoResponse:PhotoResponse? = flickrResponse?.photos
//
//                var galleryItems:List<GalleryItem> = photoResponse?.galleryItems ?:
//                emptyList()
//
//                galleryItems = galleryItems.filterNot { it.url.isBlank() }
//
//                responseLiveData.value = galleryItems
//            }
//
//            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
//               Log.e(TAG , "omg" , t)
//            }
//
//
//        })



        return responseLiveData
    }


}
