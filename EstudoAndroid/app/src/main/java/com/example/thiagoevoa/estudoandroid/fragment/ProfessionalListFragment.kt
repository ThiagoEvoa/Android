package com.example.thiagoevoa.estudoandroid.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.activity.ProfessionalDetailActivity
import com.example.thiagoevoa.estudoandroid.adapter.ProfessionalAdapter
import com.example.thiagoevoa.estudoandroid.asynctask.DeleteAsyncTask
import com.example.thiagoevoa.estudoandroid.asynctask.ListAsyncTask
import com.example.thiagoevoa.estudoandroid.util.*
import com.example.thiagoevoa.estudoandroid.viewmodel.ProfessionalViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_professional_list.view.*

class ProfessionalListFragment : Fragment() {
    internal var view: View? = null
    internal var menu: Menu? = null
    private var row: View? = null
    private var menuDelete: MenuItem? = null
    private var menuSearch: MenuItem? = null
    private var searchView: SearchView? = null
    private var txtMessage: TextView? = null
    private var refresh: SwipeRefreshLayout? = null
    private val viewModel: ProfessionalViewModel by lazy {
        ViewModelProviders.of(this).get(ProfessionalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_professional_list, container, false)
        initView()
        return view
    }

    override fun onResume() {
        super.onResume()
        if (ListAsyncTask(URL_PROFESSIONAL).status != AsyncTask.Status.RUNNING) {
            refreshList()
            viewModel.professionalsLiveData.observe(this, Observer {
                if (it?.size == 0) {
                    txtMessage?.text = resources.getString(R.string.success_no_professional)
                    txtMessage?.visibility = View.VISIBLE
                } else {
                    txtMessage?.visibility = View.GONE
                }
                view?.listView_professional_fragment!!.adapter = ProfessionalAdapter(activity!!.baseContext, it!!)
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
        searchView?.maxWidth = Int.MAX_VALUE
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                ListAsyncTask(URL_PROFESSIONAL).execute(query)
                viewModel.professionalsLiveData.value = getProfessionalFromJSON(ListAsyncTask.result)
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
                if (DeleteAsyncTask(URL_PROFESSIONAL, Gson().toJson(viewModel.professionalLiveData.value)).execute().get() == RESPONSE_OK) {
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
        refresh = view?.swipe_professional
        txtMessage = view?.txt_professional_message

        view?.swipe_professional?.setOnRefreshListener {
            refreshList()
        }

        view?.listView_professional_fragment?.onItemClickListener = (AdapterView.OnItemClickListener
        { parent, view, position, id ->
            if (resources.getBoolean(R.bool.tablet)) {
                viewModel.professionalLiveData.value = viewModel.professionalsLiveData.value?.get(position)
                activity!!.supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.detail_professional, ProfessionalDetailFragment(), PROFESSIONAL_DETAIL_FRAGMENT).commit()
            } else {
                val intent = Intent(activity!!.baseContext, ProfessionalDetailActivity::class.java)
                intent.putExtra(EXTRA_PROFESSIONAL, viewModel.professionalsLiveData.value?.get(position))
                activity!!.startActivity(intent)
            }
        })

        view?.listView_professional_fragment?.onItemLongClickListener = (AdapterView.OnItemLongClickListener
        { parent, view, position, id ->
            row = if (row == null) {
                view
            } else {
                row!!.setBackgroundColor(ContextCompat.getColor(activity!!.baseContext, R.color.colorWhite))
                view
            }
            menuSearch?.isVisible = false
            view!!.setBackgroundColor(ContextCompat.getColor(activity!!.baseContext, R.color.light_grey))
            menuDelete?.isEnabled = true
            menuDelete?.isVisible = true
            viewModel.professionalLiveData.value = viewModel.professionalsLiveData.value?.get(position)
            true
        })
    }

    private fun refreshList() {
        view?.swipe_professional?.isRefreshing = true
        ListAsyncTask(URL_PROFESSIONAL).execute()
        viewModel.professionalsLiveData.value = getProfessionalFromJSON(ListAsyncTask.result)
        view?.swipe_professional?.isRefreshing = false
    }

    fun resetMenuIcons() {
        if (searchView!!.isIconified) {
            viewModel.professionalLiveData.value = null
            menuDelete!!.isVisible = false
            menuSearch!!.isVisible = true
            row!!.setBackgroundColor(ContextCompat.getColor(activity!!.baseContext, R.color.colorWhite))
        } else {
            searchView!!.setQuery("", false)
            searchView!!.isIconified = true
        }
    }
}
