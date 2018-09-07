package com.example.thiagoevoa.estudoandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.AsyncTask
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.model.Client
import com.example.thiagoevoa.estudoandroid.util.CONTENT_TYPE
import com.example.thiagoevoa.estudoandroid.util.HOST
import com.example.thiagoevoa.estudoandroid.util.URL_CLIENT
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

class ClientViewModel : ViewModel() {
    var clientsLiveData = MutableLiveData<MutableList<Client>>()
    var clientLiveData = MutableLiveData<Client>()
    private var client: Client? = null
    private var swipe: SwipeRefreshLayout? = null
    private var progress: ProgressBar? = null
    private var txtMessage: TextView? = null
    private var response: Response? = null
    private var responseCode: Int? = null
    private var responseMessage: MutableList<String> = mutableListOf()
    private var context: Context? = null

    inner class ListAllClientAsyncTask(var view: View?) : AsyncTask<String, Void, MutableList<Client>>() {
        override fun onPreExecute() {
            super.onPreExecute()
            txtMessage = view?.findViewById(R.id.txt_message)
            swipe = view?.findViewById(R.id.swipe_client)
            swipe?.isRefreshing = true
        }

        override fun doInBackground(vararg params: String?): MutableList<Client> {
            var clients = mutableListOf<Client>()
            try {
                response = if (params.isEmpty()) {
                    OkHttpClient().newCall(
                            Request.Builder().url("$HOST$URL_CLIENT").build()).execute()
                } else {
                    OkHttpClient().newCall(
                            Request.Builder().url("$HOST$URL_CLIENT/cpf/${params[0]}").build()).execute()
                }


                val jsonString = response?.body()?.string()
                val jsonArray = JSONArray(jsonString)

                for (i in 0 until jsonArray.length()) {
                    var client = Gson().fromJson(jsonArray[i].toString(), Client::class.java)
                    clients.add(client)
                }
            } catch (ex: Exception) {
                Log.e("Error: ", ex.message)
            }
            return clients
        }

        override fun onPostExecute(result: MutableList<Client>?) {
            super.onPostExecute(result)
            clientsLiveData.value = result

            if (clientsLiveData.value?.size == 0) {
                txtMessage?.text = "There is no client with those data"
                txtMessage?.visibility = View.VISIBLE
            } else {
                txtMessage?.visibility = View.GONE
            }
            swipe?.isRefreshing = false
        }
    }

    inner class SaveClientAsyncTask(var view: View?) : AsyncTask<Void, Void, Int>() {
        override fun onPreExecute() {
            super.onPreExecute()
            context = view?.context
            progress = view?.findViewById(R.id.pb_client)
            progress?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): Int? {
            try {
                response = if (clientLiveData?.value?._id != null) {
                    OkHttpClient().newCall(
                            Request.Builder()
                                    .url(HOST + URL_CLIENT)
                                    .put(RequestBody.create(CONTENT_TYPE, Gson().toJson(clientLiveData.value)))
                                    .build())
                            .execute()
                } else {
                    OkHttpClient().newCall(
                            Request.Builder()
                                    .url(HOST + URL_CLIENT)
                                    .post(RequestBody.create(CONTENT_TYPE, Gson().toJson(clientLiveData.value)))
                                    .build())
                            .execute()
                }
                responseCode = response?.code()
                if (responseCode == 200) {
                    client = Gson().fromJson(response?.body()?.string(), Client::class.java)
                }
            } catch (ex: Exception) {
                Log.e("Error: ", ex.message)
            }
            return responseCode
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            if (responseCode == 200) {
                Toast.makeText(view?.context, "Saved successfully", Toast.LENGTH_LONG).show()
                ListAllClientAsyncTask(view).execute()
            } else {
                responseMessage = JSONObject(response?.body()?.string()).getString("error").split(",") as MutableList<String>
                for (message: String in responseMessage) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
            progress?.visibility = View.INVISIBLE
        }
    }

    inner class DeleteClientAsyncTask(var view: View?, var menu: Menu?) : AsyncTask<Void, Void, Int>() {
        override fun onPreExecute() {
            super.onPreExecute()
            context = view?.context
            swipe = view?.findViewById(R.id.swipe_schedule)
            swipe?.isRefreshing = true
            menu?.findItem(R.id.action_delete)?.isEnabled = false
        }

        override fun doInBackground(vararg params: Void?): Int? {
            try {
                response = OkHttpClient().newCall(
                        Request.Builder()
                                .url(HOST + URL_CLIENT)
                                .delete(RequestBody.create(CONTENT_TYPE, Gson().toJson(clientLiveData.value)))
                                .build())
                        .execute()
                responseCode = response?.code()
                if (responseCode == 200) {
                    client = Gson().fromJson(JSONObject(response?.body()?.string()).toString(), Client::class.java)
                }
            } catch (ex: Exception) {
                Log.e("Error: ", ex.message)
            }
            return responseCode
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            if (responseCode == 200) {
                Toast.makeText(view?.context, "Deleted successfully", Toast.LENGTH_LONG).show()
                ListAllClientAsyncTask(view).execute()
            } else {
                responseMessage = JSONObject(response?.body()?.string()).getString("error").split(",") as MutableList<String>
                for (message: String in responseMessage) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
            progress?.visibility = View.INVISIBLE
            menu?.findItem(R.id.action_delete)?.isVisible = false
            menu?.findItem(R.id.action_search)?.isVisible = true
        }
    }
}