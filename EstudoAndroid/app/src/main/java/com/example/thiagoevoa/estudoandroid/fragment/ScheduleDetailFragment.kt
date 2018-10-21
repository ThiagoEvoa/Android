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
import com.example.thiagoevoa.estudoandroid.model.Schedule
import com.example.thiagoevoa.estudoandroid.util.BUNDLE_POSITION
import com.example.thiagoevoa.estudoandroid.util.RESPONSE_OK
import com.example.thiagoevoa.estudoandroid.util.URL_SCHEDULE
import com.example.thiagoevoa.estudoandroid.util.showToast
import com.example.thiagoevoa.estudoandroid.viewmodel.ScheduleViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_schedule_detail.*
import kotlinx.android.synthetic.main.fragment_schedule_detail.view.*

class ScheduleDetailFragment : Fragment() {
    internal var view: View? = null
    private var schedule: Schedule? = null
    private var progressBar: ProgressBar? = null
    private val viewModel: ScheduleViewModel by lazy {
        ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
    }

    fun newInstance(schedule: Schedule?): ScheduleDetailFragment {
        val fragment = ScheduleDetailFragment()
        val args = Bundle()
        args.putParcelable(BUNDLE_POSITION, schedule)
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
        schedule = arguments?.get(BUNDLE_POSITION) as Schedule?
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
        progressBar = view?.pb_schedule
        view?.edt_date?.setText(schedule?.date)
        view?.edt_initial_time?.setText(schedule?.initialTime)
        view?.edt_final_time?.setText(schedule?.finalTime)
        view?.edt_professional?.setText(schedule?.professionalId)
        view?.edt_client?.setText(schedule?.clientId)
    }

    private fun save() {
        when {
            view?.edt_date?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_date))
            }
            view?.edt_initial_time?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_initial_time))
            }
            view?.edt_final_time?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_final_time))
            }
            view?.edt_client?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_client))
            }
            view?.edt_professional?.text.toString().isEmpty() -> {
                showToast(activity!!.baseContext, resources.getString(R.string.error_edt_professional))
            }
            else -> {
                progressBar?.visibility = View.VISIBLE
                viewModel.scheduleLiveData.value = Schedule(schedule?._id, edt_date.text.toString(), edt_initial_time.text.toString(), edt_final_time.text.toString(), edt_client.text.toString(), edt_professional.text.toString())
                if (schedule?._id == null) {
                    if (SaveAsyncTask(URL_SCHEDULE, Gson().toJson(viewModel.scheduleLiveData.value)).execute().get() == RESPONSE_OK) {
                        showToast(activity!!.baseContext, resources.getString(R.string.success_create_schedule))
                    } else {
                        showToast(activity!!.baseContext, resources.getString(R.string.error_create_schedule))
                    }
                } else {
                    if (UpdateAsyncTask(URL_SCHEDULE, Gson().toJson(viewModel.scheduleLiveData.value)).execute().get() == RESPONSE_OK) {
                        showToast(activity!!.baseContext, resources.getString(R.string.success_update_schedule))
                    } else {
                        showToast(activity!!.baseContext, resources.getString(R.string.error_update_schedule))
                    }
                }
                progressBar?.visibility = View.GONE
            }
        }
    }
}
