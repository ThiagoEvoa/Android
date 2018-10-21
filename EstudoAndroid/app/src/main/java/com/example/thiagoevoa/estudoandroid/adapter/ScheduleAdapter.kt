package com.example.thiagoevoa.estudoandroid.adapter

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.fragment.ScheduleListFragment
import com.example.thiagoevoa.estudoandroid.model.Schedule
import kotlinx.android.synthetic.main.item_schedule.view.*

class ScheduleAdapter(private val activity: FragmentActivity, private val objects: MutableList<Schedule>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false))
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = objects[position].date
        holder.initialTime.text = objects[position].initialTime
        holder.finalTime.text = objects[position].finalTime
        holder.professional.text = objects[position].professionalId
        holder.client.text = objects[position].clientId

        holder.itemView.setOnClickListener {
            (activity.supportFragmentManager.fragments[0] as ScheduleListFragment).itemClicked(position)
        }

        holder.itemView.setOnLongClickListener {
            (activity.supportFragmentManager.fragments[0] as ScheduleListFragment).itemLongClicked(position, holder)
            true
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var date: TextView = view.txt_date
        var initialTime: TextView = view.txt_initialTime
        var finalTime: TextView = view.txt_final_time
        var professional: TextView = view.txt_professional
        var client: TextView = view.txt_client
        var itemLayout: LinearLayout = view.item_layout
    }
}