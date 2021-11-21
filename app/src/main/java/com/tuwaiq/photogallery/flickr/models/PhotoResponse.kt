package com.tuwaiq.photogallery.flickr.models

import com.google.gson.annotations.SerializedName

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems:List<GalleryItem>

}