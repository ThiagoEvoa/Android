package com.example.thiagoevoa.estudoandroid.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.model.Professional
import com.example.thiagoevoa.estudoandroid.util.BUNDLE_POSITION
import com.example.thiagoevoa.estudoandroid.viewmodel.ProfessionalViewModel
import kotlinx.android.synthetic.main.fragment_professional_detail.*
import kotlinx.android.synthetic.main.fragment_professional_detail.view.*

class ProfessionalDetailFragment : Fragment() {
    internal var view: View? = null
    private var professional: Professional? = null

    private val viewModel: ProfessionalViewModel by lazy {
        ViewModelProviders.of(this).get(ProfessionalViewModel::class.java)
    }

    fun newInstance(professional: Professional?): ProfessionalDetailFragment {
        val fragment = ProfessionalDetailFragment()
        val args = Bundle()
        args.putParcelable(BUNDLE_POSITION, professional)
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

        professional = arguments?.get(BUNDLE_POSITION) as Professional?
        mountView(professional)

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
                saveProfessional()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveProfessional(){
        viewModel.professionalLiveData.value = mountProfessional()
        viewModel.SaveProfessionalAsyncTask(this.view).execute()
    }

    private fun mountView(professional: Professional?){
        view?.edt_professional_cpfcnpj?.setText(professional?.cpf_cnpj)
        view?.edt_professional_name?.setText(professional?.name)
    }

    private fun mountProfessional(): Professional{
        return Professional(professional?._id,
                edt_professional_cpfcnpj.text.toString(),
                edt_professional_name.text.toString())
    }
}
