package com.technology.codespace.rem.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.technology.codespace.rem.Data.Contact
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.technology.codespace.rem.R
import android.support.v4.content.ContextCompat.startActivity



/**
 * Created by Yash on 18-03-2018.
 */
class ContactAdapter(private val context:Context, private val list:ArrayList<Contact>):RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.contact_row,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder:ViewHolder ?, position: Int) {
        holder!!.bindViews(list[position])
        holder.row.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "${holder.number}"))
            startActivity(context,intent,null)
        }
    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        var name = itemView.findViewById<TextView>(R.id.contact_name)
        var number = itemView.findViewById<TextView>(R.id.contactNumber)
        var row = itemView.findViewById<LinearLayout>(R.id.row);
        fun bindViews(c:Contact)
        {
            name.setText(c.cname)
            number.setText(c.cno)



        }
    }
    fun removeItem(position: Int) {

        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }



}