package com.example.thiagoevoa.estudoandroid.asynctask

import android.os.AsyncTask
import android.util.Log
import com.example.thiagoevoa.estudoandroid.util.RESPONSE_OK
import okhttp3.OkHttpClient
import okhttp3.Request

class ListAsyncTask(private val url: String) : AsyncTask<String, Void, Void>() {
    companion object {
        var result: String? = null
    }

    override fun doInBackground(vararg params: String?): Void? {
        try {
            val response = if (params.isEmpty()) {
                OkHttpClient().newCall(
                        Request.Builder().url(url).build()).execute()
            } else {
                OkHttpClient().newCall(
                        Request.Builder().url("/$url/${params[0]}").build()).execute()
            }

            if(response.code() == RESPONSE_OK){
                result = response.body()?.string()
            }
        } catch (ex: Exception) {
            Log.e("Error: ", ex.message)
        }
        return null
    }
}