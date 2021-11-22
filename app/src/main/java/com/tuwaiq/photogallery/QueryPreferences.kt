package com.tuwaiq.photogallery

import android.content.Context
import androidx.preference.PreferenceManager
import retrofit2.http.Query

private const val PREF_SEARCH_QUERY_KEY = "searchQuery"
object QueryPreferences {

    fun getStoredQuery(context: Context):String{

        val pref = PreferenceManager.getDefaultSharedPreferences(context)

      return  pref.getString(PREF_SEARCH_QUERY_KEY,"")!!
    }

    fun setStoredQuery(context: Context,query: String){
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_QUERY_KEY,query)
            .apply()
    }


}