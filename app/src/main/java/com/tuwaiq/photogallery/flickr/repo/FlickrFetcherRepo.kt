package com.tuwaiq.photogallery.flickr.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tuwaiq.photogallery.flickr.api.FlickrApi
import com.tuwaiq.photogallery.flickr.models.FlickrResponse
import com.tuwaiq.photogallery.flickr.models.GalleryItem
import com.tuwaiq.photogallery.flickr.models.PhotoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FlickrFetcherRepo"

class FlickrFetcherRepo {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.flickr.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

   private val flickrApi = retrofit.create(FlickrApi::class.java)


    fun fetchContent():LiveData<List<GalleryItem>>{

        val responseLiveData:MutableLiveData<List<GalleryItem>> = MutableLiveData()


        val photoRequest: Call<FlickrResponse> = flickrApi.fetchPhotos()

        photoRequest.enqueue(object : Callback<FlickrResponse>{
            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>
            ) {
                val flickrResponse:FlickrResponse? = response.body()

                val photoResponse:PhotoResponse? = flickrResponse?.photos

                var galleryItems:List<GalleryItem> = photoResponse?.galleryItems ?:
                emptyList()

                galleryItems = galleryItems.filterNot { it.url.isBlank() }

                responseLiveData.value = galleryItems
            }

            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
               Log.e(TAG , "omg" , t)
            }


        })


        return responseLiveData
    }


}
