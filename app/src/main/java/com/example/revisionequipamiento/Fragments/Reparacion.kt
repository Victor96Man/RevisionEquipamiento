package com.example.revisionequipamiento.Fragments

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.revisionequipamiento.Adapter.MyAdapterCards
import com.example.revisionequipamiento.Adapter.MyAdapterEmpty
import com.example.revisionequipamiento.BBDDSQLite
import com.example.revisionequipamiento.Clases.EquipamientoItem
import com.example.revisionequipamiento.R

class Reparacion : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_blank, container, false)

        val rv = rootView.findViewById<View>(R.id.rv_recycler_view) as RecyclerView
        rv.setHasFixedSize(true)
        var equipos : ArrayList<EquipamientoItem>? = null
        val bbddsqlite = BBDDSQLite(requireContext())
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT t1.*, t2.nombrefamilia as nombrefamilia, t4.nombreubicacion as nombreubicacion FROM equipamientos as t1, familias as t2,  ubicaciones as t4 WHERE t1.id_familia = t2.id  AND t1.id_ubicacion = t4.id AND t1.estado=1 AND t1.id_zona in (SELECT id_zona FROM usuariosZonas) ORDER BY t1.fecha_proxima_revision asc", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    equipos = ArrayList<EquipamientoItem>()
                }
                do {
                    val id_equipamiento = cusrsor.getString(cusrsor.getColumnIndex("n_serie"))
                    val familia = cusrsor.getString(cusrsor.getColumnIndex("nombrefamilia"))
                    val ubicacion = cusrsor.getString(cusrsor.getColumnIndex("nombreubicacion"))
                    val fecha = cusrsor.getString(cusrsor.getColumnIndex("fecha_proxima_revision"))
                    if (equipos != null) {
                        equipos.add(EquipamientoItem(id_equipamiento,familia,ubicacion,fecha))
                    }
                }while (cusrsor.moveToNext())
                db.close()

                val adapter = MyAdapterCards(equipos!!)
                rv.adapter = adapter

                val llm = LinearLayoutManager(activity)
                rv.layoutManager = llm
            }else{

                val adapter = MyAdapterEmpty(getString(R.string.emptyReparacion))
                rv.adapter = adapter

                val llm = LinearLayoutManager(activity)
                rv.layoutManager = llm
            }

        }else{

        }


        return rootView
    }


}