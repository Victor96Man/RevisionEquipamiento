package com.example.revisionequipamiento

import android.content.Intent
import android.database.Cursor
import android.os.AsyncTask
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import com.onesignal.OneSignal

import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.fragment_main.view.*
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
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        setSupportActionBar(toolbar)
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
        OneSignal.sendTag("user_id", devuelveId())

        //Animaciones
        show_fab_1 = AnimationUtils.loadAnimation(application, R.anim.fab1_show)
        hide_fab_1 = AnimationUtils.loadAnimation(application, R.anim.fab1_hide)
        show_fab_2 = AnimationUtils.loadAnimation(application, R.anim.fab2_show)
        hide_fab_2 = AnimationUtils.loadAnimation(application, R.anim.fab2_hide)
        show_fab_3 = AnimationUtils.loadAnimation(application, R.anim.fab3_show)
        hide_fab_3 = AnimationUtils.loadAnimation(application, R.anim.fab3_hide)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            rootView.section_label.text = ""
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
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

    fun devuelveId():String {
        //consulta a la bbdd
        var id= ""
        val bbddsqlite = BBDDSQLite(this@Principal)
        val db = bbddsqlite.writableDatabase
        val cusrsor : Cursor
        cusrsor = db.rawQuery("SELECT id FROM usuarios", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    id = cusrsor.getString(cusrsor.getColumnIndex("id"))

                }
            }
        }
        db.close()
        return id
    }
}

