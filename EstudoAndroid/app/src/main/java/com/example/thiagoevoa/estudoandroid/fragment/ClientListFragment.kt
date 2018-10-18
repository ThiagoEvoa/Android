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
import android.widget.TextView
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.activity.ClientDetailActivity
import com.example.thiagoevoa.estudoandroid.adapter.ClientAdapter
import com.example.thiagoevoa.estudoandroid.asynctask.DeleteAsyncTask
import com.example.thiagoevoa.estudoandroid.asynctask.ListAsyncTask
import com.example.thiagoevoa.estudoandroid.util.*
import com.example.thiagoevoa.estudoandroid.viewmodel.ClientViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_client_list.view.*

class ClientListFragment : Fragment() {
    internal var view: View? = null
    internal var menu: Menu? = null
    private var row: View? = null
    private var menuDelete: MenuItem? = null
    private var menuSearch: MenuItem? = null
    private var searchView: SearchView? = null
    private var txtMessage: TextView? = null
    private val viewModel: ClientViewModel by lazy {
        ViewModelProviders.of(this).get(ClientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_client_list, container, false)
        initView()
        return view
    }

    override fun onResume() {
        super.onResume()
        if (ListAsyncTask(URL_CLIENT).status != AsyncTask.Status.RUNNING) {
            refreshList()
            viewModel.clientsLiveData.observe(this, Observer {
                if (it?.size == 0) {
                    txtMessage?.text = resources.getString(R.string.success_no_client)
                    txtMessage?.visibility = View.VISIBLE
                }else{
                    txtMessage?.visibility = View.GONE
                }
                view?.listView_client_fragment!!.adapter = ClientAdapter(activity!!.baseContext, it!!)
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
        this.menu = menu
        menuDelete = menu?.findItem(R.id.action_delete)
        menuSearch = menu?.findItem(R.id.action_search)

        val menuItem = menu?.findItem(R.id.action_search)
        searchView = menuItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.clientsLiveData.value = getClientFromJSON(ListAsyncTask(URL_CLIENT).execute(query).get())
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
                if (DeleteAsyncTask(URL_CLIENT, Gson().toJson(viewModel.clientLiveData.value)).execute().get() == RESPONSE_OK) {
                    menuDelete?.isVisible = false
                    menuSearch?.isVisible = true
                    refreshList()
                    showToast(activity!!.baseContext, resources.getString(R.string.success_delete_user))
                } else {
                    showToast(activity!!.baseContext, resources.getString(R.string.error_delete_user))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        txtMessage = view?.txt_message

        view?.swipe_client?.setOnRefreshListener {
            refreshList()
        }

        view?.listView_client_fragment?.onItemClickListener = (AdapterView.OnItemClickListener
        { parent, view, position, id ->
            if (resources.getBoolean(R.bool.tablet)) {
                viewModel.clientLiveData.value = viewModel.clientsLiveData.value?.get(position)
                activity!!.supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.detail_client, ScheduleDetailFragment(), CLIENT_DETAIL_FRAGMENT).commit()
            } else {
                val intent = Intent(activity!!.baseContext, ClientDetailActivity::class.java)
                intent.putExtra(EXTRA_CLIENT, viewModel.clientsLiveData.value?.get(position))
                activity!!.startActivity(intent)
            }
        })

        view?.listView_client_fragment?.onItemLongClickListener = (AdapterView.OnItemLongClickListener
        { parent, view, position, id ->
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
            viewModel.clientLiveData.value = viewModel.clientsLiveData.value?.get(position)
            true
        })
    }

    private fun refreshList() {
        view?.swipe_client?.isRefreshing = true
        viewModel.clientsLiveData.value = getClientFromJSON(ListAsyncTask(URL_CLIENT).execute().get())
        view?.swipe_client?.isRefreshing = false
    }

    fun resetMenuIcons() {
        if (searchView!!.isIconified) {
            viewModel.clientLiveData.value = null
            menuDelete!!.isVisible = false
            menuSearch!!.isVisible = true
            row!!.setBackgroundColor(ContextCompat.getColor(activity!!.baseContext, R.color.colorWhite))
        } else {
            searchView!!.setQuery("", false)
            searchView!!.isIconified = true
        }
    }
}