package com.example.revisionequipamiento

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.Manifest.permission.*
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*
import android.os.Build
import android.annotation.TargetApi
import android.database.Cursor
import android.view.View
import android.net.Uri
import android.support.v7.app.AlertDialog
import com.example.revisionequipamiento.Files.BBDDSQLite


class SplashScreenActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        if (mayRequestStoragePermission()) {
            Timer().schedule(object : TimerTask() {
                override fun run() {

                        if(logeado()){
                            val bbddsqlite = BBDDSQLite(this@SplashScreenActivity)
                            val bd = bbddsqlite.writableDatabase
                            bd.close()
                            startActivity(Intent(applicationContext, PrincipalActivity::class.java))
                            finish()
                        }else{
                            val bbddsqlite = BBDDSQLite(this@SplashScreenActivity)
                            val bd = bbddsqlite.writableDatabase
                            bd.close()
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                            finish()
                        }

                    }
                }, 2000)
        }else{

        }
    }



    private fun mayRequestStoragePermission(): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true

        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED)
            return true

        if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(INTERNET) || shouldShowRequestPermissionRationale(CAMERA) || shouldShowRequestPermissionRationale(ACCESS_NETWORK_STATE)) {
            Snackbar.make(constraintLayout, R.string.permisoInfo,
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, object : View.OnClickListener {
                @TargetApi(Build.VERSION_CODES.M)
                override fun onClick(v: View) {
                    requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE, INTERNET, CAMERA, ACCESS_NETWORK_STATE), 103)
                }
            }).show()
        } else {
            requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE, INTERNET, CAMERA, ACCESS_NETWORK_STATE), 103)
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === 103) {
            if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED && grantResults[1] === PackageManager.PERMISSION_GRANTED && grantResults[2] === PackageManager.PERMISSION_GRANTED && grantResults[3] === PackageManager.PERMISSION_GRANTED) {

                Timer().schedule(object : TimerTask() {
                    override fun run() {
                            if(logeado()){
                                val bbddsqlite = BBDDSQLite(this@SplashScreenActivity)
                                val bd = bbddsqlite.writableDatabase
                                bd.close()
                                startActivity(Intent(applicationContext, PrincipalActivity::class.java))
                                finish()
                            }else{
                                val bbddsqlite = BBDDSQLite(this@SplashScreenActivity)
                                val bd = bbddsqlite.writableDatabase
                                bd.close()
                                startActivity(Intent(applicationContext, LoginActivity::class.java))
                                finish()
                            }
                        }
                }, 3000)
            } else {
                showExplanation()
            }
        }
    }

    private fun showExplanation() {
        val builder = AlertDialog.Builder(this@SplashScreenActivity)
        builder.setTitle(getString(R.string.permisoD))
        builder.setMessage(getString(R.string.permisoInfo))
        builder.setPositiveButton(getString(R.string.aceptar)) {
            _, _ ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
            dialog.dismiss()
            finish()
        }

        val dialog =builder.create()
        dialog.show()
    }

    private fun logeado():Boolean {
        //consulta a la bbdd
        var login= false
        val bbddsqlite = BBDDSQLite(this)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT * FROM usuarios", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                login= true
            }
        }
        db.close()
        return login
    }
}
