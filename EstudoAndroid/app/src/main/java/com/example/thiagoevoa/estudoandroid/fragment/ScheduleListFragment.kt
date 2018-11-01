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
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.R.id.action_search
import com.example.thiagoevoa.estudoandroid.activity.ScheduleDetailActivity
import com.example.thiagoevoa.estudoandroid.adapter.ScheduleAdapter
import com.example.thiagoevoa.estudoandroid.asynctask.DeleteAsyncTask
import com.example.thiagoevoa.estudoandroid.asynctask.ListAsyncTask
import com.example.thiagoevoa.estudoandroid.util.*
import com.example.thiagoevoa.estudoandroid.viewmodel.ScheduleViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_schedule_list.view.*

class ScheduleListFragment : Fragment() {
    internal var view: View? = null
    internal var menu: Menu? = null
    private var row: LinearLayout? = null
    private var menuDelete: MenuItem? = null
    private var menuSearch: MenuItem? = null
    private var searchView: SearchView? = null
    private var txtMessage: TextView? = null
    private val viewModel: ScheduleViewModel by lazy {
        ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        refreshList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_schedule_list, container, false)
        initView()
        setViewModel()
        return view
    }

    override fun onResume() {
        super.onResume()
        if (ListAsyncTask(URL_SCHEDULE).status != AsyncTask.Status.RUNNING) {
            refreshList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
        this.menu = menu
        menuDelete = menu?.findItem(R.id.action_delete)
        menuSearch = menu?.findItem(R.id.action_search)

        val menuItem = menu?.findItem(action_search)
        searchView = menuItem?.actionView as SearchView
        searchView?.maxWidth = Int.MAX_VALUE
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.schedulesLiveData.value = getScheduleFromJSON(ListAsyncTask(URL_SCHEDULE).execute(query).get())
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })
        menuSearch?.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_search -> {
                true
            }
            R.id.action_delete -> {
                if (DeleteAsyncTask(URL_SCHEDULE, Gson().toJson(viewModel.scheduleLiveData.value)).execute().get() == RESPONSE_OK) {
                    menuDelete?.isVisible = false
                    menuSearch?.isVisible = true
                    refreshList()
                    showToast(activity!!.baseContext, resources.getString(R.string.success_delete_schedule))
                } else {
                    showToast(activity!!.baseContext, resources.getString(R.string.error_delete_schedule))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        txtMessage = view?.txt_schedule_message

        view?.swipe_schedule?.setOnRefreshListener {
            refreshList()
        }
    }

    private fun setViewModel() {
        viewModel.schedulesLiveData.observe(this, Observer {
            if (it?.size == 0) {
                txtMessage?.text = resources.getString(R.string.success_no_schedule)
                txtMessage?.visibility = View.VISIBLE
            } else {
                txtMessage?.visibility = View.GONE
            }
            view?.recycler_schedule_fragment!!.layoutManager = LinearLayoutManager(activity!!.baseContext)
            view?.recycler_schedule_fragment!!.adapter = ScheduleAdapter(activity!!, viewModel.schedulesLiveData.value!!)
        })
    }

    private fun refreshList() {
        viewModel.schedulesLiveData.value = getScheduleFromJSON(ListAsyncTask(URL_SCHEDULE).execute().get())
        view?.swipe_schedule?.isRefreshing = false
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
        if (resources.getBoolean(R.bool.tablet)) {
            activity!!.supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.detail_schedule, ScheduleDetailFragment().newInstance(viewModel.schedulesLiveData.value?.get(position)), SCHEDULE_DETAIL_FRAGMENT).commit()
        } else {
            val intent = Intent(activity!!.baseContext, ScheduleDetailActivity::class.java)
            intent.putExtra(EXTRA_SCHEDULE, viewModel.schedulesLiveData.value?.get(position))
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
        viewModel.scheduleLiveData.value = viewModel.schedulesLiveData.value?.get(position)
    }
}
