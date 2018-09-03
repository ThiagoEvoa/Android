package com.example.thiagoevoa.estudoandroid.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.model.Schedule
import com.example.thiagoevoa.estudoandroid.util.BUNDLE_POSITION
import com.example.thiagoevoa.estudoandroid.viewmodel.ScheduleViewModel
import kotlinx.android.synthetic.main.fragment_schedule_detail.*
import kotlinx.android.synthetic.main.fragment_schedule_detail.view.*

class ScheduleDetailFragment : Fragment() {
    internal var view: View? = null
    private var schedule: Schedule? = null

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
        mountView(schedule)

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
                saveSchedule()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveSchedule(){
        viewModel.scheduleLiveData.value = mountSchedule()
        viewModel.SaveScheduleAsyncTask(this.view).execute()
    }

    private fun mountView(schedule: Schedule?) {
        view?.edt_date?.setText(schedule?.date)
        view?.edt_initial_time?.setText(schedule?.initialTime)
        view?.edt_final_time?.setText(schedule?.finalTime)
        view?.edt_professional?.setText(schedule?.professionalId)
        view?.edt_client?.setText(schedule?.clientId)
    }

    private fun mountSchedule(): Schedule {
        return Schedule(schedule?._id,
                edt_date.text.toString(),
                edt_initial_time.text.toString(),
                edt_final_time.text.toString(),
                edt_client.text.toString(),
                edt_professional.text.toString())
    }
}
