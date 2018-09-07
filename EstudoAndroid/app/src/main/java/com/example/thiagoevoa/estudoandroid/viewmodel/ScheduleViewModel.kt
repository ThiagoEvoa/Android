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
import com.example.thiagoevoa.estudoandroid.model.Schedule
import com.example.thiagoevoa.estudoandroid.util.CONTENT_TYPE
import com.example.thiagoevoa.estudoandroid.util.HOST
import com.example.thiagoevoa.estudoandroid.util.URL_SCHEDULE
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

class ScheduleViewModel : ViewModel() {
    var schedulesLiveData = MutableLiveData<MutableList<Schedule>>()
    var scheduleLiveData = MutableLiveData<Schedule>()
    private var schedule: Schedule? = null
    private var swipe: SwipeRefreshLayout? = null
    private var progress: ProgressBar? = null
    private var txtMessage: TextView? = null
    private var response: Response? = null
    private var responseCode: Int? = null
    private var responseMessage: MutableList<String> = mutableListOf()
    private var context: Context? = null

    inner class ListAllSchedulesAsyncTask(var view: View?) : AsyncTask<String, Void, MutableList<Schedule>>() {
        override fun onPreExecute() {
            super.onPreExecute()
            txtMessage = view?.findViewById(R.id.txt_message)
            swipe = view?.findViewById(R.id.swipe_schedule)
            swipe?.isRefreshing = true
        }

        override fun doInBackground(vararg params: String?): MutableList<Schedule> {
            var schedules = mutableListOf<Schedule>()
            try {
                response = if (params.isEmpty()) {
                    OkHttpClient().newCall(
                            Request.Builder().url("$HOST$URL_SCHEDULE").build()).execute()
                } else {
                    OkHttpClient().newCall(
                            Request.Builder().url("$HOST$URL_SCHEDULE/clientid/${params[0]}").build()).execute()
                }

                val jsonString = response?.body()?.string()
                val jsonArray = JSONArray(jsonString)

                for (i in 0 until jsonArray.length()) {
                    var schedule = Gson().fromJson(jsonArray[i].toString(), Schedule::class.java)
                    schedules.add(schedule)
                }
            } catch (ex: Exception) {
                Log.e("Error: ", ex.message)
            }
            return schedules
        }

        override fun onPostExecute(result: MutableList<Schedule>?) {
            schedulesLiveData.value = result

            if (schedulesLiveData.value?.size == 0) {
                txtMessage?.text = "There is no schedule with those data"
                txtMessage?.visibility = View.VISIBLE
            } else {
                txtMessage?.visibility = View.GONE
            }
            swipe?.isRefreshing = false
        }
    }

    inner class SaveScheduleAsyncTask(var view: View?) : AsyncTask<Void, Void, Int>() {
        override fun onPreExecute() {
            super.onPreExecute()
            context = view?.context
            progress = view?.findViewById(R.id.pb_schedule)
            progress?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): Int? {
            try {
                response = if (scheduleLiveData?.value?._id != null) {
                    OkHttpClient().newCall(
                            Request.Builder()
                                    .url(HOST + URL_SCHEDULE)
                                    .put(RequestBody.create(CONTENT_TYPE, Gson().toJson(scheduleLiveData.value)))
                                    .build())
                            .execute()
                } else {
                    OkHttpClient().newCall(
                            Request.Builder()
                                    .url(HOST + URL_SCHEDULE)
                                    .post(RequestBody.create(CONTENT_TYPE, Gson().toJson(scheduleLiveData.value)))
                                    .build())
                            .execute()
                }
                responseCode = response?.code()
                if (responseCode == 200) {
                    schedule = Gson().fromJson(response?.body()?.string(), Schedule::class.java)
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
                ListAllSchedulesAsyncTask(view).execute()
            } else {
                responseMessage = JSONObject(response?.body()?.string()).getString("error").split(",") as MutableList<String>
                for (message: String in responseMessage) {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
            progress?.visibility = View.INVISIBLE
        }
    }

    inner class DeleteScheduleAsyncTask(var view: View?, var menu: Menu?) : AsyncTask<Void, Void, Int>() {
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
                                .url(HOST + URL_SCHEDULE)
                                .delete(RequestBody.create(CONTENT_TYPE, Gson().toJson(scheduleLiveData.value)))
                                .build())
                        .execute()
                responseCode = response?.code()
                if (responseCode == 200) {
                    schedule = Gson().fromJson(JSONObject(response?.body()?.string()).toString(), Schedule::class.java)
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
                ListAllSchedulesAsyncTask(view).execute()
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