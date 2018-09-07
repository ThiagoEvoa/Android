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
import com.example.thiagoevoa.estudoandroid.model.Professional
import com.example.thiagoevoa.estudoandroid.util.CONTENT_TYPE
import com.example.thiagoevoa.estudoandroid.util.HOST
import com.example.thiagoevoa.estudoandroid.util.URL_PROFESSIONAL
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

class ProfessionalViewModel : ViewModel() {
    var professionalsLiveData = MutableLiveData<MutableList<Professional>>()
    var professionalLiveData = MutableLiveData<Professional>()
    private var professional: Professional? = null
    private var swipe: SwipeRefreshLayout? = null
    private var progress: ProgressBar? = null
    private var txtMessage: TextView? = null
    private var response: Response? = null
    private var responseCode: Int? = null
    private var responseMessage: MutableList<String> = mutableListOf()
    private var context: Context? = null

    inner class ListAllProfessionalsAsyncTask(var view: View?) : AsyncTask<String, Void, MutableList<Professional>>() {
        override fun onPreExecute() {
            super.onPreExecute()
            txtMessage = view?.findViewById(R.id.txt_message)
            swipe = view?.findViewById(R.id.swipe_professional)
            swipe?.isRefreshing = true
        }

        override fun doInBackground(vararg params: String?): MutableList<Professional> {
            var professionals = mutableListOf<Professional>()
            try {
                response = if (params.isEmpty()) {
                    OkHttpClient().newCall(
                            Request.Builder().url("$HOST$URL_PROFESSIONAL").build()).execute()
                } else {
                    OkHttpClient().newCall(
                            Request.Builder().url("$HOST$URL_PROFESSIONAL/cpf_cnpj/${params[0]}").build()).execute()
                }

                val jsonString = response?.body()?.string()
                val jsonArray = JSONArray(jsonString)

                for (i in 0 until jsonArray.length()) {
                    var professional = Gson().fromJson(jsonArray[i].toString(), Professional::class.java)
                    professionals.add(professional)
                }
            } catch (ex: Exception) {
                Log.e("Error: ", ex.message)
            }
            return professionals
        }

        override fun onPostExecute(result: MutableList<Professional>?) {
            professionalsLiveData.value = result

            if (professionalsLiveData.value?.size == 0) {
                txtMessage?.text = "There is no professional with those data"
                txtMessage?.visibility = View.VISIBLE
            } else {
                txtMessage?.visibility = View.GONE
            }
            swipe?.isRefreshing = false
        }
    }

    inner class SaveProfessionalAsyncTask(var view: View?) : AsyncTask<Void, Void, Int>() {
        override fun onPreExecute() {
            super.onPreExecute()
            context = view?.context
            progress = view?.findViewById(R.id.pb_professional)
            progress?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): Int? {
            try {
                response = if (professionalLiveData?.value?._id != null) {
                    OkHttpClient().newCall(
                            Request.Builder()
                                    .url(HOST + URL_PROFESSIONAL)
                                    .put(RequestBody.create(CONTENT_TYPE, Gson().toJson(professionalLiveData.value)))
                                    .build())
                            .execute()
                } else {
                    OkHttpClient().newCall(
                            Request.Builder()
                                    .url(HOST + URL_PROFESSIONAL)
                                    .post(RequestBody.create(CONTENT_TYPE, Gson().toJson(professionalLiveData.value)))
                                    .build())
                            .execute()
                }
                responseCode = response?.code()
                if (responseCode == 200) {
                    professional = Gson().fromJson(response?.body()?.string(), Professional::class.java)
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
                ListAllProfessionalsAsyncTask(view).execute()
            } else {
                responseMessage = JSONObject(response?.body()?.string()).getString("error").split(",") as MutableList<String>
                for (message: String in responseMessage) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
            progress?.visibility = View.INVISIBLE
        }
    }

    inner class DeleteProfessionalAsyncTask(var view: View?, var menu: Menu?) : AsyncTask<Void, Void, Int>() {
        override fun onPreExecute() {
            super.onPreExecute()
            context = view?.context
            swipe = view?.findViewById(R.id.swipe_professional)
            swipe?.isRefreshing = true
            menu?.findItem(R.id.action_delete)?.isEnabled = false
        }

        override fun doInBackground(vararg params: Void?): Int? {
            try {
                response = OkHttpClient().newCall(
                        Request.Builder()
                                .url(HOST + URL_PROFESSIONAL)
                                .delete(RequestBody.create(CONTENT_TYPE, Gson().toJson(professionalLiveData.value)))
                                .build())
                        .execute()
                responseCode = response?.code()
                if (responseCode == 200) {
                    professional = Gson().fromJson(JSONObject(response?.body()?.string()).toString(), Professional::class.java)
                }
            } catch (ex: Exception) {
                Log.e("Error: ", ex.message)
            }
            return responseCode
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            swipe?.isRefreshing = false
            if (result == 200) {
                Toast.makeText(view?.context, "Deleted successfully", Toast.LENGTH_LONG).show()
                ListAllProfessionalsAsyncTask(view).execute()
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