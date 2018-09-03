package com.example.thiagoevoa.estudoandroid.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.model.Client
import com.example.thiagoevoa.estudoandroid.util.BUNDLE_POSITION
import com.example.thiagoevoa.estudoandroid.viewmodel.ClientViewModel
import kotlinx.android.synthetic.main.fragment_client_detail.*
import kotlinx.android.synthetic.main.fragment_client_detail.view.*

class ClientDetailFragment : Fragment() {
    internal var view: View? = null
    private var client: Client? = null

    private val viewModel: ClientViewModel by lazy{
        ViewModelProviders.of(this).get(ClientViewModel::class.java)
    }

    fun newInstance(client: Client?): ClientDetailFragment{
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
        mountView(client)

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
                saveClient()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveClient(){
        viewModel.clientLiveData.value = mountClient()
        viewModel.SaveClientAsyncTask(this.view).execute()
    }

    private fun mountView(client: Client?){
        view?.edt_client_cpf?.setText(client?.cpf)
        view?.edt_client_name?.setText(client?.name)
    }

    private fun mountClient(): Client{
        return Client(client?._id,
                edt_client_cpf.text.toString(),
                edt_client_name.text.toString())
    }
}
