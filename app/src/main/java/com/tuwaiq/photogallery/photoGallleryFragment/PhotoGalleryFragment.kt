package com.tuwaiq.photogallery.photoGallleryFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tuwaiq.photogallery.R
import com.tuwaiq.photogallery.flickr.models.GalleryItem

private const val TAG = "PhotoGalleryFragment"
class PhotoGalleryFragment : Fragment() {

    private lateinit var photoGalleryRV:RecyclerView
    private lateinit var progressBar: ProgressBar

    private val viewModel by lazy { ViewModelProvider(this)[PhotoGalleryViewModel::class.java] }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.photo_gallery_fragment,menu)

        val searchItem =menu.findItem(R.id.app_bar_search)
        val searchView  = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        viewModel.sendQueryFetchPhotos(query)
                        progressBar.visibility = View.VISIBLE
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.responseLiveData.observe(
            this, {galleryItems->
                updateUI(galleryItems)
                progressBar.visibility = View.GONE
            }
        )




    }
    private fun updateUI(galleryItems: List<GalleryItem>){
        photoGalleryRV.adapter = GalleryAdapter(galleryItems)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.photo_gallery_fragment, container, false)
        photoGalleryRV = view.findViewById(R.id.photoGallery_rv)
        photoGalleryRV.layoutManager = GridLayoutManager(context,3)
        progressBar = view.findViewById(R.id.progressBar)
        return view
    }

    private inner class GalleryHolder( view: View):
            RecyclerView.ViewHolder(view){
                private val photoItem:ImageView = itemView.findViewById(R.id.photo_item)

           fun bind(galleryItem: GalleryItem){
                photoItem.load(galleryItem.url){
                    placeholder(R.drawable.ic_android_black_24dp)
                    crossfade(250)


                }
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