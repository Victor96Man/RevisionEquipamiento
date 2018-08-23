package com.example.revisionequipamiento

import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_principal.*

class Principal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
        bt_borrar_bd.setOnClickListener{
            val bbddsqlite = BBDDSQLite(this)
            val db = bbddsqlite.writableDatabase
            val tables = arrayOf<String>("usuarios","marcas","zonas","trabajadores","usuariosZonas","familias","equipamientos","revisiones","ubicaciones")
            for (table in tables) {
                db.delete(table, null, null)
            }
            db.close()
        }
    }
}
