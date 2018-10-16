package com.example.revisionequipamiento

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.revisionequipamiento.Files.ParseoFile
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.email_dialog.*
import kotlinx.android.synthetic.main.progressbar.*
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {
    var id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lg_olvido_tx.setOnClickListener{
            if (comprobarInternet()) {
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.email_dialog, null)
                //AlertDialogBuilder

                val mBuilder = AlertDialog.Builder(this)
                        .setView(mDialogView)
                        .setTitle(getString(R.string.lg_tituloEmail))
                //show dialog
                val mAlertDialog = mBuilder.show()
                mAlertDialog.setCancelable(false)
                mAlertDialog.setCanceledOnTouchOutside(false)

                mAlertDialog.lg_aceptar_bt.setOnClickListener{
                    val email :String = mAlertDialog.lg_email_edt.text.toString()
                    if (email.length>0){
                        val urlemial= "${getString(R.string.URL)}${getString(R.string.URLemail)}"
                        AsyncTaskHandleJSON3(email).execute(urlemial)
                        mAlertDialog.dismiss()
                    }else{
                        mAlertDialog.lg_email_edt.setError(getString(R.string.err_campoVacio))
                    }

                }
                mAlertDialog.lg_cancelar_bt.setOnClickListener{
                    mAlertDialog.dismiss()
                }
            }else{
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setTitle(getString(R.string.noInternet))
                builder.setMessage(getString(R.string.noInternetInfo))
                builder.setNeutralButton(getString(R.string.aceptar)){_,_ ->

                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
        entrar_login.setOnClickListener{
            val username = username_login.text.toString()
            val contrasena = contrasena_login.text.toString()
            if (comprobarInternet()) {
                soyUsuario(username, contrasena)
            }else{
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setTitle(getString(R.string.noInternet))
                builder.setMessage(getString(R.string.noInternetInfo))
                builder.setNeutralButton(getString(R.string.aceptar)){_,_ ->

                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }

    private fun comprobarInternet():Boolean {
       val cm = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
       val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected){
            return true
        }
        return false
    }

    inner class AsyncTaskHandleJSON(username: String, contrasena1:String): AsyncTask<String, String, String>() {
        var user = username
        var contrasena = contrasena1
        override fun onPreExecute() {
            super.onPreExecute()

            progressBarInc.visibility = View.VISIBLE
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        override fun doInBackground(vararg url: String): String {
            return POST(url[0], user, contrasena)

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }
    }

    private fun handleJson(jsonString: String?) {
        val jsonarray = JSONArray(jsonString)
        val jsonobject = jsonarray.getJSONObject(0)
        val codigo = jsonobject.getInt("codigo")
        if(codigo==2) {
            Toast.makeText(this@LoginActivity, getString(R.string.loginError), Toast.LENGTH_LONG).show()
            progressBarInc.visibility = View.INVISIBLE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        if(codigo==1){
            val usuarios= jsonobject.getJSONArray("usuarios")
            val jsonobject2 = usuarios.getJSONObject(0)
            val username = jsonobject2.getString("username")
            val contrasena = jsonobject2.getString("password")
            id = jsonobject2.getString("id")
            val urlTodo= "${getString(R.string.URL)}${getString(R.string.URLtodo)}$username/$contrasena"
            AsyncTaskHandleJSON2().execute(urlTodo)

        }
    }


    fun soyUsuario(username :String, contrasena :String){
        val urlInicio= "${getString(R.string.URL)}${getString(R.string.URLinicio)}"
        AsyncTaskHandleJSON(username,contrasena).execute(urlInicio)
    }

    inner class AsyncTaskHandleJSON2(): AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            val text : String
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            }finally {
                connection.disconnect()
                // OneSignal Initialization
                OneSignal.startInit(this@LoginActivity)
                        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                        .unsubscribeWhenNotificationsAreDisabled(true)
                        .init()
                OneSignal.sendTag(getString(R.string.user_id), id)
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressBarInc.visibility = View.INVISIBLE
            ParseoFile(result, this@LoginActivity,1)
            startActivity(Intent(applicationContext, PrincipalActivity::class.java))
            finish()
        }
    }
    fun POST(url: String, usuario:String, contrasena: String): String {
        try {
            val httpclient = DefaultHttpClient()
            val httpPost = HttpPost(url)
            val jsonObject = JSONObject()
            jsonObject.accumulate("usuario", usuario)
            jsonObject.accumulate("contrasena", contrasena)
            val json = jsonObject.toString()
            val se = StringEntity(json)
            httpPost.entity = se
            httpPost.setHeader("Accept", "application/json")
            httpPost.setHeader("Content-type", "application/json")
            val httpResponse = httpclient.execute(httpPost)
            val inputStream = httpResponse.entity.content
            if (inputStream != null)
                return  convertInputStreamToString(inputStream)
            else
                return  "Did not work!"
        } catch (e: Exception) {
            return "Did not work!"
        }
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

    inner class AsyncTaskHandleJSON3(email: String): AsyncTask<String, String, String>() {
        val email1 = email
        override fun doInBackground(vararg url: String): String {
            return POST3(url[0], email1)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson3(result)
        }
    }

    private fun handleJson3(jsonString: String?) {

        val jsonobject = JSONObject(jsonString)
        val codigo = jsonobject.getInt("codigo")
        if(codigo==2) {
            Toast.makeText(this@LoginActivity, getString(R.string.emailError), Toast.LENGTH_LONG).show()
        }
        if(codigo==1){
            Toast.makeText(this@LoginActivity, getString(R.string.emailOK), Toast.LENGTH_LONG).show()

        }
    }

    fun POST3(url: String, email: String): String {
        try {
            val httpclient = DefaultHttpClient()
            val httpPost = HttpPost(url)
            val jsonObject = JSONObject()
            jsonObject.accumulate("email", email)
            val json = jsonObject.toString()
            httpPost.entity = StringEntity(json)
            httpPost.setHeader("Accept", "application/json")
            httpPost.setHeader("Content-type", "application/json")
            val httpResponse = httpclient.execute(httpPost)
            val inputStream = httpResponse.entity.content
            if (inputStream != null)
                return convertInputStreamToString(inputStream)
            else
                return "Did not work!"
        } catch (e: Exception) {
            return "Did not work!"
        }

    }
}
