package com.example.revisionequipamiento

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.revisionequipamiento.Clases.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class Login : AppCompatActivity() {
    val url="http://emproacsa.mjhudesings.com/api/v1/"
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
            Toast.makeText(this@Login, "usuario o contraseña no validos", Toast.LENGTH_LONG).show()
        }
        if(codigo==1){
            val usuarios= jsonobject.getJSONArray("usuarios")
            val jsonobject2 = usuarios.getJSONObject(0)
            val username = jsonobject2.getString("username")
            val contraseña = jsonobject2.getString("password")
            val url2= url+"todo/$username/$contraseña"
            AsyncTaskHandleJSON2().execute(url2)
            startActivity(Intent(applicationContext, Principal::class.java))
            finish()
        }
    }


    fun soyUsuario(username :String, contrasena :String){
        val url1= url+"inicio/$username/$contrasena"
        AsyncTaskHandleJSON().execute(url1)
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
            MyprogressBar.visibility = View.INVISIBLE;
            ParseoFile(result, this@Login)
        }
    }

}
