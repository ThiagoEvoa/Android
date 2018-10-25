package com.example.thiagoevoa.estudoandroid.asynctask

import android.os.AsyncTask
import android.util.Log
import com.example.thiagoevoa.estudoandroid.util.CONTENT_TYPE_JSON
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class DeleteAsyncTask(private var url: String, private val value: String) : AsyncTask<Void, Void, Int>() {
    override fun doInBackground(vararg params: Void?): Int? {
        var response: Response? = null
        try {
            response = OkHttpClient().newCall(Request.Builder()
                            .url(url)
                            .delete(RequestBody.create(CONTENT_TYPE_JSON, value))
                            .build())
                    .execute()
        } catch (ex: Exception) {
            Log.e("Error: ", ex.message)
        }
        return response?.code()
    }
}
