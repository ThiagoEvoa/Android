package com.example.thiagoevoa.estudoandroid.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ProgressBar
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.asynctask.ListAsyncTask
import com.example.thiagoevoa.estudoandroid.asynctask.SaveAsyncTask
import com.example.thiagoevoa.estudoandroid.asynctask.UpdateAsyncTask
import com.example.thiagoevoa.estudoandroid.model.Client
import com.example.thiagoevoa.estudoandroid.util.BUNDLE_POSITION
import com.example.thiagoevoa.estudoandroid.util.RESPONSE_OK
import com.example.thiagoevoa.estudoandroid.util.URL_CLIENT
import com.example.thiagoevoa.estudoandroid.util.showToast
import com.example.thiagoevoa.estudoandroid.viewmodel.ClientViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_client_detail.view.*

class ClientDetailFragment : Fragment() {
    internal var view: View? = null
    private var client: Client? = null
    private var progressBar: ProgressBar? = null

    private val viewModel: ClientViewModel by lazy {
        ViewModelProviders.of(this).get(ClientViewModel::class.java)
    }

    fun newInstance(client: Client?): ClientDetailFragment {
        val fragment = ClientDetailFragment()
        val args = Bundle()
        args.putParcelable(BUNDLE_POSITION, client)
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_client_detail, container, false)

        client = arguments?.get(BUNDLE_POSITION) as Client?
        initView()
        viewModel.clientLiveData.observe(this, Observer {
            view?.edt_client_cpf?.setText(it?.cpf)
            view?.edt_client_name?.setText(it?.name)
        })
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu, menu)
        menu!!.findItem(R.id.action_save).isVisible = true
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
        progressBar = view?.pb_client
        view?.edt_client_cpf?.setText(client?.cpf)
        view?.edt_client_name?.setText(client?.name)
    }

    private fun save() {
        when {
            view?.edt_client_cpf?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_cpf))
            }
            view?.edt_client_name?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_name))
            }
            else -> {
                progressBar?.visibility = View.VISIBLE
                viewModel.clientLiveData.value = Client(client?._id, view?.edt_client_cpf?.text.toString(), view?.edt_client_name?.text.toString())
                if (client?._id == null) {
                    if (SaveAsyncTask(URL_CLIENT, Gson().toJson(viewModel.clientLiveData.value)).execute().get() == RESPONSE_OK) {
                        showToast(activity!!.baseContext, resources.getString(R.string.success_create_user))
                    } else {
                        showToast(activity!!.baseContext, resources.getString(R.string.error_create_user))
                    }
                } else {
                    if (UpdateAsyncTask(URL_CLIENT, Gson().toJson(viewModel.clientLiveData.value)).execute().get() == RESPONSE_OK) {
                        ListAsyncTask(URL_CLIENT).execute()
                        showToast(activity!!.baseContext, resources.getString(R.string.success_create_user))
                    } else {
                        showToast(activity!!.baseContext, resources.getString(R.string.error_create_user))
                    }
                }
                progressBar?.visibility = View.GONE
            }
        }
    }
}


