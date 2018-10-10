package com.example.revisionequipamiento.Adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.revisionequipamiento.Clases.EquipamientoItem
import com.example.revisionequipamiento.EquipamientoActivity
import com.example.revisionequipamiento.R

class MyAdapterCards(context: Context,private val mDataset: ArrayList<EquipamientoItem>) : RecyclerView.Adapter<MyAdapterCards.MyViewHolder>() {
    val mContext = context
    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v){


        var mCardView: CardView
        var n_serie: TextView
        var familia: TextView
        var ubicacion: TextView
        var fecha: TextView
        var trabajador: TextView
        var icon: View

        init {

            mCardView = v.findViewById<View>(R.id.card_view) as CardView
            n_serie = v.findViewById<View>(R.id.n_serie_tx) as TextView
            familia = v.findViewById<View>(R.id.familia_tx) as TextView
            ubicacion = v.findViewById<View>(R.id.ubicacion_tx) as TextView
            fecha = v.findViewById<View>(R.id.fecha_tx) as TextView
            trabajador = v.findViewById<View>(R.id.trabajador_tx) as TextView
            icon = v.findViewById<View>(R.id.icon_circle) as View

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var fecha = mDataset.get(position).fecha
        fecha = fecha!!.substring(8,10)+"-"+fecha!!.substring(5,7)+"-"+fecha!!.substring(0,4)
        holder.n_serie.text = mDataset.get(position).id_equipamiento
        holder.familia.text = mDataset.get(position).familia
        holder.ubicacion.text = mDataset.get(position).ubicacion
        holder.fecha.text = fecha
        when (mDataset.get(position).icon){
            0 -> holder.icon.background = getDrawable(mContext,R.drawable.circle_negro)
            1 -> holder.icon.background = getDrawable(mContext,R.drawable.circle_rojo)
            2 -> holder.icon.background = getDrawable(mContext,R.drawable.circle_naranja)
            3 -> holder.icon.background = getDrawable(mContext,R.drawable.circle_verde)
        }

        if(mDataset.get(position).trabajador!=null){
            holder.trabajador.text = mDataset.get(position).trabajador
        }else{
            holder.trabajador.text = "No asiganado"
        }
        holder.itemView.setOnClickListener {
            val intent =Intent(mContext,EquipamientoActivity::class.java)
            intent.putExtra("n_serie",holder.n_serie.text.toString())
            startActivity(mContext,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

}
