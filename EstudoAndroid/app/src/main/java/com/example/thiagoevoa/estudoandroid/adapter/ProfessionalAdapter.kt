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

class ProfessionalAdapter(context: Context, objects: MutableList<Professional>) : ArrayAdapter<Professional>(context, 0, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val professional = getItem(position)!!
        val viewHolder: ViewHolder
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(item_professional, parent, false)
            viewHolder = ViewHolder(view)
        } else {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.professionalImage.setImageResource(R.drawable.ic_person_black)
        viewHolder.professionalCpfCnjpj.text = professional.cpf_cnpj
        viewHolder.professionalName.text = professional.name
        return view
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