package com.example.thiagoevoa.estudoandroid.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.R.id.action_search
import com.example.thiagoevoa.estudoandroid.activity.ScheduleDetailActivity
import com.example.thiagoevoa.estudoandroid.adapter.ScheduleAdapter
import com.example.thiagoevoa.estudoandroid.util.*
import com.example.thiagoevoa.estudoandroid.viewmodel.ScheduleViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_schedule_list.view.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class ScheduleListFragment : Fragment() {
    private var response: Response? = null
    internal var view: View? = null
    internal var menu: Menu? = null
    private var row: LinearLayout? = null
    private var menuDelete: MenuItem? = null
    private var menuSearch: MenuItem? = null
    private var searchView: SearchView? = null
    private val viewModel: ScheduleViewModel by lazy {
        ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.schedulesLiveData.observe(this, Observer {
            if (it?.size == 0) {
                view?.txt_schedule_message?.visibility = View.VISIBLE
            } else {
                view?.txt_schedule_message?.visibility = View.GONE
            }
            view?.recycler_schedule_fragment!!.layoutManager = LinearLayoutManager(activity!!.baseContext)
            view?.recycler_schedule_fragment!!.adapter = ScheduleAdapter(activity!!, it!!)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_schedule_list, container, false)
        initView()
        refreshList()
        return view
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
        this.menu = menu
        menuDelete = menu?.findItem(R.id.action_delete)
        menuSearch = menu?.findItem(R.id.action_search)

        val menuSearchItem = menu?.findItem(action_search)
        searchView = menuSearchItem?.actionView as SearchView
        searchView?.maxWidth = Int.MAX_VALUE
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                ListAsyncTask().execute(query)
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {return false}
        })
        searchView?.setOnCloseListener {
            refreshList()
            false
        }
        menuSearch?.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_search -> {
                true
            }
            R.id.action_delete -> {
                DeleteAsyncTask().execute()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        view?.swipe_schedule?.setOnRefreshListener {
            refreshList()
        }
    }

    private fun refreshList() {
        if (ListAsyncTask().status != AsyncTask.Status.RUNNING) {
            ListAsyncTask().execute()
        }
    }

    fun resetMenuIcons() {
        if (searchView!!.isIconified) {
            viewModel.scheduleLiveData.value = null
            menuDelete!!.isVisible = false
            menuSearch!!.isVisible = true
            row!!.setBackgroundColor(ContextCompat.getColor(activity!!.baseContext, R.color.colorWhite))
        } else {
            searchView!!.setQuery("", false)
            searchView!!.isIconified = true
        }
    }

    fun itemClicked(position: Int) {
        viewModel.scheduleLiveData.value = viewModel.schedulesLiveData.value?.get(position)
        if (resources.getBoolean(R.bool.tablet)) {
            activity!!.supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.detail_schedule, ScheduleDetailFragment().newInstance(viewModel.scheduleLiveData.value), SCHEDULE_DETAIL_FRAGMENT).commit()
        } else {
            val intent = Intent(activity!!.baseContext, ScheduleDetailActivity::class.java)
            intent.putExtra(EXTRA_SCHEDULE, viewModel.scheduleLiveData.value)
            startActivity(intent)
        }
    }

    fun itemLongClicked(position: Int, view: ScheduleAdapter.ViewHolder) {
        row = if (row == null) {
            view.itemLayout
        } else {
            row?.setBackgroundColor(ContextCompat.getColor(activity!!.baseContext, R.color.colorWhite))
            view.itemLayout
        }
        menuSearch?.isVisible = false
        row?.setBackgroundColor(ContextCompat.getColor(activity!!.baseContext, R.color.light_grey))
        menuDelete?.isEnabled = true
        menuDelete?.isVisible = true
        viewModel.schedulesLiveData.value?.get(position)
    }

    private inner class ListAsyncTask : AsyncTask<String, Void, Response>() {
        override fun onPreExecute() {
            super.onPreExecute()
            view?.swipe_schedule?.isRefreshing = true
        }

        override fun doInBackground(vararg params: String?): Response? {
            try {
                response = if (params.isEmpty()) {
                    OkHttpClient().newCall(
                            Request.Builder().url(URL_SCHEDULE).build()).execute()
                } else {
                    OkHttpClient().newCall(
                            Request.Builder().url("/$URL_SCHEDULE/${params[0]}").build()).execute()
                }
            } catch (ex: Exception) {
                Log.e("Error: ", ex.message)
            }
            return response
        }

        override fun onPostExecute(result: Response?) {
            super.onPostExecute(result)
            if (result?.code() == RESPONSE_OK) {
                view?.txt_schedule_message?.visibility = View.GONE
                viewModel.schedulesLiveData.value = getScheduleFromJSON(result.body()?.string())
            }else{
                view?.txt_schedule_message?.text = resources.getString(R.string.error_loading_schedule)
                view?.txt_schedule_message?.visibility = View.VISIBLE
            }
            view?.swipe_schedule?.isRefreshing = false
        }
    }
    private inner class DeleteAsyncTask: AsyncTask<Void, Void, Response>(){
        override fun onPreExecute() {
            super.onPreExecute()
            view?.swipe_schedule?.isRefreshing = true
        }
        override fun doInBackground(vararg params: Void?): Response? {
            try {
                response = OkHttpClient().newCall(Request.Builder()
                        .url(URL_SCHEDULE)
                        .delete(RequestBody.create(CONTENT_TYPE_JSON, Gson().toJson(viewModel.scheduleLiveData.value)))
                        .build())
                        .execute()
            } catch (ex: Exception) {
                Log.e("Error: ", ex.message)
            }
            return response
        }
        override fun onPostExecute(result: Response?) {
            super.onPostExecute(result)
            if(response?.code() == RESPONSE_OK){
                menuDelete?.isVisible = false
                menuSearch?.isVisible = true
                refreshList()
                showToast(activity!!.baseContext, resources.getString(R.string.success_delete_schedule))
            }else{
                showToast(activity!!.baseContext, resources.getString(R.string.error_delete_schedule))
            }
            view?.swipe_schedule?.isRefreshing = false
        }
    }
}
