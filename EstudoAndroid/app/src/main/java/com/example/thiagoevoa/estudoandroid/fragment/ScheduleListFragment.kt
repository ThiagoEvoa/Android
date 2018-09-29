package com.example.thiagoevoa.estudoandroid.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.R.id.action_search
import com.example.thiagoevoa.estudoandroid.activity.ScheduleDetailActivity
import com.example.thiagoevoa.estudoandroid.adapter.ScheduleAdapter
import com.example.thiagoevoa.estudoandroid.util.EXTRA_SCHEDULE
import com.example.thiagoevoa.estudoandroid.util.SCHEDULE_DETAIL_FRAGMENT
import com.example.thiagoevoa.estudoandroid.viewmodel.ScheduleViewModel
import kotlinx.android.synthetic.main.fragment_schedule_list.view.*

class ScheduleListFragment : Fragment(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, SearchView.OnQueryTextListener {
    internal var view: View? = null
    internal var menu: Menu? = null
    private var row: View? = null
    private var menuDelete: MenuItem? = null
    private var menuSearch: MenuItem? = null
    private var searchView: SearchView? = null
    private val viewModel: ScheduleViewModel by lazy {
        ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_schedule_list, container, false)

        view?.listView_schedule_fragment?.onItemClickListener = this
        view?.listView_schedule_fragment?.onItemLongClickListener = this
        refresh(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.ListAllSchedulesAsyncTask(view).status != AsyncTask.Status.RUNNING) {
            viewModel.ListAllSchedulesAsyncTask(view).execute()

            viewModel.schedulesLiveData.observe(this, Observer {
                view?.listView_schedule_fragment!!.adapter = ScheduleAdapter(activity!!.baseContext, it)
            })
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
        searchView?.setOnQueryTextListener(this)

        menuSearch?.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_search -> {
                true
            }
            R.id.action_delete -> {
                viewModel.DeleteScheduleAsyncTask(view, menu).execute()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (resources.getBoolean(R.bool.tablet)) {
            activity!!.supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.detail_schedule, ScheduleDetailFragment().newInstance(viewModel.schedulesLiveData.value?.get(position)), SCHEDULE_DETAIL_FRAGMENT).commit()
        } else {
            val intent = Intent(activity!!.baseContext, ScheduleDetailActivity::class.java)
            intent.putExtra(EXTRA_SCHEDULE, viewModel.schedulesLiveData.value?.get(position))
            activity!!.startActivity(intent)
        }
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        row = if (row == null) {
            view
        } else {
            row!!.setBackgroundColor(ContextCompat.getColor(activity!!.baseContext, R.color.colorWhite))
            view
        }

        menuSearch?.isVisible = false
        view!!.setBackgroundColor(ContextCompat.getColor(activity!!.baseContext, R.color.material_grey_300))
        menuDelete?.isEnabled = true
        menuDelete?.isVisible = true
        viewModel.scheduleLiveData.value = viewModel.schedulesLiveData.value?.get(position)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.ListAllSchedulesAsyncTask(view).execute(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    private fun refresh(view: View?) {
        view?.swipe_schedule?.setOnRefreshListener {
            viewModel.ListAllSchedulesAsyncTask(view).execute()
        }
    }

    fun resetMenuIcons() {
        if(searchView!!.isIconified){
            viewModel.scheduleLiveData.value = null
            menuDelete!!.isVisible = false
            menuSearch!!.isVisible = true
            row!!.setBackgroundColor(ContextCompat.getColor(activity!!.baseContext, R.color.colorWhite))
        }else{
            searchView!!.setQuery("", false)
            searchView!!.isIconified = true
        }
    }
}
