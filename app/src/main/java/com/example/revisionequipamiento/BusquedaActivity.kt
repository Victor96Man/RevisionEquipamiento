package com.example.revisionequipamiento

import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.example.revisionequipamiento.Adapter.MyAdapterCards
import com.example.revisionequipamiento.Clases.EquipamientoItem
import kotlinx.android.synthetic.main.activity_busqueda.*
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.revisionequipamiento.Adapter.MyAdapterEmpty
import kotlinx.android.synthetic.main.filtro_dialog.*


class BusquedaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)
        BuscarEquipamiento()
        fab_2.setOnClickListener { view ->
            val builder = AlertDialog.Builder(this@BusquedaActivity)
            // Get the layout inflater
            val inflater = this@BusquedaActivity.getLayoutInflater()
            builder.setTitle(getString(R.string.filtro))
            // Inflate and set the layout for the dialog

            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.filtro_dialog, null))



            val dialog =builder.create()
            dialog.show()
        }
        rv_recycler_view2.layoutManager=LinearLayoutManager(this@BusquedaActivity,LinearLayout.VERTICAL,false)

    }

    private fun BuscarEquipamiento() {

        var equipos = ArrayList<EquipamientoItem>()
        val bbddsqlite = BBDDSQLite(this@BusquedaActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT t1.*, t2.nombrefamilia as nombrefamilia, t4.nombreubicacion as nombreubicacion FROM equipamientos as t1, familias as t2,  ubicaciones as t4 WHERE t1.id_familia = t2.id  AND t1.id_ubicacion = t4.id", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                }
                do {
                    val id_equipamiento = cusrsor.getString(cusrsor.getColumnIndex("n_serie"))
                    val familia = cusrsor.getString(cusrsor.getColumnIndex("nombrefamilia"))
                    val ubicacion = cusrsor.getString(cusrsor.getColumnIndex("nombreubicacion"))
                    val fecha = cusrsor.getString(cusrsor.getColumnIndex("fecha_proxima_revision"))
                    equipos.add(EquipamientoItem(id_equipamiento, familia, ubicacion, fecha))
                } while (cusrsor.moveToNext())

                db.close()
                rv_recycler_view2.adapter=MyAdapterCards(this@BusquedaActivity, equipos)
            }else{
                rv_recycler_view2.adapter=MyAdapterEmpty(getString(R.string.nofiltros))
            }
        }
    }


    override fun onBackPressed() {
        finish()
    }
}
