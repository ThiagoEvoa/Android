package com.example.thiagoevoa.estudoandroid.asynctask

import android.os.AsyncTask
import android.util.Log
import com.example.thiagoevoa.estudoandroid.util.CONTENT_TYPE
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class UpdateAsyncTask(private val url: String, private val value: String) : AsyncTask<Void, Void, Int>() {
    override fun doInBackground(vararg params: Void?): Int {
        var response: Response? = null
        try {
            response = OkHttpClient().newCall(Request.Builder()
                    .url(url)
                    .put(RequestBody.create(CONTENT_TYPE, value))
                    .build())
                    .execute()
        } catch (ex: Exception) {
            Log.e("Error: ", ex.message)
        }
        return response!!.code()
    }
}