package com.tuwaiq.photogallery.photoGallleryFragment

import androidx.lifecycle.ViewModel
import com.tuwaiq.photogallery.flickr.repo.FlickrFetcherRepo

class PhotoGalleryViewModel : ViewModel() {


    private val flickrFetcherRepo = FlickrFetcherRepo()
    val responseLiveData = flickrFetcherRepo.fetchContent()

}