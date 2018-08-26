package com.example.thiagoevoa.estudoandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.thiagoevoa.estudoandroid.R.layout.item_professional
import com.example.thiagoevoa.estudoandroid.model.Professional
import kotlinx.android.synthetic.main.item_professional.view.*

class ProfessionalAdapter(context: Context, objects: MutableList<Professional>) : ArrayAdapter<Professional>(context, 0, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var professional = getItem(position)
        var viewHolder: ViewHolder
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_professional, parent, false)
            viewHolder = ViewHolder(convertView)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.cpfcnjpj.text = professional.cpfcnpj
        viewHolder.name.text = professional.name

        return convertView
    }

    class ViewHolder(parent: View) {
        var cpfcnjpj: TextView = parent.txt_cpfcnpj
        var name: TextView = parent.txt_professional_name

        init {
            parent.tag = this
        }
    }
}