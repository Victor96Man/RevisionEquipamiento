package com.example.revisionequipamiento

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AlertDialog
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.example.revisionequipamiento.Files.BBDDSQLite
import com.example.revisionequipamiento.Files.ParseoFile
import com.example.revisionequipamiento.Fragments.NoEnviado
import com.example.revisionequipamiento.Fragments.PmaRevisiones
import com.example.revisionequipamiento.Fragments.Reparacion
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.frame_fab.*
import java.net.HttpURLConnection
import java.net.URL

class PrincipalActivity : AppCompatActivity() {
    private var FAB_Status = false
    var show_fab_1 : Animation? = null
    var hide_fab_1 : Animation? = null
    var show_fab_2 : Animation? = null
    var hide_fab_2 : Animation? = null
    var show_fab_3 : Animation? = null
    var hide_fab_3 : Animation? = null
    var username :String=""
    var contrasena :String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        getSupportActionBar()!!.setElevation(0F)
        val pagerAdapter = PagerAdapter(getSupportFragmentManager(), this@PrincipalActivity)

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

        fab_1.setOnClickListener{
            fab_1.isEnabled=false
            BuscarEquipa()
        }
        fab_2.setOnClickListener{
            ActualizarBD()
        }
        fab_3.setOnClickListener{
            fab_3.isEnabled=false
            CerrarSesion()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_settings -> {
                startActivity(Intent(this@PrincipalActivity,AjustesActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class AsyncTaskHandleJSON2(): AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            MyprogressBar2.visibility = View.VISIBLE
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
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
            ParseoFile(result, this@PrincipalActivity,1)
            val pagerAdapter = PagerAdapter(getSupportFragmentManager(), this@PrincipalActivity)
            viewpager.adapter = pagerAdapter
            tab_layout.setupWithViewPager(viewpager)

            for (i in 0 until tab_layout.tabCount) {
                val tab = tab_layout.getTabAt(i)
                tab!!.customView = pagerAdapter.getTabView(i)
            }
            MyprogressBar2.visibility = View.INVISIBLE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            Toast.makeText(this@PrincipalActivity,getString(R.string.actualizar), Toast.LENGTH_SHORT).show()
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
        val builder = AlertDialog.Builder(this@PrincipalActivity)
        builder.setTitle(getString(R.string.cerrarSesion))
        builder.setMessage(getString(R.string.cerrarSesionInfo))
        builder.setPositiveButton(getString(R.string.aceptar)) {
            _, _ ->
            val bbddsqlite = BBDDSQLite(this)
            val db = bbddsqlite.writableDatabase
            val tables = arrayOf<String>("usuarios", "fotos","marcas","zonas","trabajadores","usuariosZonas","familias","equipamientos","revisiones","ubicaciones")
            for (table in tables) {
                db.delete(table, null, null)
            }
            db.close()
            OneSignal.sendTag(getString(R.string.user_id), "0")
            startActivity(Intent(applicationContext,SplashScreenActivity::class.java))
            finish()
        }
        builder.setNegativeButton(getString(R.string.cancelar)) { _,_ ->
            fab_3.isEnabled=true
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.setOnCancelListener{

            fab_3.isEnabled=true
        }
    }

    fun ActualizarBD() {
        var cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
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
            val tables = arrayOf<String>("usuarios", "marcas", "zonas", "trabajadores", "usuariosZonas", "familias", "equipamientos", "ubicaciones")
            for (table in tables) {
                db.delete(table, null, null)
            }
            db.close()
            AsyncTaskHandleJSON2().execute("${getString(R.string.URL)}${getString(R.string.URLtodo)}$username/$contrasena")
        }else{
            val builder = AlertDialog.Builder(this@PrincipalActivity)
            builder.setTitle(getString(R.string.noInternet))
            builder.setMessage(getString(R.string.noInternetInfo))
            builder.setNeutralButton(getString(R.string.aceptar)){_,_ ->

            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    fun  BuscarEquipa(){
        val intent = Intent(this@PrincipalActivity,BusquedaActivity::class.java)
        startActivity(intent)
        fab_1.isEnabled=true
    }

    internal inner class PagerAdapter(fm: FragmentManager, var context: Context) : FragmentPagerAdapter(fm) {

        var tabTitles = arrayOf(getString(R.string.tab_text_1), getString(R.string.tab_text_2), getString(R.string.tab_text_3))

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
            val tab = LayoutInflater.from(this@PrincipalActivity).inflate(R.layout.custom_tab, null)
            val tv = tab.findViewById(R.id.custom_text) as TextView
            tv.text = tabTitles[position]
            return tab
        }
    }

    override fun onRestart() {
        super.onRestart()
        val pagerAdapter = PagerAdapter(getSupportFragmentManager(), this@PrincipalActivity)
        viewpager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(viewpager)

        for (i in 0 until tab_layout.tabCount) {
            val tab = tab_layout.getTabAt(i)
            tab!!.customView = pagerAdapter.getTabView(i)
        }
    }
}

