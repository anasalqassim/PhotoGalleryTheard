package com.tuwaiq.photogallery.photoGallleryFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuwaiq.photogallery.R
import com.tuwaiq.photogallery.flickr.models.GalleryItem

private const val TAG = "PhotoGalleryFragment"
class PhotoGalleryFragment : Fragment() {

    private lateinit var photoGalleryRV:RecyclerView

    private val viewModel by lazy { ViewModelProvider(this)[PhotoGalleryViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.responseLiveData.observe(
            this, Observer {
                Log.d(TAG , it.toString())
            }
        )


        val livedata = MutableLiveData<String>()

        livedata.value  ="adhfvajshv"


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.photo_gallery_fragment, container, false)
        photoGalleryRV = view.findViewById(R.id.photoGallery_rv)
        photoGalleryRV.layoutManager = GridLayoutManager(context,3)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private inner class GalleryHolder(val view: View):
            RecyclerView.ViewHolder(view){
                private val photoItem:ImageView = itemView.findViewById(R.id.photo_item)

           fun bind(galleryItem: GalleryItem){

           }


            }
    private inner class GalleryAdapter(val galleryItems:List<GalleryItem>):
            RecyclerView.Adapter<GalleryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
            val view = layoutInflater.inflate(
                R.layout.photo_list_item,
                parent,
                false
            )
            return GalleryHolder(view)
        }

        override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
            val galleryItem = galleryItems[position]
            holder.bind(galleryItem)
        }

        override fun getItemCount(): Int = galleryItems.size
    }
}