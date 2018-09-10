package com.example.revisionequipamiento

import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_equipamineto.*
import kotlinx.android.synthetic.main.content_equipamineto.*

class EquipaminetoActivity : AppCompatActivity() {

    var familia :String=""
    var marca :String=""
    var ubicacion :String=""
    var zona :String=""
    var modelo :String=""
    var fecha_compra :String=""
    var fecha_puesta_funcionamiento :String=""
    var fecha_proxima_revision :String=""
    var fecha_revision :String=""
    var fecha_caducidad :String=""
    var fecha_baja :String=""
    var referencia_normativa :String=""
    var estado :Int= 0
    var id_remplaza :String=""
    var trabajador :String=""
    var bitacora :String=""
    var situacion :String= ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipamineto)
        setSupportActionBar(toolbar)
        val n_serie = intent.getStringExtra("n_serie")
        supportActionBar?.title= n_serie
        buscarEquipamineto(n_serie)
        eq_familia_tx.text = familia
        fab.setOnClickListener { view ->
        }
    }

    fun buscarEquipamineto(n_serie: String?) {
        val bbddsqlite = BBDDSQLite(this@EquipaminetoActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT t1.*, t2.nombrefamilia as nombrefamilia, t3.nombremarca as nombremarca, t4.nombreubicacion as nombreubicacion, t5.nombrezona as nombrezona, t6.nombretrabajador as nombretrabajador FROM equipamientos as t1, familias as t2, marcas as t3, ubicaciones as t4, zonas as t5, trabajadores as t6 WHERE t1.id_familia = t2.id AND t1.id_marca = t3.id AND t1.id_ubicacion = t4.id AND t1.id_zona = t5.id AND t1.id_trabajador = t6.id AND t1.n_serie= '${n_serie}'", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    familia = cusrsor.getString(cusrsor.getColumnIndex("nombrefamilia"))
                    marca = cusrsor.getString(cusrsor.getColumnIndex("nombremarca"))
                    ubicacion = cusrsor.getString(cusrsor.getColumnIndex("nombreubicacion"))
                    zona = cusrsor.getString(cusrsor.getColumnIndex("nombrezona"))
                    modelo = cusrsor.getString(cusrsor.getColumnIndex("modelo"))
                    fecha_compra = cusrsor.getString(cusrsor.getColumnIndex("fecha_compra"))
                    fecha_puesta_funcionamiento = cusrsor.getString(cusrsor.getColumnIndex("fecha_puesta_funcionamiento"))
                    fecha_proxima_revision = cusrsor.getString(cusrsor.getColumnIndex("fecha_proxima_revision"))
                    fecha_revision = cusrsor.getString(cusrsor.getColumnIndex("fecha_revision"))
                    fecha_caducidad = cusrsor.getString(cusrsor.getColumnIndex("fecha_caducidad"))
                    fecha_baja = cusrsor.getString(cusrsor.getColumnIndex("fecha_baja"))
                    referencia_normativa = cusrsor.getString(cusrsor.getColumnIndex("referencia_normativa"))
                    estado = cusrsor.getInt(cusrsor.getColumnIndex("estado"))
                    id_remplaza = cusrsor.getString(cusrsor.getColumnIndex("id_serie_reemplaza"))
                    trabajador = cusrsor.getString(cusrsor.getColumnIndex("nombretrabajador"))
                    bitacora = cusrsor.getString(cusrsor.getColumnIndex("bitacora"))
                    situacion = cusrsor.getString(cusrsor.getColumnIndex("situacion"))
                    db.close()
                }else{
                    Toast.makeText(this@EquipaminetoActivity,getString(R.string.errorBD),Toast.LENGTH_SHORT).show()
                    finish()
                }
            }else{
                Toast.makeText(this@EquipaminetoActivity,getString(R.string.errorBD),Toast.LENGTH_SHORT).show()
                finish()
            }
        }else{
            Toast.makeText(this@EquipaminetoActivity,getString(R.string.errorBD),Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
