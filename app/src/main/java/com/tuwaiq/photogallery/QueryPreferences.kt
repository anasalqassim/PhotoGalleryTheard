package com.tuwaiq.photogallery

import android.content.Context
import androidx.preference.PreferenceManager
import retrofit2.http.Query

private const val PREF_SEARCH_QUERY_KEY = "searchQuery"
private const val PREF_LAST_RESULT_ID = "lastResultId"
private const val PREF_IS_POLLING = "isPolling"
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

    fun getLastPhotoId(context: Context):String{
      val pref =  PreferenceManager.getDefaultSharedPreferences(context)

        return pref.getString(PREF_LAST_RESULT_ID,"")!!
    }

    fun setLastPhotoId(context: Context,lastPhotoId:String){
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_LAST_RESULT_ID,lastPhotoId)
            .apply()
    }

    fun isPolling(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(PREF_IS_POLLING, false)
    }
    fun setPolling(context: Context, isOn: Boolean) {

        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(PREF_IS_POLLING, isOn)
            .apply()
    }

}