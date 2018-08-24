package com.example.revisionequipamiento

import android.content.Intent
import android.database.Cursor
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_principal.*
import java.net.HttpURLConnection
import java.net.URL

class Principal : AppCompatActivity() {
    var url="http://emproacsa.mjhudesings.com/api/v1/"
    var username=""
    var contrasena=""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()

        bt_borrar_bd.setOnClickListener{


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
            startActivity(Intent(applicationContext,Login::class.java))
            finish()
            }
            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }

        bt_actualizarbd.setOnClickListener{
            val bbddsqlite = BBDDSQLite(this)
            val db = bbddsqlite.writableDatabase
            val tables = arrayOf<String>("marcas","zonas","trabajadores","usuariosZonas","familias","equipamientos","revisiones","ubicaciones")
            for (table in tables) {
                db.delete(table, null, null)
            }
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
            db.close()
            AsyncTaskHandleJSON2().execute(url+"todo/$username/$contrasena")
        }
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
}
