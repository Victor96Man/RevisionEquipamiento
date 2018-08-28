package com.example.revisionequipamiento

import android.content.Intent
import android.database.Cursor
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.frame_fab.*
import java.net.HttpURLConnection
import java.net.URL



class Principal : AppCompatActivity() {
    var url="http://emproacsa.mjhudesings.com/api/v1/"
    var username=""
    var contrasena=""
    private var FAB_Status = false
    var show_fab_1 : Animation? = null
    var hide_fab_1 : Animation? = null
    var show_fab_2 : Animation? = null
    var hide_fab_2 : Animation? = null
    var show_fab_3 : Animation? = null
    var hide_fab_3 : Animation? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        show_fab_1 = AnimationUtils.loadAnimation(application, R.anim.fab1_show)
        hide_fab_1 = AnimationUtils.loadAnimation(application, R.anim.fab1_hide)
        show_fab_2 = AnimationUtils.loadAnimation(application, R.anim.fab2_show)
        hide_fab_2 = AnimationUtils.loadAnimation(application, R.anim.fab2_hide)
        show_fab_3 = AnimationUtils.loadAnimation(application, R.anim.fab3_show)
        hide_fab_3 = AnimationUtils.loadAnimation(application, R.anim.fab3_hide)

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
        fab.setOnClickListener {
            if (FAB_Status == false) {
                //Display FAB menu
                expandFAB()
                FAB_Status = true
            } else {
                //Close FAB menu
                hideFAB()
                FAB_Status = false
            }
        }

        fab_1.setOnClickListener(View.OnClickListener {  })
        fab_2.setOnClickListener(View.OnClickListener { ActualizarBD()})
        fab_3.setOnClickListener(View.OnClickListener {CerrarSesion() })

    }
    inner class AsyncTaskHandleJSON2(): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            var text : String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            }finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            ParseoFile(result, this@Principal)
        }
    }

    private fun expandFAB() {

        //Floating Action Button 1
        val layoutParams = fab_1.getLayoutParams() as FrameLayout.LayoutParams
        layoutParams.rightMargin += (fab_1.getWidth() * 1.7).toInt()
        layoutParams.bottomMargin += (fab_1.getHeight() * 0.25).toInt()
        fab_1.setLayoutParams(layoutParams)
        fab_1.startAnimation(show_fab_1)
        fab_1.setClickable(true)

        //Floating Action Button 2
        val layoutParams2 = fab_2.getLayoutParams() as FrameLayout.LayoutParams
        layoutParams2.rightMargin += (fab_2.getWidth() * 1.5).toInt()
        layoutParams2.bottomMargin += (fab_2.getHeight() * 1.5).toInt()
        fab_2.setLayoutParams(layoutParams2)
        fab_2.startAnimation(show_fab_2)
        fab_2.setClickable(true)

        //Floating Action Button 3
        val layoutParams3 = fab_3.getLayoutParams() as FrameLayout.LayoutParams
        layoutParams3.rightMargin += (fab_3.getWidth() * 0.25).toInt()
        layoutParams3.bottomMargin += (fab_3.getHeight() * 1.7).toInt()
        fab_3.setLayoutParams(layoutParams3)
        fab_3.startAnimation(show_fab_3)
        fab_3.setClickable(true)
    }


    private fun hideFAB() {

        //Floating Action Button 1
        val layoutParams = fab_1.getLayoutParams() as FrameLayout.LayoutParams
        layoutParams.rightMargin -= (fab_1.getWidth() * 1.7).toInt()
        layoutParams.bottomMargin -= (fab_1.getHeight() * 0.25).toInt()
        fab_1.setLayoutParams(layoutParams)
        fab_1.startAnimation(hide_fab_1)
        fab_1.setClickable(false)

        //Floating Action Button 2
        val layoutParams2 = fab_2.getLayoutParams() as FrameLayout.LayoutParams
        layoutParams2.rightMargin -= (fab_2.getWidth() * 1.5).toInt()
        layoutParams2.bottomMargin -= (fab_2.getHeight() * 1.5).toInt()
        fab_2.setLayoutParams(layoutParams2)
        fab_2.startAnimation(hide_fab_2)
        fab_2.setClickable(false)

        //Floating Action Button 3
        val layoutParams3 = fab_3.getLayoutParams() as FrameLayout.LayoutParams
        layoutParams3.rightMargin -= (fab_3.getWidth() * 0.25).toInt()
        layoutParams3.bottomMargin -= (fab_3.getHeight() * 1.7).toInt()
        fab_3.setLayoutParams(layoutParams3)
        fab_3.startAnimation(hide_fab_3)
        fab_3.setClickable(false)
    }

    fun CerrarSesion(){

        val builder = AlertDialog.Builder(this@Principal)
        builder.setTitle("¿Cerrar Sesión?")
        builder.setMessage("Los datos almacenados en su teléfono se borraran.")
        builder.setPositiveButton("Aceptar") {
            dialog, which ->
            val bbddsqlite = BBDDSQLite(this)
            val db = bbddsqlite.writableDatabase
            val tables = arrayOf<String>("usuarios","marcas","zonas","trabajadores","usuariosZonas","familias","equipamientos","revisiones","ubicaciones")
            for (table in tables) {
                db.delete(table, null, null)
            }
            db.close()
            startActivity(Intent(applicationContext,SplashScreenActivity::class.java))
            finish()
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

        fun ActualizarBD() {
            val bbddsqlite = BBDDSQLite(this)
            val db = bbddsqlite.writableDatabase
            val cusrsor: Cursor
            cusrsor = db.rawQuery("SELECT * FROM usuarios", null)
            if (cusrsor != null) {
                if (cusrsor.count > 0) {
                    if (cusrsor.moveToFirst()) {
                        username = cusrsor.getString(cusrsor.getColumnIndex("username"))
                        contrasena = cusrsor.getString(cusrsor.getColumnIndex("password"))
                    }
                }
                cusrsor.close()
            }
            val tables = arrayOf<String>("usuarios", "marcas", "zonas", "trabajadores", "usuariosZonas", "familias", "equipamientos", "revisiones", "ubicaciones")
            for (table in tables) {
                db.delete(table, null, null)
            }
            db.close()
            AsyncTaskHandleJSON2().execute(url + "todo/$username/$contrasena")
            Toast.makeText(this,"Todo Actualizado",Toast.LENGTH_SHORT).show()
        }
}
