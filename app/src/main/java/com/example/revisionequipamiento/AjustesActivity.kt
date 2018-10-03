package com.example.revisionequipamiento

import android.content.Context
import android.content.pm.PackageInfo
import android.database.Cursor
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.revisionequipamiento.Files.BBDDSQLite
import kotlinx.android.synthetic.main.activity_ajustes.*
import kotlinx.android.synthetic.main.contrasena_dialog.*
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Pattern

class AjustesActivity : AppCompatActivity() {
    val Password_patter :Pattern = Pattern.compile("/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\$@\$!#%*?&])([A-Za-z\\d\$@\$!%*?&]|[^ ]){8,16}\$/")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var pInfo: PackageInfo? = null
        pInfo = packageManager.getPackageInfo(packageName, 0)
        val versionS = pInfo!!.versionName
        version_tx.text="Versión: $versionS"
        val username = SelectUsuario()
        aj_cambioContra_bt.setOnClickListener { _ ->
            aj_cambioContra_bt.isEnabled=false
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.contrasena_dialog, null)
            //AlertDialogBuilder

            val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setTitle(getString(R.string.aj_cambiarContra_tx))
            //show dialog
            val mAlertDialog = mBuilder.show()
            mAlertDialog.setCancelable(false)
            mAlertDialog.setCanceledOnTouchOutside(false)

            mAlertDialog.aj_aceptar_bt.setOnClickListener{
                val contraseñaA = mAlertDialog.aj_contraActual_edt.text.toString()
                val contraseñaN = mAlertDialog.aj_contraNueva_edt.text.toString()
                val contraseñaNR = mAlertDialog.aj_contraNuevaR_edt.text.toString()
                if (comprobarInternet()) {
                    if (contraseñaA != "") {
                        if (contraseñaN != "") {
                            if (contraseñaNR != "") {
                                if (contraseñaNR ==contraseñaN) {
                                    if(Password_patter.matcher(contraseñaN).matches()){
                                        val urlInicio = "${getString(R.string.URL)}${getString(R.string.URLcontraseña)}"
                                        AsyncTaskHandleJSON(username, contraseñaA, contraseñaN).execute(urlInicio)
                                        mAlertDialog.dismiss()
                                        aj_cambioContra_bt.isEnabled=true
                                    }else{
                                        mAlertDialog.aj_contraNueva_edt.setError(getString(R.string.err_campoPSW))
                                    }
                                }else{
                                    mAlertDialog.aj_contraNuevaR_edt.setError(getString(R.string.err_campoNoIgual))
                                }
                            }else{
                                mAlertDialog.aj_contraNuevaR_edt.setError(getString(R.string.err_campoVacio))
                            }
                        }else{
                            mAlertDialog.aj_contraNueva_edt.setError(getString(R.string.err_campoVacio))
                        }
                    }else{
                        mAlertDialog.aj_contraActual_edt.setError(getString(R.string.err_campoVacio))
                    }
                }else{
                    Toast.makeText(this@AjustesActivity,getString(R.string.noInternetInfo),Toast.LENGTH_SHORT).show()
                }
            }

            mAlertDialog.aj_cancelar_bt.setOnClickListener{
                mAlertDialog.dismiss()
                aj_cambioContra_bt.isEnabled=true
            }
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

    fun SelectUsuario():String {
        //consulta a la bbdd
        val bbddsqlite = BBDDSQLite(this)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        var username :String=""
        cusrsor = db.rawQuery("SELECT username FROM usuarios", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                     username = cusrsor.getString(cusrsor.getColumnIndex("username"))
                }
            }
        }
        db.close()
        return username
    }

    inner class AsyncTaskHandleJSON(usuario1: String, contrasena1:String, contrasena2:String): AsyncTask<String, String, String>() {
        var usuario = usuario1
        var contrasenaA = contrasena1
        var contrasenaN = contrasena2
        override fun onPreExecute() {
            super.onPreExecute()
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            aj_MyProgressBar3.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg url: String): String {
            return POST(url[0], usuario, contrasenaA,contrasenaN)

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
            aj_MyProgressBar3.visibility = View.INVISIBLE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun handleJson(jsonString: String?) {
        val jsonarray = JSONArray(jsonString)
        val jsonobject = jsonarray.getJSONObject(0)
        val codigo = jsonobject.getInt("codigo")
        if(codigo==1){
            val contrasena = jsonobject.getString("contrasena")
            val bbddsqlite = BBDDSQLite(this@AjustesActivity)
            bbddsqlite.updateContrasena(contrasena)
            Toast.makeText(this@AjustesActivity,getString(R.string.contraseñaOK),Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@AjustesActivity,getString(R.string.errorBD),Toast.LENGTH_SHORT).show()
        }
    }

    private fun comprobarInternet():Boolean {
        var cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected){
            return true
        }
        return false
    }

    fun POST(url: String, usuario:String, contrasenaA: String,  contrasenaN: String): String {
        var inputStream: InputStream? = null
        var result = ""
        try {

            // 1. create HttpClient
            val httpclient = DefaultHttpClient()

            // 2. make POST request to the given URL
            val httpPost = HttpPost(url)

            var json = ""

            // 3. build jsonObject
            val jsonObject = JSONObject()
            jsonObject.accumulate("usuario", usuario)
            jsonObject.accumulate("contrasenaactual", contrasenaA)
            jsonObject.accumulate("contrasenanueva", contrasenaN)
            //jsonObject.accumulate("twitter", person.getTwitter());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString()

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            val se = StringEntity(json)

            // 6. set httpPost Entity
            httpPost.entity = se

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json")
            httpPost.setHeader("Content-type", "application/json")

            // 8. Execute POST request to the given URL
            val httpResponse = httpclient.execute(httpPost)

            // 9. receive response as inputStream
            inputStream = httpResponse.entity.content

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream)
            else
                result = "Did not work!"

        } catch (e: Exception) {

        }

        // 11. return result
        return result
    }

    fun convertInputStreamToString(inputStream: InputStream): String {

        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line: String
        var result = ""

        try {
            do {
                line = bufferReader.readLine()
                if (line != null) {
                    result += line
                }
            } while (line != null)
            inputStream.close()
        } catch (ex: Exception) {

        }

        return result
    }


    override fun onBackPressed() {
        finish()
    }
}
