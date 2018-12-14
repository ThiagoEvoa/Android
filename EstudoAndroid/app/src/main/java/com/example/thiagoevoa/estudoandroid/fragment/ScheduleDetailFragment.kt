package com.example.thiagoevoa.estudoandroid.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.R.id.action_save
import com.example.thiagoevoa.estudoandroid.model.Schedule
import com.example.thiagoevoa.estudoandroid.util.*
import com.example.thiagoevoa.estudoandroid.viewmodel.ScheduleViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_schedule_detail.*
import kotlinx.android.synthetic.main.fragment_schedule_detail.view.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class ScheduleDetailFragment : Fragment() {
    private var response: Response? = null
    internal var view: View? = null
    private var schedule: Schedule? = null
    private var menuSave: MenuItem? = null
    private val viewModel: ScheduleViewModel by lazy {
        ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
    }

    fun newInstance(schedule: Schedule?): ScheduleDetailFragment {
        val fragment = ScheduleDetailFragment()
        val args = Bundle()
        args.putParcelable(BUNDLE_ITEM, schedule)
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_schedule_detail, container, false)
        schedule = arguments?.get(BUNDLE_ITEM) as Schedule?
        initView()
        viewModel.scheduleLiveData.observe(this, Observer {
            view?.edt_date?.setText(it?.date)
            view?.edt_initial_time?.setText(it?.initialTime)
            view?.edt_final_time?.setText(it?.finalTime)
            view?.edt_professional?.setText(it?.professionalId)
            view?.edt_client?.setText(it?.clientId)
        })
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
        menuSave = menu?.findItem(action_save)
        menuSave?.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        view?.swipe_detail_schedule?.setOnRefreshListener {
            view?.swipe_detail_schedule?.isRefreshing = false
        }
        view?.edt_date?.setText(schedule?.date)
        view?.edt_initial_time?.setText(schedule?.initialTime)
        view?.edt_final_time?.setText(schedule?.finalTime)
        view?.edt_professional?.setText(schedule?.professionalId)
        view?.edt_client?.setText(schedule?.clientId)
    }

    private fun save() {
        if (validateForm()) {
            viewModel.scheduleLiveData.value = Schedule(schedule?._id, edt_date.text.toString(), edt_initial_time.text.toString(),
                    edt_final_time.text.toString(), edt_client.text.toString(), edt_professional.text.toString())
            if (schedule?._id == null) {
                SaveAsyncTask().execute()
            } else {
                UpdateAsyncTask().execute()
            }
        }
    }

    private fun validateForm(): Boolean {
        when {
            view?.edt_date?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_date))
                return false
            }
            view?.edt_initial_time?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_initial_time))
                return false
            }
            view?.edt_final_time?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_final_time))
                return false
            }
            view?.edt_client?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_client))
                return false
            }
            view?.edt_professional?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_professional))
                return false
            }
            else -> {
                return true
            }
        }
    }

    private inner class SaveAsyncTask : AsyncTask<Void, Void, Response>() {
        override fun onPreExecute() {
            super.onPreExecute()
            menuSave?.isEnabled = false
            view?.swipe_detail_schedule?.isRefreshing = true
        }

        override fun doInBackground(vararg params: Void?): Response? {
            try {
                response = OkHttpClient().newCall(Request.Builder()
                        .url(URL_SCHEDULE)
                        .post(RequestBody.create(CONTENT_TYPE_JSON, Gson().toJson(viewModel.scheduleLiveData.value)))
                        .build())
                        .execute()
            } catch (ex: Exception) {
                Log.e("Error: ", ex.message)
            }
            return response
        }

        override fun onPostExecute(result: Response?) {
            super.onPostExecute(result)
            if (result?.code() == RESPONSE_OK) {
                showToast(activity!!.baseContext, resources.getString(R.string.success_create_schedule))
            } else {
                showToast(activity!!.baseContext, resources.getString(R.string.error_create_schedule))
            }
            view?.swipe_detail_schedule?.isRefreshing = false
            menuSave?.isEnabled = true
        }
    }

    private inner class UpdateAsyncTask : AsyncTask<Void, Void, Response>() {
        override fun onPreExecute() {
            super.onPreExecute()
            menuSave?.isEnabled = false
            view?.swipe_detail_schedule?.isRefreshing = true
        }

        override fun doInBackground(vararg params: Void?): Response? {
            try {
                response = OkHttpClient().newCall(Request.Builder()
                        .url(URL_SCHEDULE)
                        .put(RequestBody.create(CONTENT_TYPE_JSON, Gson().toJson(viewModel.scheduleLiveData.value)))
                        .build())
                        .execute()
            } catch (ex: Exception) {
                Log.e("Error: ", ex.message)
            }
            return response
        }

        override fun onPostExecute(result: Response?) {
            super.onPostExecute(result)
            if (result?.code() == RESPONSE_OK) {
                showToast(activity!!.baseContext, resources.getString(R.string.success_update_schedule))
            } else {
                showToast(activity!!.baseContext, resources.getString(R.string.error_update_schedule))
            }
            view?.swipe_detail_schedule?.isRefreshing = false
            menuSave?.isEnabled = true
        }
    }
}
