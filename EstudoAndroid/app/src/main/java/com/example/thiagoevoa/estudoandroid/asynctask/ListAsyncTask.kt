package com.example.thiagoevoa.estudoandroid.asynctask

import android.os.AsyncTask
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request

class ListAsyncTask(private val url: String) : AsyncTask<String, Void, String>() {
    private var result: String? = null
    override fun doInBackground(vararg params: String?): String? {
        try {
            val response = if (params.isEmpty()) {
                OkHttpClient().newCall(
                        Request.Builder().url(url).build()).execute()
            } else {
                OkHttpClient().newCall(
                        Request.Builder().url("/$url/${params[0]}").build()).execute()
            }

            result = response?.body()?.string()
        } catch (ex: Exception) {
            Log.e("Error: ", ex.message)
        }
        return result
    }
}