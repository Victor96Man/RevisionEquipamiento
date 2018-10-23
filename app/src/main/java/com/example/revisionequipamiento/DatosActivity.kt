package com.example.revisionequipamiento

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.SnapHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.example.revisionequipamiento.Adapter.PostsAdapter
import com.example.revisionequipamiento.Clases.Foto
import com.example.revisionequipamiento.Clases.RevisionObjeto
import com.example.revisionequipamiento.Clases.fotoItem
import com.example.revisionequipamiento.Files.BBDDSQLite
import com.example.revisionequipamiento.Files.EnviarRevi
import com.example.revisionequipamiento.Files.ParseoFile
import kotlinx.android.synthetic.main.activity_datos.*
import kotlinx.android.synthetic.main.horizontal_scroll_card.*
import kotlinx.android.synthetic.main.progressbar.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DatosActivity : AppCompatActivity(), PostsAdapter.CallbackInterface{

    private val GALLERY = 1
    private val CAMERA = 2

    var imagen :ImageButton?= null
    var MODO : String=""
    var familia :String=""
    var username :String=""
    var contrasena :String=""
    var positionCameraElement: Int=0
    var positionGalleryElement: Int=0
    var or : RevisionObjeto = RevisionObjeto.getObjetoRevision()

    var tv1: String = ""
    var tv2: String = ""
    var tv3: String = ""
    var tv4: String = ""

    var fotos: ArrayList<Foto> = ArrayList()
    var mCurrentPhotoPath:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        MODO = intent.getStringExtra("MODO")
        familia = intent.getStringExtra("familia")
        val dt_objeciones_edit = findViewById<EditText>(R.id.dt_objeciones_edit)

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //SPINNER
        val spinnerArrayAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item , resources.getStringArray(R.array.estados_sp))
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dt_estado_sp.adapter = spinnerArrayAdapter
        if(MODO == "2"){
            MostrarDatos()
        }else{
            or.fotos = ArrayList<Foto>()
        }
        ComprobarSiFirmado()


        if (dt_objeciones_edit.text.toString()==""){
            dt_objecionesFirma_bt.isEnabled=false
        }
        dt_objeciones_edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count>0) {
                    dt_objecionesFirma_bt.isEnabled = true
                }else if(count==0){
                    dt_objecionesFirma_bt.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = PostsAdapter(this@DatosActivity,sacarFotosObjeto(or.fotos))

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        dt_guardar_bt.setOnClickListener{

            if (dt_estado_sp.selectedItemPosition!=0) {
                if (or.firma!="") {
                    if(!dt_objecionesFirma_bt.isEnabled) {
                        guardar()

                        val builder = android.support.v7.app.AlertDialog.Builder(this@DatosActivity)
                        builder.setTitle(getString(R.string.enviarTitulo))
                        builder.setMessage(getString(R.string.enviarInfo))
                        builder.setPositiveButton(getString(R.string.aceptar)) { _, _ ->
                            val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                            val networkInfo = cm.activeNetworkInfo
                            if (networkInfo != null && networkInfo.isConnected) {
                                val urlInsertRev = "${getString(R.string.URL)}${getString(R.string.URLinsert)}"
                                EnviarRevi(or.equipamiento, urlInsertRev, this@DatosActivity)
                                or.volveranull()
                                actualizarBD()


                            } else {
                                val builder = android.support.v7.app.AlertDialog.Builder(this@DatosActivity)
                                builder.setTitle(getString(R.string.noInternet))
                                builder.setMessage(getString(R.string.noInternetInfo))
                                builder.setNeutralButton(getString(R.string.aceptar)) { _, _ ->

                                }
                                val dialog: android.support.v7.app.AlertDialog = builder.create()
                                dialog.show()
                            }

                        }
                        builder.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }else{
                        Toast.makeText(this@DatosActivity,getString(R.string.noFirmadoT),Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DatosActivity,getString(R.string.noFirmado),Toast.LENGTH_SHORT).show()
                }
            }else{
                val errorText :TextView = dt_estado_sp.getSelectedView() as TextView
                errorText.setError("")
                errorText.setTextColor(Color.RED)
                errorText.setText(getString(R.string.err_NoEstado))
                Toast.makeText(this@DatosActivity,getString(R.string.err_NoEstado),Toast.LENGTH_SHORT).show()
            }

        }

        dt_UsuarioFirma_bt.setOnClickListener {
            dt_UsuarioFirma_bt.isEnabled=false
            val i = Intent(this@DatosActivity, FirmaActivity::class.java)
            i.putExtra("Nombre", "0")
            startActivity(i)
        }
        dt_objecionesFirma_bt.setOnClickListener {
            val i = Intent(this@DatosActivity, FirmaActivity::class.java)
            i.putExtra("Nombre", "1")
            startActivity(i)
        }
    }

    //********************************************BOTON SALIR*****************************************************
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
                    or.volveranull()
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

    //***********************************************MOSTRAR DATOS*******************************************
    fun MostrarDatos(){
        dt_estado_sp.setSelection(or.estado+1)
        dt_peticiones_edit.setText(or.peticiones)
        dt_objeciones_edit.setText(or.objecione)
    }

    fun sacarFotosObjeto(arrayFotos :ArrayList<Foto>?):ArrayList<fotoItem> {
        val posts : ArrayList<fotoItem> = ArrayList<fotoItem>()
        if (arrayFotos!=null) {
            for (i in 0 until arrayFotos.size) {
                val ruta: Uri = Uri.parse(arrayFotos.get(i).ruta)
                val observacion = arrayFotos.get(i).observacion
                val imagen = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(File(ruta.path)))
                posts.add(fotoItem(imagen, observacion))
            }
        }
        if (posts.size!=4){
            do {
                val icon = BitmapFactory.decodeResource(this.getResources(),R.drawable.foto)
                posts.add(fotoItem(icon,""))
            }while (posts.size!=4)
        }
        return posts
    }
 //*******************************************RECUPERAR DATOS******************************************
    fun recuperarDatos(){
         or.estado = dt_estado_sp.selectedItemPosition -1
         or.usuario = idUsuario()
         or.setfR(fechaHoy())
         or.peticiones = dt_peticiones_edit.text.toString()
         or.objecione = dt_objeciones_edit.text.toString()
         for(i in 0 until fotos.size){
             when(fotos.get(i).nomDes) {
                 "-1.jpg" -> {
                     fotos.get(i).observacion = tv1
                     if(MODO == "2") {
                         if(or.fotos.getOrNull(0)!=null) {
                             or.fotos.removeAt(0)
                         }
                     }
                     or.fotos.add(0,fotos.get(i))
                 }
                 "-2.jpg" -> {
                     fotos.get(i).observacion = tv2
                     if(MODO == "2") {
                         if(or.fotos.getOrNull(1)!=null) {
                             or.fotos.removeAt(1)
                         }
                     }
                     or.fotos.add(1,fotos.get(i))
                 }
                 "-3.jpg" -> {
                     fotos.get(i).observacion = tv3
                     if(MODO == "2") {
                         if(or.fotos.getOrNull(2)!=null) {
                             or.fotos.removeAt(2)
                         }
                     }
                     or.fotos.add(2,fotos.get(i))
                 }
                 "-4.jpg" -> {
                     fotos.get(i).observacion = tv4
                     if(MODO == "2") {
                         if(or.fotos.getOrNull(3)!=null) {
                             or.fotos.removeAt(3)
                         }
                     }
                     or.fotos.add(3,fotos.get(i))
                 }
             }
         }
    }
    fun fechaHoy():String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        val fecha = dateFormat.format(date)
        return fecha
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

    //******************************************GUARDAR EN BBDD**********************************************
    fun guardar(){
        recuperarDatos()
        val bbddsqlite = BBDDSQLite(this@DatosActivity)
        val id_revision :Int = bbddsqlite.insertRevision(or).toInt()
        for (i in 0 until or.fotos.size) {
            bbddsqlite.insertFoto(or.fotos.get(i), id_revision)
        }
        bbddsqlite.close()
    }

    //*******************************************ONCLICK FOTOITEM**********************************************
    override fun onHandleSelectionImage(imagen2: ImageButton,position:Int) {
        imagen=imagen2
        showPictureDialog(position)
    }

    override fun onHandleSelectionEditext(obs: EditText, position: Int) {
        when(position){
            0->{
                tv1 = obs.text.toString()
            }
            1->{
                tv2 = obs.text.toString()
            }
            2->{
                tv3 = obs.text.toString()
            }
            3->{
                tv4 = obs.text.toString()
            }
        }
    }

    fun showPictureDialog(position:Int) {
        val pictureDialog = AlertDialog.Builder(this@DatosActivity)
        pictureDialog.setTitle(getString(R.string.seleccionFoto))
        val pictureDialogItems = arrayOf(getString(R.string.seleccionFotoGaleria), getString(R.string.seleccionFotoCamara))
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallary(position)
                1 -> takePhotoFromCamera(position)
            }
        }
        pictureDialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String
        val imageFileName: String
        val storageDir: File

        timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        imageFileName = "JPEG_" + timeStamp + "_"
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, // prefix
                ".jpg", // suffix
                storageDir      // directory
        )
        //mCurrentPhotoPath = "file:" + image!!.absolutePath
        return image
    }

    fun choosePhotoFromGallary(position:Int) {
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        positionGalleryElement = position
        startActivityForResult(galleryIntent, GALLERY)
    }
    fun takePhotoFromCamera(position:Int) {
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        }catch (ioe:IOException){
            ioe.stackTrace
        }
        //Toast.makeText(this@DatosActivity, "Camera"+position, Toast.LENGTH_SHORT).show()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,1)
        positionCameraElement = position
        startActivityForResult(intent, CAMERA)

        mCurrentPhotoPath = "file:" + photoFile!!.getAbsolutePath()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data

                try {


                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val resizeBitmap: Bitmap
                    resizeBitmap = redimensionarImagenMaximo(bitmap, bitmap.getWidth() / 3f, bitmap.getHeight() / 3f)
                    val path = saveImage(resizeBitmap)

                    when (positionGalleryElement) {
                        0 -> {
                            try {
                                fotos.get(positionGalleryElement).nomDes = "-1.jpg"
                                fotos.get(positionGalleryElement).ruta = path
                            } catch (aoobe: IndexOutOfBoundsException) {
                                val pos = positionGalleryElement + 1
                                fotos.add(Foto(or.id, path, "-$pos.jpg", tv1))
                            }
                        }

                        1 -> {
                            try {
                                fotos.get(positionGalleryElement).nomDes = "-2.jpg"
                                fotos.get(positionGalleryElement).ruta = path
                            } catch (aoobe: IndexOutOfBoundsException) {
                                val pos = positionGalleryElement + 1
                                fotos.add(Foto(or.id, path, "-$pos.jpg", tv2))
                            }
                        }
                        2 -> {
                            try {
                                fotos.get(positionGalleryElement).nomDes = "-3.jpg"
                                fotos.get(positionGalleryElement).ruta = path
                            } catch (aoobe: IndexOutOfBoundsException) {
                                val pos = positionGalleryElement + 1
                                fotos.add(Foto(or.id, path, "-$pos.jpg", tv3))
                            }
                        }

                        3 -> {
                            try {
                                fotos.get(positionGalleryElement).nomDes = "-4.jpg"
                                fotos.get(positionGalleryElement).ruta = path
                            } catch (aoobe: IndexOutOfBoundsException) {
                                val pos = positionGalleryElement + 1
                                fotos.add(Foto(or.id, path, "-$pos.jpg", tv4))
                            }
                        }
                    }
                        imagen!!.setImageBitmap(resizeBitmap)

                    } catch (e: IOException) {
                        Toast.makeText(this@DatosActivity, "Algo a Fallado!", Toast.LENGTH_SHORT).show()
                    }

                }

            } else if (requestCode == CAMERA) {

                val bmp: Bitmap
                val resizeBitmap: Bitmap
                try {
                    if (mCurrentPhotoPath != null) {

                        bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath))
                        resizeBitmap = redimensionarImagenMaximo(bmp, bmp.getWidth() / 3f, bmp.getHeight() / 3f)
                        val path = saveImage(resizeBitmap)

                        when (positionCameraElement) {
                            0 -> {

                                try {
                                    fotos.get(positionCameraElement).nomDes = "-1.jpg"
                                    fotos.get(positionCameraElement).ruta = path
                                } catch (aoobe: IndexOutOfBoundsException) {
                                    val pos = positionCameraElement + 1
                                    fotos.add(Foto(or.id, path, "-$pos.jpg", tv1))

                                }
                            }

                            1 -> {

                                try {
                                    fotos.get(positionCameraElement).nomDes = "-2.jpg"
                                    fotos.get(positionCameraElement).ruta = path
                                } catch (aoobe: IndexOutOfBoundsException) {
                                    val pos = positionCameraElement + 1
                                    fotos.add(Foto(or.id, path, "-$pos.jpg", tv2))

                                }
                            }

                            2 -> {

                                try {
                                    fotos.get(positionCameraElement).nomDes = "-3.jpg"
                                    fotos.get(positionCameraElement).ruta = path
                                } catch (aoobe: IndexOutOfBoundsException) {
                                    val pos = positionCameraElement + 1
                                    fotos.add(Foto(or.id, path, "-$pos.jpg", tv3))

                                }
                            }

                            3 -> {

                                try {
                                    fotos.get(positionCameraElement).nomDes = "-4.jpg"
                                    fotos.get(positionCameraElement).ruta = path
                                } catch (aoobe: IndexOutOfBoundsException) {
                                    val pos = positionCameraElement + 1
                                    fotos.add(Foto(or.id, path, "-$pos.jpg", tv4))

                                }
                            }
                        }
                        imagen!!.setImageBitmap(resizeBitmap)

                    }
                } catch (e: Exception) {
                }
            }
        }

    fun redimensionarImagenMaximo(mBitmap: Bitmap, newWidth: Float, newHeigth: Float): Bitmap {
        //Redimensionamos
        val width = mBitmap.width
        val height = mBitmap.height
        val scaleWidth = newWidth / width
        val scaleHeight = newHeigth / height
        // create a matrix for the manipulation
        val matrix = Matrix()
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight)
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false)
    }


    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        var ruta =""
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {

            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null)
            fo.close()
            ruta = f.getAbsolutePath()


            return f.getAbsolutePath()
        } catch (e1: IOException) {

        }

        return ruta
    }

    companion object {

        private val IMAGE_DIRECTORY = "/RevisionesEmproacsa"
    }

    //******************************BORRAR Y ACTUALIZAR BBDD*****************************************
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
            progressBarInc.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg url: String?): String {
            val text: String
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
            progressBarInc.visibility = View.INVISIBLE
            or.volveranull()
            finish()
        }
    }

    //***************************************CONTROL FIRMAS*******************************************
    fun ComprobarSiFirmado() {
        if (or.firma!="") {
            cambiarStyle(dt_UsuarioFirma_bt)
        }
        if(or.firmaT!=""){
            cambiarStyle(dt_objecionesFirma_bt)
            dt_objeciones_edit.isEnabled=false
        }
    }

    fun cambiarStyle(button :ImageButton){
        button.setImageResource(R.drawable.firma)
        button.isEnabled=false
    }

    //***************************************OnBACK Y OnRESTAR**************************************

    override fun onBackPressed() {
        recuperarDatos()

        val int = Intent(this@DatosActivity, PreguntasActivity::class.java)
        int.putExtra("familia", familia)
        int.putExtra("MODO", "3")
        int.putExtra("n_serie", or.equipamiento)
        startActivity(int)
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        dt_UsuarioFirma_bt.isEnabled=true
        ComprobarSiFirmado()
    }

}

