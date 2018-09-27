package com.example.revisionequipamiento

import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.revisionequipamiento.Files.BBDDSQLite
import kotlinx.android.synthetic.main.activity_preguntas.*


class PreguntasActivity : AppCompatActivity() {
    var infoFamilia : String? = null
    var familia : String= ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preguntas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        familia = intent.getStringExtra("familia")
        Nfamilias(familia,View(this@PreguntasActivity))
        siguienteP_bt.setOnClickListener{
            val int = Intent(this@PreguntasActivity, DatosActivity::class.java)
            startActivity(int)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_info, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.getItemId()) {
            R.id.action_info -> {
                val builder = AlertDialog.Builder(this@PreguntasActivity)
                val info = getString(R.string.informacion)
                builder.setTitle("$info $familia")
                builder.setMessage("$infoFamilia")

                val dialog =builder.create()
                dialog.show()

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

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@PreguntasActivity)
        builder.setTitle(getString(R.string.salirNoGuardar))
        builder.setMessage(getString(R.string.salirNoGuardarInfo))
        builder.setPositiveButton(getString(R.string.aceptar)) {
            _, _ ->
            finish()
        }
        builder.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
            dialog.dismiss()
            }

        val dialog =builder.create()
        dialog.show()
    }
}
