package com.example.revisionequipamiento

import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.example.revisionequipamiento.Adapter.MyAdapterCards
import com.example.revisionequipamiento.Clases.EquipamientoItem
import kotlinx.android.synthetic.main.activity_busqueda.*
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.revisionequipamiento.Adapter.MyAdapterEmpty
import com.example.revisionequipamiento.Files.BBDDSQLite
import kotlinx.android.synthetic.main.filtro_dialog.*
import android.content.DialogInterface




class BusquedaActivity : AppCompatActivity() {

    var nserie :String?= null
    var familia :String?= null
    var zona :String?= null
    var ubicacion :String?= null
    var trabajador :String?= null
    var marca :String?= null
    var whereNserie :String=""
    var whereFamilia :String=""
    var whereZona :String=""
    var whereUbicacion :String=""
    var whereTrabajador :String=""
    var whereMarca :String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        BuscarEquipamiento()

        fab_2.setOnClickListener { _ ->
            fab_2.isEnabled=false
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.filtro_dialog, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setTitle(getString(R.string.filtro))
            //show dialog
            val  mAlertDialog = mBuilder.show()
            mAlertDialog.setOnCancelListener{
                fab_2.isEnabled=true
            }
            //---------------------------------------SPINNER--FAMILIA----------------------------------------------------
            mAlertDialog.flt_familia_spnr.adapter = ArrayAdapter(this@BusquedaActivity, android.R.layout.simple_spinner_item, BuscarFamilias())

            //---------------------------------------SPINNER--ZONA----------------------------------------------------
            mAlertDialog.flt_zona_spnr.adapter = ArrayAdapter(this@BusquedaActivity, android.R.layout.simple_spinner_item, BuscarZonas())
            mAlertDialog.flt_zona_spnr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    mAlertDialog.flt_ubicacion_spnr.adapter = ArrayAdapter(this@BusquedaActivity, android.R.layout.simple_spinner_item, BuscarUbicaciones(mAlertDialog.flt_zona_spnr.selectedItem.toString()))
                }
            }
            //---------------------------------------SPINNER--UBICACION----------------------------------------------------
            mAlertDialog.flt_ubicacion_spnr.adapter = ArrayAdapter(this@BusquedaActivity, android.R.layout.simple_spinner_item, BuscarUbicaciones(mAlertDialog.flt_zona_spnr.selectedItem.toString()))
            mAlertDialog.flt_ubicacion_spnr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mAlertDialog.flt_trabajador_spnr.adapter = ArrayAdapter(this@BusquedaActivity, android.R.layout.simple_spinner_item, BuscarTrabajadores(mAlertDialog.flt_ubicacion_spnr.selectedItem.toString()))
                }
            }
            //---------------------------------------SPINNER--TRABAJADOR----------------------------------------------------
            mAlertDialog.flt_trabajador_spnr.adapter = ArrayAdapter(this@BusquedaActivity, android.R.layout.simple_spinner_item, BuscarTrabajadores(mAlertDialog.flt_ubicacion_spnr.selectedItem.toString()))
            //---------------------------------------SPINNER--MARCA----------------------------------------------------
            mAlertDialog.flt_marca_spnr.adapter = ArrayAdapter(this@BusquedaActivity, android.R.layout.simple_spinner_item, BuscarMarcas())
            //--------------------------------------------APLICAR--------------------------------------------------
            mAlertDialog.flt_aplicar_bt.setOnClickListener{
                nserie= mAlertDialog.flt_nserie_edit.text.toString()
                familia = mAlertDialog.flt_familia_spnr.selectedItem.toString()
                zona = mAlertDialog.flt_zona_spnr.selectedItem.toString()
                ubicacion = mAlertDialog.flt_ubicacion_spnr.selectedItem.toString()
                trabajador = mAlertDialog.flt_trabajador_spnr.selectedItem.toString()
                marca = mAlertDialog.flt_marca_spnr.selectedItem.toString()
                BuscarEquipamiento()
                mAlertDialog.dismiss()
            }
            mAlertDialog.flt_cancelar_bt.setOnClickListener{
                mAlertDialog.dismiss()
                fab_2.isEnabled=true
            }
        }

        rv_recycler_view2.layoutManager=LinearLayoutManager(this@BusquedaActivity,LinearLayout.VERTICAL,false)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun BuscarMarcas():ArrayList<String> {
        val bbddsqlite = BBDDSQLite(this@BusquedaActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        val list: ArrayList<String> = ArrayList()
        cusrsor = db.rawQuery("SELECT *  FROM  marcas ", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    list.add(getString(R.string.spnr_marca))
                }
                do {
                    list.add(cusrsor.getString(cusrsor.getColumnIndex("nombremarca")))
                } while (cusrsor.moveToNext())

                db.close()
            }
        }

        return  list
    }

    private fun BuscarTrabajadores(ubicacion: String):ArrayList<String> {
        var filtroUbicacion :String=""
        val bbddsqlite = BBDDSQLite(this@BusquedaActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        val list: ArrayList<String> = ArrayList()
        if(ubicacion!=getString(R.string.spnr_ubicacion)){
            filtroUbicacion= ", ubicaciones as t2 WHERE t1.id_ubicacion=t2.id AND t2.nombreubicacion='$ubicacion' "
        }
        cusrsor = db.rawQuery("SELECT t1.nombretrabajador as nombretrabajador FROM trabajadores as t1 $filtroUbicacion", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    list.add(getString(R.string.spnr_trabajador))
                }
                do {
                    list.add(cusrsor.getString(cusrsor.getColumnIndex("nombretrabajador")))
                } while (cusrsor.moveToNext())

                db.close()
            }
        }

        return  list
    }

    private fun BuscarUbicaciones(zona :String):ArrayList<String> {
        var filtroZonas :String=""
        val bbddsqlite = BBDDSQLite(this@BusquedaActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        val list: ArrayList<String> = ArrayList()
        if(zona!=getString(R.string.spnr_zona)){
            filtroZonas= ", zonas as t2 WHERE t1.id_zona=t2.id AND t2.nombrezona='$zona'"
        }
        cusrsor = db.rawQuery("SELECT t1.nombreubicacion as nombreubicacion FROM ubicaciones as t1 $filtroZonas", null)
        if (cusrsor != null) {

            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    list.add(getString(R.string.spnr_ubicacion))
                }
                do {
                    list.add(cusrsor.getString(cusrsor.getColumnIndex("nombreubicacion")))
                } while (cusrsor.moveToNext())

                db.close()
            }
        }
        return  list
    }

    private fun BuscarZonas():ArrayList<String> {
        val bbddsqlite = BBDDSQLite(this@BusquedaActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor

        val list: ArrayList<String> = ArrayList()
        cusrsor = db.rawQuery("SELECT *  FROM  zonas", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    list.add(getString(R.string.spnr_zona))
                }
                do {
                    list.add(cusrsor.getString(cusrsor.getColumnIndex("nombrezona")))
                } while (cusrsor.moveToNext())

                db.close()
            }
        }

        return  list
    }

    private fun BuscarFamilias() :ArrayList<String> {
        val bbddsqlite = BBDDSQLite(this@BusquedaActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor

        val list: ArrayList<String> = ArrayList()
        cusrsor = db.rawQuery("SELECT *  FROM  familias", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    list.add(getString(R.string.spnr_familia))
                }
                do {
                    list.add(cusrsor.getString(cusrsor.getColumnIndex("nombrefamilia")))
                } while (cusrsor.moveToNext())

                db.close()
            }
        }

        return  list
    }

    private fun BuscarEquipamiento() {

        var equipos = ArrayList<EquipamientoItem>()
        val bbddsqlite = BBDDSQLite(this@BusquedaActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        if(nserie!=null && nserie!=""){
            whereNserie = "AND t1.n_serie LIKE '%$nserie%'"
        }

        if(familia!=null && familia!=getString(R.string.spnr_familia)){
            whereFamilia = "AND t2.nombrefamilia = '$familia'"
        }

        if(zona!=null && zona!=getString(R.string.spnr_zona)){
            whereZona = "AND t3.nombrezona = '$zona'"
        }

        if(ubicacion!=null && ubicacion!=getString(R.string.spnr_ubicacion)){
            whereUbicacion = "AND t4.nombreubicacion = '$ubicacion'"
        }

        if(trabajador!=null && trabajador!=getString(R.string.spnr_trabajador)){
            whereTrabajador = "AND (SELECT nombretrabajador FROM trabajadores WHERE id=t1.id_trabajador) = '$trabajador'"
        }

        if(marca!=null && marca!=getString(R.string.spnr_marca)){
            whereMarca = "AND t6.nombremarca = '$marca'"
        }
        cusrsor = db.rawQuery("SELECT t1.*, t2.nombrefamilia as nombrefamilia, t4.nombreubicacion as nombreubicacion, (SELECT nombretrabajador FROM trabajadores WHERE id=t1.id_trabajador) as nombretrabajador FROM equipamientos as t1, familias as t2, zonas as t3, ubicaciones as t4, marcas as t6 WHERE t1.id_familia = t2.id AND t1.id_zona = t3.id AND t1.id_ubicacion = t4.id AND t1.id_marca = t6.id $whereNserie $whereFamilia $whereZona $whereUbicacion $whereTrabajador $whereMarca", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                }
                do {
                    val id_equipamiento = cusrsor.getString(cusrsor.getColumnIndex("n_serie"))
                    val familia = cusrsor.getString(cusrsor.getColumnIndex("nombrefamilia"))
                    val ubicacion = cusrsor.getString(cusrsor.getColumnIndex("nombreubicacion"))
                    val fecha = cusrsor.getString(cusrsor.getColumnIndex("fecha_proxima_revision"))
                    val trabajador  = cusrsor.getString(cusrsor.getColumnIndex("nombretrabajador"))
                    equipos.add(EquipamientoItem(id_equipamiento, familia, ubicacion, fecha, trabajador))
                } while (cusrsor.moveToNext())

                db.close()
                rv_recycler_view2.adapter=MyAdapterCards(this@BusquedaActivity, equipos)
            }else{
                rv_recycler_view2.adapter=MyAdapterEmpty(getString(R.string.nofiltros))
            }
            whereNserie=""
            whereFamilia=""
            whereZona=""
            whereUbicacion=""
            whereTrabajador=""
            whereMarca=""
        }
    }


    override fun onBackPressed() {
        finish()
    }
}
