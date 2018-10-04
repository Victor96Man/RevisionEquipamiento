package com.example.revisionequipamiento

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
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.SnapHelper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import java.io.*
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
    val id : Int=0
    var or : RevisionObjeto = RevisionObjeto.getObjetoRevision(id)

    var tv1: EditText?=null
    var tv2: EditText?=null
    var tv3: EditText?=null
    var tv4: EditText?=null



    var fotos: ArrayList<Foto>? = null

    var mCurrentPhotoPath:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        MODO = intent.getStringExtra("MODO")
        familia = intent.getStringExtra("familia")

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())



        //SPINNER
        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.estados_sp))
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        dt_estado_sp.adapter = spinnerArrayAdapter
        if(MODO == "2"){
            MostrarDatos()
        }

        ComprobarSiFirmado()

        var dt_objeciones_edit = findViewById<EditText>(R.id.dt_objeciones_edit)
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

        if(MODO=="2"){

            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = PostsAdapter(this@DatosActivity,posts)
        }else{

            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = PostsAdapter(this@DatosActivity,selectFotos())
        }





        val snapHelper: SnapHelper = LinearSnapHelper() as SnapHelper
        snapHelper.attachToRecyclerView(recyclerView)

        dt_guardar_bt.setOnClickListener{

            if (dt_estado_sp.selectedItemPosition!=0) {
                if (or.firma!="") {
                    dt_UsuarioFirma_bt.setError(null)
                    guardar()

                    val builder = android.support.v7.app.AlertDialog.Builder(this@DatosActivity)
                    builder.setTitle(getString(R.string.enviarTitulo))
                    builder.setMessage(getString(R.string.enviarInfo))
                    builder.setPositiveButton(getString(R.string.aceptar)) { _, _ ->
                        var cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        var networkInfo = cm.activeNetworkInfo
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
                } else {
                    dt_UsuarioFirma_bt.setError("")
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

    fun selectFotos():ArrayList<fotoItem> {
        var posts : ArrayList<fotoItem>? = null
        val bbddsqlite = BBDDSQLite(this@DatosActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        cusrsor = db.rawQuery("Select * from fotos where id_revision= '$'",null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    posts = ArrayList<fotoItem>()
                }
                do {
                    val ruta = cusrsor.getString(cusrsor.getColumnIndex("ruta")) as Uri
                    val observacion = cusrsor.getString(cusrsor.getColumnIndex("observacion"))
                    if (posts != null) {
                        var imagen = MediaStore.Images.Media.getBitmap(this.contentResolver, ruta)
                        posts.add(fotoItem(imagen,observacion))
                    }
                }while (cusrsor.moveToNext())

                db.close()

            }
        }
        if (posts!!.size!=4){
            do {
                posts.add(fotoItem(null,""))
            }while (posts!!.size!=4)
        }
        return posts!!
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

        recuperarDatos()
        val bbddsqlite = BBDDSQLite(this@DatosActivity)
        bbddsqlite.insertRevision(or)
        var fotos = or.fotos
        for (foto in fotos){
            bbddsqlite.insertFoto(foto)
        }
        bbddsqlite.close()
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
        or.fotos = fotos
        println(or)
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

    override fun onHandleSelectionImage(imagen2: ImageButton,position:Int) {
        imagen=imagen2

        showPictureDialog(position)

    }

    override fun onHandleSelectionEditext(obs: EditText, position: Int) {
        when(position){
            0->{
                tv1 = obs
            }
            1->{
                tv2 = obs
            }
            2->{
                tv3 = obs
            }
            3->{
                tv4 = obs
            }
        }
    }

    fun showPictureDialog(position:Int) {
        val pictureDialog = AlertDialog.Builder(this@DatosActivity)
        pictureDialog.setTitle(getString(R.string.seleccionFoto))
        val pictureDialogItems = arrayOf(getString(R.string.seleccionFotoGaleria), getString(R.string.seleccionFotoCamara))
        pictureDialog.setItems(pictureDialogItems) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary(position)
                1 -> takePhotoFromCamera(position)
            }
        }
        pictureDialog.show()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String
        val imageFileName: String
        val storageDir: File
        var image: File? = null

        timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        imageFileName = "JPEG_" + timeStamp + "_"
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        image = File.createTempFile(
                imageFileName, // prefix
                ".jpg", // suffix
                storageDir      // directory
        )
        //mCurrentPhotoPath = "file:" + image!!.absolutePath
        return image
    }
    fun choosePhotoFromGallary(position:Int) {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.putExtra("position", position)


        startActivityForResult(galleryIntent, GALLERY)
    }

    fun takePhotoFromCamera(position:Int) {
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        }catch (ioe:IOException){
            ioe.stackTrace
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,1);
        positionCameraElement = position
        startActivityForResult(intent, CAMERA)

        mCurrentPhotoPath = "file:" + photoFile!!.getAbsolutePath()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fotos = ArrayList()
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    var position = data!!.extras!!.getInt("position")
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    //Toast.makeText(this@DatosActivity, "Image Saved!", Toast.LENGTH_SHORT).show()

                    when (position) {
                        0 -> {
                            if(fotos!=null) {
                                if (fotos!!.get(position) != null) {
                                    fotos!!.get(position).nomDes = "-1.jpg"
                                    fotos!!.get(position).ruta = path
                                } else {
                                    var pos = position + 1
                                    fotos!!.add(Foto(0, path, "-$pos.jpg", ""))
                                }
                            }else{
                                println("es null")
                            }

                        }

                        1 -> {
                            if(fotos!=null) {
                                if (fotos!!.get(position) != null) {
                                    fotos!!.get(position).nomDes = "-2.jpg"
                                    fotos!!.get(position).ruta = path
                                } else {
                                    var pos = position + 1
                                    fotos!!.add(Foto(0, path, "-$pos.jpg", ""))
                                }
                            }else{
                                println("es null")
                            }
                        }

                        2 -> {
                            if(fotos!=null) {
                                if (fotos!!.get(position) != null) {
                                    fotos!!.get(position).nomDes = "-3.jpg"
                                    fotos!!.get(position).ruta = path
                                } else {
                                    var pos = position + 1
                                    fotos!!.add(Foto(0, path, "-$pos.jpg", ""))
                                }
                            }else{
                                println("es null")
                            }
                        }

                        3 -> {
                            if(fotos!=null) {
                                if (fotos!!.get(position) != null) {
                                    fotos!!.get(position).nomDes = "-4.jpg"
                                    fotos!!.get(position).ruta = path
                                } else {
                                    var pos = position + 1
                                    fotos!!.add(Foto(0, path, "-$pos.jpg", ""))
                                }
                            }else{
                                println("es null")
                            }
                        }
                    }

                    imagen!!.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@DatosActivity, "Algo a Fallado!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {

            val bmp: Bitmap
            val resizeBitmap: Bitmap
            try{
                if(mCurrentPhotoPath !=null){

                    bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    resizeBitmap = redimensionarImagenMaximo(bmp, bmp.getWidth() / 3f, bmp.getHeight() / 3f)
                    val path = saveImage(resizeBitmap)

                        when (positionCameraElement) {
                            0 -> {
                                if(fotos!=null) {
                                    if(fotos!!.get(positionCameraElement)!=null){
                                        fotos!!.get(positionCameraElement).nomDes = "-1.jpg"
                                        fotos!!.get(positionCameraElement).ruta = path
                                    }else{
                                        var pos = positionCameraElement+1
                                        fotos!!.add(Foto(0,path,"-$pos.jpg",""))
                                    }
                                }else{
                                    println("es null")
                                }

                            }

                            1 -> {
                                if(fotos!=null) {
                                    if (fotos!!.get(positionCameraElement) != null) {
                                        fotos!!.get(positionCameraElement).nomDes = "-2.jpg"
                                        fotos!!.get(positionCameraElement).ruta = path
                                    } else {
                                        var pos = positionCameraElement + 1
                                        fotos!!.add(Foto(0, path, "-$pos.jpg", ""))
                                    }
                                }else{
                                    println("es null")
                                }
                            }

                            2 -> {
                                if(fotos!=null) {
                                    if (fotos!!.get(positionCameraElement) != null) {
                                        fotos!!.get(positionCameraElement).nomDes = "-3.jpg"
                                        fotos!!.get(positionCameraElement).ruta = path
                                    } else {
                                        var pos = positionCameraElement + 1
                                        fotos!!.add(Foto(0, path, "-$pos.jpg", ""))
                                    }
                                }else{
                                    println("es null")
                                }
                            }

                            3 -> {
                                if (fotos != null) {
                                    if (fotos!!.get(positionCameraElement) != null) {
                                        fotos!!.get(positionCameraElement).nomDes = "-4.jpg"
                                        fotos!!.get(positionCameraElement).ruta = path
                                    } else {
                                        var pos = positionCameraElement + 1
                                        fotos!!.add(Foto(0, path, "-$pos.jpg", ""))
                                    }
                                }else{
                                    println("es null")
                                }
                            }
                        }

                    imagen!!.setImageBitmap(resizeBitmap)
                }
            }catch(e:Exception){
                e.printStackTrace()
            }



        }
    }

    fun addFotoToArray(foto: Foto, pos: Int){

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
        var ruta: String =""
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
            ruta = f.getAbsolutePath()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ruta
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
        int.putExtra("MODO", "3")
        int.putExtra("n_serie", or.equipamiento )
        startActivity(int)
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        dt_UsuarioFirma_bt.isEnabled=true
        ComprobarSiFirmado()

    }

    fun ComprobarSiFirmado() {
        if (or.firma!="") {
            cambiarStyle(dt_UsuarioFirma_bt)
        }
        if(or.firmaT!=""){
            cambiarStyle(dt_objecionesFirma_bt)
            dt_objeciones_edit.isEnabled=false
        }
    }

    fun cambiarStyle(button :Button){
        button.text = "Firmado"
        button.setBackgroundColor(resources.getColor(R.color.GrisTransparente))
        button.setTextColor(resources.getColor(R.color.colorPrimary))
        button.isEnabled=false
        button.setError(null)
    }

}

