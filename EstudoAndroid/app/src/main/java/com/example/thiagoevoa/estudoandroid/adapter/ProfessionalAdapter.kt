package com.example.thiagoevoa.estudoandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.R.layout.item_professional
import com.example.thiagoevoa.estudoandroid.model.Professional
import kotlinx.android.synthetic.main.item_professional.view.*

class ProfessionalAdapter(context: Context, objects: MutableList<Professional>?) : ArrayAdapter<Professional>(context, 0, objects) {
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

        viewHolder.professionalImage.setImageResource(R.drawable.ic_person_black)
        viewHolder.professionalCpfCnjpj.text = professional.cpf_cnpj
        viewHolder.professionalName.text = professional.name

        return convertView
    }

    class ViewHolder(parent: View) {
        var professionalImage: ImageView = parent.img_professional
        var professionalCpfCnjpj: TextView = parent.txt_professional_cpfcnpj
        var professionalName: TextView = parent.txt_professional_name

        init {
            parent.tag = this
        }
    }
}