package com.example.revisionequipamiento.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.revisionequipamiento.R

class MyAdapterEmpty(private val mDataset: String) : RecyclerView.Adapter<MyAdapterEmpty.MyViewHolder>() {

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textEmpty: TextView
        init {
            textEmpty = v.findViewById<View>(R.id.text_tx) as TextView
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterEmpty.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.empty_page, parent, false)
        return MyAdapterEmpty.MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyAdapterEmpty.MyViewHolder, position: Int) {
        holder.textEmpty.text=mDataset
    }

    override fun getItemCount(): Int {
        return 1
    }
}
