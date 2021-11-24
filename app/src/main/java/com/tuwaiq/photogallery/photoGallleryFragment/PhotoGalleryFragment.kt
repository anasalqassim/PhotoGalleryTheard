package com.tuwaiq.photogallery.photoGallleryFragment

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import coil.load
import com.tuwaiq.photogallery.QueryPreferences
import com.tuwaiq.photogallery.R
import com.tuwaiq.photogallery.flickr.models.GalleryItem
import com.tuwaiq.photogallery.workers.PollWorker
import java.util.concurrent.TimeUnit


private const val TAG = "PhotoGalleryFragment"
private const val POLL_WORK = "POLL_WORK"
class PhotoGalleryFragment : Fragment() {

    private lateinit var photoGalleryRV:RecyclerView
    private lateinit var progressBar: ProgressBar

    private val viewModel by lazy { ViewModelProvider(this)[PhotoGalleryViewModel::class.java] }


    val onShowNotification = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG , "hi im awake")
            resultCode = Activity.RESULT_CANCELED
        }

    }

    override fun onStart() {
        super.onStart()

        IntentFilter(PollWorker.ACTION_SHOW_NOTIFICATION).also {
            requireContext().registerReceiver(onShowNotification
                ,it,
                PollWorker.PERM_PRIVATE,
                null
            )
        }

    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(onShowNotification)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.photo_gallery_fragment,menu)

        val toggleItem = menu.findItem(R.id.menu_item_toggle_polling)

        val isPolling = QueryPreferences.isPolling(requireContext())

        val toggleItemTitle = if (isPolling) {
            R.string.stop_polling
        } else {
            R.string.start_polling
        }
        toggleItem.setTitle(toggleItemTitle)

        toggleItem.setOnMenuItemClickListener {

                startNotificationWorker(isPolling)

                activity?.invalidateOptionsMenu()

                true
        }


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

            setOnSearchClickListener {
                searchView.setQuery(viewModel.searchTerm,false)
            }
        }

    }

    private fun startNotificationWorker(isPolling:Boolean) {

        if (isPolling){
            WorkManager.getInstance(requireContext()).cancelUniqueWork(POLL_WORK)
            QueryPreferences.setPolling(requireContext(),false)
        }else{
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequest
                .Builder(PollWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(requireContext())
                .enqueueUniquePeriodicWork(
                    POLL_WORK,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
            QueryPreferences.setPolling(requireContext(),true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.clear_search -> {
                viewModel.sendQueryFetchPhotos("")
                true
            }
            else -> super.onOptionsItemSelected(item)


        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        startNotificationWorker(QueryPreferences.isPolling(requireContext()))


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