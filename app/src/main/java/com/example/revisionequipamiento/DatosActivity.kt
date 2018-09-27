package com.example.revisionequipamiento

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.SnapHelper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.revisionequipamiento.Adapter.PostsAdapter
import kotlinx.android.synthetic.main.activity_datos.*
import kotlinx.android.synthetic.main.horizontal_scroll_card.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class DatosActivity : AppCompatActivity(), PostsAdapter.CallbackInterface{


    private val GALLERY = 1
    private val CAMERA = 2
    var imagen :ImageButton?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var dt_objeciones_edit = findViewById<EditText>(R.id.dt_objeciones_edit)
        val posts: ArrayList<String> = ArrayList()

        posts.add("")
        posts.add("")
        posts.add("")
        posts.add("")
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = PostsAdapter(this@DatosActivity,posts)

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        dt_UsuarioFirma_bt.setOnClickListener {
            val i = Intent(this@DatosActivity, FirmaActivity::class.java)
            startActivity(i)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun onBackPressed() {
        finish()
    }
}

