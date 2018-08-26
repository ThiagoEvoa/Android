package com.example.thiagoevoa.estudoandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.thiagoevoa.estudoandroid.R.layout.item_schedule
import com.example.thiagoevoa.estudoandroid.model.Schedule
import kotlinx.android.synthetic.main.item_schedule.view.*

class ScheduleAdapter(context: Context, objects: MutableList<Schedule>?) : ArrayAdapter<Schedule>(context, 0, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var schedule: Schedule = getItem(position)
        var viewHolder: ViewHolder
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_schedule, parent, false)
            viewHolder = ViewHolder(convertView)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.date.text = schedule.date
        viewHolder.initialTime.text = schedule.initialTime
        viewHolder.finalTime.text = schedule.finalTime
        viewHolder.professional.text = schedule.professionalId
        viewHolder.client.text = schedule.clientId

        return convertView
    }

    class ViewHolder(parent: View) {
        var date: TextView = parent.txt_date
        var initialTime: TextView = parent.txt_initialTime
        var finalTime: TextView = parent.txt_final_time
        var professional: TextView = parent.txt_professional
        var client: TextView = parent.txt_client

        init {
            parent.tag = this
        }
    }
}