package com.example.revisionequipamiento

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.example.revisionequipamiento.Fragments.NoEnviado
import com.example.revisionequipamiento.Fragments.PmaRevisiones
import com.example.revisionequipamiento.Fragments.Reparacion
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
        val pagerAdapter = PagerAdapter(getSupportFragmentManager(), this@Principal)

        viewpager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(viewpager)

        for (i in 0 until tab_layout.tabCount) {
            val tab = tab_layout.getTabAt(i)
            tab!!.customView = pagerAdapter.getTabView(i)
        }

        //Animaciones
        show_fab_1 = AnimationUtils.loadAnimation(application, R.anim.fab1_show)
        hide_fab_1 = AnimationUtils.loadAnimation(application, R.anim.fab1_hide)
        show_fab_2 = AnimationUtils.loadAnimation(application, R.anim.fab2_show)
        hide_fab_2 = AnimationUtils.loadAnimation(application, R.anim.fab2_hide)
        show_fab_3 = AnimationUtils.loadAnimation(application, R.anim.fab3_show)
        hide_fab_3 = AnimationUtils.loadAnimation(application, R.anim.fab3_hide)


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
        fab_2.setOnClickListener(View.OnClickListener {ActualizarBD()})
        fab_3.setOnClickListener(View.OnClickListener {CerrarSesion()})

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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
            val pagerAdapter = PagerAdapter(getSupportFragmentManager(), this@Principal)
            viewpager.adapter = pagerAdapter
            tab_layout.setupWithViewPager(viewpager)

            for (i in 0 until tab_layout.tabCount) {
                val tab = tab_layout.getTabAt(i)
                tab!!.customView = pagerAdapter.getTabView(i)
            }
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
            OneSignal.sendTag("user_id", "0")
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
        Toast.makeText(this,"Todo Actualizado", Toast.LENGTH_SHORT).show()
    }

    internal inner class PagerAdapter(fm: FragmentManager, var context: Context) : FragmentPagerAdapter(fm) {

        var tabTitles = arrayOf("Pma. Revisiones", "Reparación", "No Enviado")

        override fun getCount(): Int {
            return tabTitles.size
        }

        override fun getItem(position: Int): Fragment? {

            when (position) {
                0 -> return PmaRevisiones()
                1 -> return Reparacion()
                2 -> return NoEnviado()
            }

            return null
        }


        override fun getPageTitle(position: Int): CharSequence {
            return tabTitles[position]
        }

        fun getTabView(position: Int): View {
            val tab = LayoutInflater.from(this@Principal).inflate(R.layout.custom_tab, null)
            val tv = tab.findViewById(R.id.custom_text) as TextView
            tv.text = tabTitles[position]
            return tab
        }
    }
}

