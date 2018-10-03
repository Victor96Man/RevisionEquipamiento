package com.example.revisionequipamiento

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.SnapHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.*
import com.example.revisionequipamiento.Adapter.PostsAdapter
import com.example.revisionequipamiento.Clases.RevisionObjeto
import com.example.revisionequipamiento.Files.BBDDSQLite
import com.example.revisionequipamiento.Files.EnviarRevi
import com.example.revisionequipamiento.Files.ParseoFile
import kotlinx.android.synthetic.main.activity_datos.*
import kotlinx.android.synthetic.main.horizontal_scroll_card.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class DatosActivity : AppCompatActivity(), PostsAdapter.CallbackInterface{
    private val GALLERY = 1
    private val CAMERA = 2
    var imagen :ImageButton?= null
    var MODO : String=""
    var familia :String=""
    var username :String=""
    var contrasena :String=""
    val id : Int=0
    var or : RevisionObjeto = RevisionObjeto.getObjetoRevision(id)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        MODO = intent.getStringExtra("MODO")
        familia = intent.getStringExtra("familia")

        //SPINNER
        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.estados_sp))
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        dt_estado_sp.adapter = spinnerArrayAdapter


        var dt_objeciones_edit = findViewById<EditText>(R.id.dt_objeciones_edit)

        if(MODO == "2"){
            MostrarDatos()
        }

        val posts: ArrayList<String> = ArrayList()

        posts.add("")
        posts.add("")
        posts.add("")
        posts.add("")
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = PostsAdapter(this@DatosActivity,posts)

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        dt_guardar_bt.setOnClickListener{
            guardar()

            val builder = android.support.v7.app.AlertDialog.Builder(this@DatosActivity)
            builder.setTitle(getString(R.string.enviarTitulo))
            builder.setMessage(getString(R.string.enviarInfo))
            builder.setPositiveButton(getString(R.string.aceptar)) {
                _, _ ->
                var cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var networkInfo = cm.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected) {
                    val urlInsertRev = "${getString(R.string.URL)}${getString(R.string.URLinsert)}"
                    EnviarRevi(or.equipamiento, urlInsertRev, this@DatosActivity)
                    or.volveranull()
                    actualizarBD()


                }else{
                    val builder = android.support.v7.app.AlertDialog.Builder(this@DatosActivity)
                    builder.setTitle(getString(R.string.noInternet))
                    builder.setMessage(getString(R.string.noInternetInfo))
                    builder.setNeutralButton(getString(R.string.aceptar)){_,_ ->

                    }
                    val dialog: android.support.v7.app.AlertDialog = builder.create()
                    dialog.show()
                }

            }
            builder.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
                dialog.dismiss()
            }
            val dialog =builder.create()
            dialog.show()

        }

        dt_UsuarioFirma_bt.setOnClickListener {
            dt_UsuarioFirma_bt.isEnabled=false
            val i = Intent(this@DatosActivity, FirmaActivity::class.java)
            startActivity(i)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_exit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.salirmenu -> {
                val builder = android.support.v7.app.AlertDialog.Builder(this@DatosActivity)
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
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    fun MostrarDatos(){
        dt_estado_sp.setSelection(or.estado+1)
        dt_peticiones_edit.setText(or.peticiones)
        dt_objeciones_edit.setText(or.objecione)
    }

    fun guardar(){
        if (dt_estado_sp.selectedItemPosition!=0){
            recuperarDatos()
            val bbddsqlite = BBDDSQLite(this@DatosActivity)
            bbddsqlite.insertRevision(or)
            bbddsqlite.close()
        }else{
            val errorText :TextView = dt_estado_sp.getSelectedView() as TextView
            errorText.setError("")
            errorText.setTextColor(Color.RED)
            errorText.setText(getString(R.string.err_NoEstado))
            Toast.makeText(this@DatosActivity,getString(R.string.err_NoEstado),Toast.LENGTH_SHORT).show()
        }
    }

    fun fechaHoy():String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        val fecha = dateFormat.format(date)
        return fecha
    }

    fun recuperarDatos(){
        or.estado = dt_estado_sp.selectedItemPosition -1
        or.usuario = idUsuario()
        or.setfR(fechaHoy())
        or.peticiones = dt_peticiones_edit.text.toString()
        or.objecione = dt_objeciones_edit.text.toString()
        //Toast.makeText(this@DatosActivity,or.toString(),Toast.LENGTH_LONG).show()

    }

    private fun idUsuario(): Int {
        val bbddsqlite = BBDDSQLite(this)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        var id = 0
        cusrsor = db.rawQuery("SELECT id FROM usuarios", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    id = cusrsor.getInt(cusrsor.getColumnIndex("id"))
                }
            }
        }
        db.close()
        return id

    }

    override fun onHandleSelection(imagen2: ImageButton) {
        imagen=imagen2
       showPictureDialog()
    }

    fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this@DatosActivity)
        pictureDialog.setTitle(getString(R.string.seleccionFoto))
        val pictureDialogItems = arrayOf(getString(R.string.seleccionFotoGaleria), getString(R.string.seleccionFotoCamara))
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

       startActivityForResult(galleryIntent, GALLERY)
    }

    fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    Toast.makeText(this@DatosActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    imagen!!.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@DatosActivity, "Algo a Fallado!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            if (data != null) {
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                imagen!!.setImageBitmap(thumbnail)
                saveImage(thumbnail)
            }
        }
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/RevisionesEmproacsa"
    }

    fun actualizarBD(){
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
        val tables = arrayOf<String>( "marcas", "zonas", "trabajadores", "usuariosZonas", "familias", "equipamientos", "ubicaciones")
        for (table in tables) {
            db.delete(table, null, null)
        }
        db.close()
        AsyncTaskHandleJSON2().execute("${getString(R.string.URL)}${getString(R.string.URLtodo)}$username/$contrasena")
    }

    inner class AsyncTaskHandleJSON2(): AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

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
            ParseoFile(result, this@DatosActivity,2)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            or.volveranull()
            finish()
        }
    }

    override fun onBackPressed() {
        recuperarDatos()
        val int = Intent(this@DatosActivity, PreguntasActivity::class.java)
        int.putExtra("familia", familia)
        int.putExtra("MODO", "2")
        int.putExtra("n_serie", or.equipamiento )
        startActivity(int)
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        dt_UsuarioFirma_bt.isEnabled=true
    }
}

