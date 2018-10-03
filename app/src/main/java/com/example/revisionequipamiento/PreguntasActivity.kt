package com.example.revisionequipamiento

import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.revisionequipamiento.Clases.RevisionObjeto
import com.example.revisionequipamiento.Files.BBDDSQLite
import kotlinx.android.synthetic.main.activity_preguntas.*


class PreguntasActivity : AppCompatActivity() {
    var infoFamilia : String? = null
    var familia : String= ""
    var MODO : String= "1"
    var n_serie : String= ""
    var id : Int= 0
    var or : RevisionObjeto= RevisionObjeto.getObjetoRevision(id)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preguntas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        familia = intent.getStringExtra("familia")
        MODO = intent.getStringExtra("MODO")
        n_serie = intent.getStringExtra("n_serie")
        Nfamilias(familia,View(this@PreguntasActivity))
        if(MODO=="2"){
            volcarDatos()
            MostrarDatos()
        }
        siguienteP_bt.setOnClickListener{
            siguienteP_bt.isEnabled=false
            recogerDatos()
            val int = Intent(this@PreguntasActivity, DatosActivity::class.java)
            if(MODO=="2"){
                int.putExtra("MODO", "2")
            }else{
                int.putExtra("MODO","1")
            }
            int.putExtra("familia", familia)
            startActivity(int)
            finish()
        }
    }
    fun volcarDatos(){
        val bbddsqlite = BBDDSQLite(this@PreguntasActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT * from revisiones where id_equipamiento = '${n_serie}'",null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    or.equipamiento = n_serie
                    or.enviado= cusrsor.getInt(cusrsor.getColumnIndex("enviado"))
                    or.vp1 = cusrsor.getInt(cusrsor.getColumnIndex("vp1"))
                    or.vp2 = cusrsor.getInt(cusrsor.getColumnIndex("vp2"))
                    or.vp3 = cusrsor.getInt(cusrsor.getColumnIndex("vp3"))
                    or.vp4 = cusrsor.getInt(cusrsor.getColumnIndex("vp4"))
                    or.vp5 = cusrsor.getInt(cusrsor.getColumnIndex("vp5"))
                    or.vp6 = cusrsor.getInt(cusrsor.getColumnIndex("vp6"))
                    or.vp7 = cusrsor.getInt(cusrsor.getColumnIndex("vp7"))
                    or.vp8 = cusrsor.getInt(cusrsor.getColumnIndex("vp8"))
                    or.vp9 = cusrsor.getInt(cusrsor.getColumnIndex("vp9"))
                    or.vp10 = cusrsor.getInt(cusrsor.getColumnIndex("vp10"))
                    or.obp1 = cusrsor.getString(cusrsor.getColumnIndex("obp1"))
                    or.obp2 = cusrsor.getString(cusrsor.getColumnIndex("obp2"))
                    or.obp3 = cusrsor.getString(cusrsor.getColumnIndex("obp3"))
                    or.obp4 = cusrsor.getString(cusrsor.getColumnIndex("obp4"))
                    or.obp5 = cusrsor.getString(cusrsor.getColumnIndex("obp5"))
                    or.obp6 = cusrsor.getString(cusrsor.getColumnIndex("obp6"))
                    or.obp7 = cusrsor.getString(cusrsor.getColumnIndex("obp7"))
                    or.obp8 = cusrsor.getString(cusrsor.getColumnIndex("obp8"))
                    or.obp9 = cusrsor.getString(cusrsor.getColumnIndex("obp9"))
                    or.obp10 = cusrsor.getString(cusrsor.getColumnIndex("obp10"))
                    or.firma = cusrsor.getString(cusrsor.getColumnIndex("firma"))
                    or.firmaT = cusrsor.getString(cusrsor.getColumnIndex("firma_trabajador"))
                }
            }
        }
    }

    fun MostrarDatos(){
        pregunta1Obs_edt.setText(or.obp1)
        pregunta2Obs_edt.setText(or.obp2)
        pregunta3Obs_edt.setText(or.obp3)
        pregunta4Obs_edt.setText(or.obp4)
        pregunta5Obs_edt.setText(or.obp5)
        pregunta6Obs_edt.setText(or.obp6)
        pregunta7Obs_edt.setText(or.obp7)
        pregunta8Obs_edt.setText(or.obp8)
        pregunta9Obs_edt.setText(or.obp9)
        pregunta10Obs_edt.setText(or.obp10)
    }

    fun recogerDatos() {
        or.equipamiento = n_serie
        or.enviado=0
        or.obp1 = pregunta1Obs_edt.text.toString()
        or.obp2 = pregunta2Obs_edt.text.toString()
        or.obp3 = pregunta3Obs_edt.text.toString()
        or.obp4 = pregunta4Obs_edt.text.toString()
        or.obp5 = pregunta5Obs_edt.text.toString()
        or.obp6 = pregunta6Obs_edt.text.toString()
        or.obp7 = pregunta7Obs_edt.text.toString()
        or.obp8 = pregunta8Obs_edt.text.toString()
        or.obp9 = pregunta9Obs_edt.text.toString()
        or.obp10 = pregunta10Obs_edt.text.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        item.isEnabled=false
        when (item.getItemId()) {
            R.id.action_info -> {
                val builder = AlertDialog.Builder(this@PreguntasActivity)
                val info = getString(R.string.informacion)
                builder.setTitle("$info $familia")
                builder.setMessage("$infoFamilia")

                val dialog =builder.create()
                dialog.show()
                item.isEnabled=true
            }

            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    fun Nfamilias(familia :String, view : View) {
        val bbddsqlite = BBDDSQLite(this@PreguntasActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT * FROM familias WHERE nombrefamilia='$familia'", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                }
                infoFamilia = cusrsor.getString(cusrsor.getColumnIndex("informacion"))
                val pregunta1 = cusrsor.getString(cusrsor.getColumnIndex("pregunta1"))
                val pregunta2 = cusrsor.getString(cusrsor.getColumnIndex("pregunta2"))
                val pregunta3 = cusrsor.getString(cusrsor.getColumnIndex("pregunta3"))
                val pregunta4 = cusrsor.getString(cusrsor.getColumnIndex("pregunta4"))
                val pregunta5 = cusrsor.getString(cusrsor.getColumnIndex("pregunta5"))
                val pregunta6 = cusrsor.getString(cusrsor.getColumnIndex("pregunta6"))
                val pregunta7 = cusrsor.getString(cusrsor.getColumnIndex("pregunta7"))
                val pregunta8 = cusrsor.getString(cusrsor.getColumnIndex("pregunta8"))
                val pregunta9 = cusrsor.getString(cusrsor.getColumnIndex("pregunta9"))
                val pregunta10 = cusrsor.getString(cusrsor.getColumnIndex("pregunta10"))
                if(pregunta1!=""){
                    pregunta1_txt.text=pregunta1
                    if(pregunta2!=""){
                        pregunta2_txt.text=pregunta2
                        if(pregunta3!=""){
                            pregunta3_txt.text=pregunta3
                            if(pregunta4!=""){
                                pregunta4_txt.text=pregunta4
                                if(pregunta5!=""){
                                    pregunta5_txt.text=pregunta5
                                    if(pregunta6!=""){
                                        pregunta6_txt.text=pregunta6
                                        if(pregunta7!=""){
                                            pregunta7_txt.text=pregunta7
                                            if(pregunta8!=""){
                                                pregunta8_txt.text=pregunta8
                                                if(pregunta9!=""){
                                                    pregunta9_txt.text=pregunta9
                                                    if(pregunta10!=""){
                                                        pregunta10_txt.text=pregunta10
                                                    }else {
                                                        Pregunta10.visibility=View.GONE
                                                    }
                                                }else{
                                                    Pregunta9.visibility=View.GONE
                                                    Pregunta10.visibility=View.GONE
                                                }
                                            }else{
                                                Pregunta8.visibility=View.GONE
                                                Pregunta9.visibility=View.GONE
                                                Pregunta10.visibility=View.GONE
                                            }
                                        }else{
                                            Pregunta7.visibility=View.GONE
                                            Pregunta8.visibility=View.GONE
                                            Pregunta9.visibility=View.GONE
                                            Pregunta10.visibility=View.GONE
                                        }
                                    }else{
                                        Pregunta6.visibility=View.GONE
                                        Pregunta7.visibility=View.GONE
                                        Pregunta8.visibility=View.GONE
                                        Pregunta9.visibility=View.GONE
                                        Pregunta10.visibility=View.GONE
                                    }
                                }else{
                                    Pregunta5.visibility=View.GONE
                                    Pregunta6.visibility=View.GONE
                                    Pregunta7.visibility=View.GONE
                                    Pregunta8.visibility=View.GONE
                                    Pregunta9.visibility=View.GONE
                                    Pregunta10.visibility=View.GONE
                                }
                            }else{
                                Pregunta4.visibility=View.GONE
                                Pregunta5.visibility=View.GONE
                                Pregunta6.visibility=View.GONE
                                Pregunta7.visibility=View.GONE
                                Pregunta8.visibility=View.GONE
                                Pregunta9.visibility=View.GONE
                                Pregunta10.visibility=View.GONE
                            }
                        }else{
                            Pregunta3.visibility=View.GONE
                            Pregunta4.visibility=View.GONE
                            Pregunta5.visibility=View.GONE
                            Pregunta6.visibility=View.GONE
                            Pregunta7.visibility=View.GONE
                            Pregunta8.visibility=View.GONE
                            Pregunta9.visibility=View.GONE
                            Pregunta10.visibility=View.GONE
                        }
                    }else{
                        Pregunta2.visibility=View.GONE
                        Pregunta3.visibility=View.GONE
                        Pregunta4.visibility=View.GONE
                        Pregunta5.visibility=View.GONE
                        Pregunta6.visibility=View.GONE
                        Pregunta7.visibility=View.GONE
                        Pregunta8.visibility=View.GONE
                        Pregunta9.visibility=View.GONE
                        Pregunta10.visibility=View.GONE
                    }
                }else{
                    Pregunta1.visibility=View.GONE
                    Pregunta2.visibility=View.GONE
                    Pregunta3.visibility=View.GONE
                    Pregunta4.visibility=View.GONE
                    Pregunta5.visibility=View.GONE
                    Pregunta6.visibility=View.GONE
                    Pregunta7.visibility=View.GONE
                    Pregunta8.visibility=View.GONE
                    Pregunta9.visibility=View.GONE
                    Pregunta10.visibility=View.GONE
                }

            }
        }
        db.close()
    }

    override fun onRestart() {
        super.onRestart()
        siguienteP_bt.isEnabled=true
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@PreguntasActivity)
        builder.setTitle(getString(R.string.salirNoGuardar))
        builder.setMessage(getString(R.string.salirNoGuardarInfo))
        builder.setPositiveButton(getString(R.string.aceptar)) {
            _, _ ->
            val int = Intent(this@PreguntasActivity, EquipamientoActivity::class.java)
            int.putExtra("n_serie", n_serie)
            startActivity(int)
            or.volveranull()
            finish()
        }
        builder.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
            dialog.dismiss()
            }

        val dialog =builder.create()
        dialog.show()
    }
}
