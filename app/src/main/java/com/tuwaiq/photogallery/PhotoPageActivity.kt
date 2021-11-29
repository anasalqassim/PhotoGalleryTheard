package com.tuwaiq.photogallery

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import com.tuwaiq.photogallery.photoGallleryFragment.PHOTO_LINK_KEY

class PhotoPageActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_page)

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar3)
        progressBar.max = 100


        val fullPhotoLink = intent.getStringExtra(PHOTO_LINK_KEY)
            ?: "https://www.flickr.com"

        webView.webChromeClient = object : WebChromeClient(){

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                this@PhotoPageActivity.supportActionBar?.title = title
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100){
                    progressBar.visibility = View.GONE
                }else{
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                }
            }

        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(fullPhotoLink)

    }
}