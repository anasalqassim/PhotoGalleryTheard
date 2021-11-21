package com.tuwaiq.photogallery.flickr.api

import com.tuwaiq.photogallery.flickr.models.FlickrResponse
import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {

    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=61c5832ff895fb51a162051c51f28a33" +
            "&extras=url_s" +
            "&format=json" +
            "&nojsoncallback=1")
    fun fetchPhotos():Call<FlickrResponse>
}