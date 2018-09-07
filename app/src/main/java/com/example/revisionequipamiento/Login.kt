package com.example.revisionequipamiento

import android.content.Intent
import android.database.Cursor
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class Login : AppCompatActivity() {
    var id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        entrar_login.setOnClickListener{
            var username = username_login.text.toString()
            var contrasena = contrasena_login.text.toString()

            soyUsuario(username,contrasena)
        }

    }

    inner class AsyncTaskHandleJSON(): AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            MyprogressBar.visibility = View.VISIBLE;
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
            handleJson(result)
        }
    }

    private fun handleJson(jsonString: String?) {
        val jsonarray = JSONArray(jsonString)
        val jsonobject = jsonarray.getJSONObject(0)
        val codigo = jsonobject.getInt("codigo")
        if(codigo==2) {
            Toast.makeText(this@Login, getString(R.string.loginError), Toast.LENGTH_LONG).show()
            MyprogressBar.visibility = View.INVISIBLE
        }
        if(codigo==1){
            val usuarios= jsonobject.getJSONArray("usuarios")
            val jsonobject2 = usuarios.getJSONObject(0)
            val username = jsonobject2.getString("username")
            val contraseña = jsonobject2.getString("password")
            id = jsonobject2.getString("id")
            val urlTodo= "${getString(R.string.URL)}${getString(R.string.URLtodo)}$username/$contraseña"
            AsyncTaskHandleJSON2().execute(urlTodo)

        }
    }


    fun soyUsuario(username :String, contrasena :String){
        val urlInicio= "${getString(R.string.URL)}${getString(R.string.URLinicio)}$username/$contrasena"
        AsyncTaskHandleJSON().execute(urlInicio)
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
                // OneSignal Initialization
                OneSignal.startInit(this@Login)
                        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                        .unsubscribeWhenNotificationsAreDisabled(true)
                        .init()
                OneSignal.sendTag(getString(R.string.user_id), id)
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            MyprogressBar.visibility = View.INVISIBLE
            ParseoFile(result, this@Login)
            startActivity(Intent(applicationContext, Principal::class.java))
            finish()
        }
    }

}
