package com.example.thiagoevoa.estudoandroid.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ProgressBar
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.asynctask.SaveAsyncTask
import com.example.thiagoevoa.estudoandroid.asynctask.UpdateAsyncTask
import com.example.thiagoevoa.estudoandroid.model.Professional
import com.example.thiagoevoa.estudoandroid.util.BUNDLE_ITEM
import com.example.thiagoevoa.estudoandroid.util.RESPONSE_OK
import com.example.thiagoevoa.estudoandroid.util.URL_PROFESSIONAL
import com.example.thiagoevoa.estudoandroid.util.showToast
import com.example.thiagoevoa.estudoandroid.viewmodel.ProfessionalViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_professional_detail.*
import kotlinx.android.synthetic.main.fragment_professional_detail.view.*

class ProfessionalDetailFragment : Fragment() {
    internal var view: View? = null
    private var professional: Professional? = null
    private var progressBar: ProgressBar? = null
    private val viewModel: ProfessionalViewModel by lazy {
        ViewModelProviders.of(this).get(ProfessionalViewModel::class.java)
    }

    fun newInstance(professional: Professional?): ProfessionalDetailFragment {
        val fragment = ProfessionalDetailFragment()
        val args = Bundle()
        args.putParcelable(BUNDLE_ITEM, professional)
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_professional_detail, container, false)
        professional = arguments?.get(BUNDLE_ITEM) as Professional?
        initView()
        viewModel.professionalLiveData.observe(this, Observer {
            view?.edt_professional_cpfcnpj?.setText(it?.cpf_cnpj)
            view?.edt_professional_name?.setText(it?.name)
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
        progressBar = view!!.pb_professional
        view?.edt_professional_name?.setText(professional?.name)
        view?.edt_professional_cpfcnpj?.setText(professional?.cpf_cnpj)
    }

    private fun save() {
        when {
            view?.edt_professional_cpfcnpj?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_cpf_cnpj))
            }
            view?.edt_professional_name?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_name))
            }
            else -> {
                progressBar?.visibility = View.VISIBLE
                viewModel.professionalLiveData.value = Professional(professional?._id, edt_professional_cpfcnpj.text.toString(), edt_professional_name.text.toString())
                if (professional?._id == null) {
                    if (SaveAsyncTask(URL_PROFESSIONAL, Gson().toJson(viewModel.professionalLiveData.value)).execute().get() == RESPONSE_OK) {
                        showToast(activity!!.baseContext, resources.getString(R.string.success_create_user))
                    } else {
                        showToast(activity!!.baseContext, resources.getString(R.string.error_delete_user))
                    }
                } else {
                    if (UpdateAsyncTask(URL_PROFESSIONAL, Gson().toJson(viewModel.professionalLiveData.value)).execute().get() == RESPONSE_OK) {
                        showToast(activity!!.baseContext, resources.getString(R.string.success_update_user))
                    } else {
                        showToast(activity!!.baseContext, resources.getString(R.string.error_update_user))
                    }
                }
                progressBar?.visibility = View.GONE
            }
        }
    }
}
