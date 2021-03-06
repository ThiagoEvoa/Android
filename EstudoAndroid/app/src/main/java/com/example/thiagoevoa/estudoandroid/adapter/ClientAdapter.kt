package com.example.thiagoevoa.estudoandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.thiagoevoa.estudoandroid.R
import com.example.thiagoevoa.estudoandroid.R.layout.item_client
import com.example.thiagoevoa.estudoandroid.model.Client
import kotlinx.android.synthetic.main.item_client.view.*

class ClientAdapter(context: Context, objects: MutableList<Client>) : ArrayAdapter<Client>(context, 0, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val client: Client = getItem(position)!!
        val viewHolder: ViewHolder
        var view = convertView

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(item_client, parent, false)
            viewHolder = ViewHolder(view)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.clientCpf.text = client.cpf
        viewHolder.clientName.text = client.name
        viewHolder.clientImage.setImageResource(R.drawable.ic_client)
        return view
    }

    class ViewHolder(parent: View) {
        var clientImage: ImageView = parent.img_client
        var clientCpf: TextView = parent.txt_client_cpf
        var clientName: TextView = parent.txt_client_name
        init {
            parent.tag = this
        }
    }
}