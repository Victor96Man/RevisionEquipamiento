package com.example.revisionequipamiento

import android.content.Intent
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.revisionequipamiento.Files.BBDDSQLite
import com.example.revisionequipamiento.Files.EnviarRevi
import kotlinx.android.synthetic.main.activity_equipamineto.*
import kotlinx.android.synthetic.main.buttons_equipamineto.*
import kotlinx.android.synthetic.main.content_equipamineto.*
import java.net.HttpURLConnection
import java.net.URL

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
    var trabajador : String? =null
    var bitacora :String=""
    var situacion :String= ""
    private var fechas_Status = false
    var down_fechas : Animation? = null
    var up_fechas : Animation? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipamineto)
        setSupportActionBar(toolbar)
        val n_serie = intent.getStringExtra("n_serie")
        supportActionBar?.title = n_serie

        //Animaciones
        down_fechas = AnimationUtils.loadAnimation(application, R.anim.fecha_slide_down)
        up_fechas = AnimationUtils.loadAnimation(application, R.anim.fecha_slide_up)

        buscarEquipamineto(n_serie)
        eq_familia_tx.text = familia
        if(trabajador!=null) {
            eq_trabajador_tx.text = trabajador
        }else{
            eq_trabajador_tx.text = getString(R.string.noTrabajador)
        }
        eq_marca_modelo_tx.text = "$marca / $modelo"
        eq_zona_tx.text = zona
        eq_ubicacion_tx.text = ubicacion
        eq_situacion_tx.text = situacion
        eq_fechaPR_tx.text = fecha_proxima_revision
        eq_fechaR_tx.text = fecha_revision
        eq_fechaC_tx.text = fecha_compra
        eq_fechaPF_tx.text = fecha_puesta_funcionamiento
        eq_fechaCaducidad_tx.text = fecha_caducidad
        eq_fechaBaja_tx.text = fecha_baja
        eq_referenciaN_tx.text = referencia_normativa
        eq_bitacora_tx.text = bitacora

        when(estado){
            0 -> eq_estado_tx.text = getString(R.string.estadoBien)
            1 -> eq_estado_tx.text = getString(R.string.estadoReparacion)
            2 -> eq_estado_tx.text = getString(R.string.estadoBaja)
        }
        eq_idremplaza_tx.text = id_remplaza

        enviarRV_bt.isEnabled = false

        fab.setOnClickListener {
            fab.isEnabled=false
            val int = Intent(this@EquipaminetoActivity, PreguntasActivity::class.java)
            int.putExtra("familia", familia)
            startActivity(int)
        }

        layout_fechas1.setOnClickListener{
            if (fechas_Status == false) {
                //Display FAB menu
                expandFECHA()
                fechas_Status = true
            } else {
                //Close FAB menu
                hideFECHA()
                fechas_Status = false
            }
        }

        if (buscarRevision(n_serie)) {
            enviarRV_bt.isEnabled = true
            fab.setImageResource(R.drawable.ic_mode_edit)
            fab.setOnClickListener {

            }
        }
        enviarRV_bt.setOnClickListener {
            val urlInsert = "${getString(R.string.URL)}${getString(R.string.URLinsert)}"
            EnviarRevi(n_serie, urlInsert, this@EquipaminetoActivity)
        }

        descarga_revi_bt.setOnClickListener{
            val urlDescarga= "${getString(R.string.URL)}${getString(R.string.URLDescargaPDF)}$n_serie"
            AsyncTaskHandleJSON2().execute(urlDescarga)
        }
    }


    private fun hideFECHA() {
       // layout_fechas2.startAnimation(down_fechas)
        layout_fechas2.visibility = View.VISIBLE
    }

    private fun expandFECHA() {
        //layout_fechas2.startAnimation(up_fechas)
        layout_fechas2.visibility = View.GONE
    }

    inner class AsyncTaskHandleJSON2(): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            var text: String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Toast.makeText(this@EquipaminetoActivity,"PDF descargado", Toast.LENGTH_SHORT).show()
        }
    }


    private fun buscarRevision(n_serie: String?): Boolean {
        val bbddsqlite = BBDDSQLite(this@EquipaminetoActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        var revision :Boolean = false
        cusrsor = db.rawQuery("Select * FROM revisiones WHERE revisiones.id_equipamiento= '${n_serie}'", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                }
                revision = true
            }
        }
        return revision
    }

    private fun buscarEquipamineto(n_serie: String?) {
        val bbddsqlite = BBDDSQLite(this@EquipaminetoActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT t1.*, t2.nombrefamilia as nombrefamilia, t3.nombremarca as nombremarca, t4.nombreubicacion as nombreubicacion, t5.nombrezona as nombrezona, (SELECT nombretrabajador FROM trabajadores WHERE id=t1.id_trabajador) as nombretrabajador FROM equipamientos as t1, familias as t2, marcas as t3, ubicaciones as t4, zonas as t5 WHERE t1.id_familia = t2.id AND t1.id_marca = t3.id AND t1.id_ubicacion = t4.id AND t1.id_zona = t5.id AND t1.n_serie= '${n_serie}'", null)
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

    override fun onBackPressed() {
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        fab.isEnabled=true
    }


}
