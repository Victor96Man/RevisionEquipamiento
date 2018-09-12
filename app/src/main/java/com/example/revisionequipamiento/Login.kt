package com.example.revisionequipamiento

import android.content.Intent
import android.database.Cursor
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_login.*
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

    inner class AsyncTaskHandleJSON(username: String, contrasena:String): AsyncTask<String, String, String>() {
        var user = username
        var contrasena = contrasena
        override fun onPreExecute() {
            super.onPreExecute()
            MyprogressBar.visibility = View.VISIBLE
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
            Toast.makeText(this@Login, getString(R.string.loginError), Toast.LENGTH_LONG).show()
            MyprogressBar.visibility = View.INVISIBLE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
        val urlInicio= "${getString(R.string.URL)}${getString(R.string.URLinicio)}"
        AsyncTaskHandleJSON(username,contrasena).execute(urlInicio)
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
    fun POST(url: String, usuario:String, contrasena: String): String {
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
            jsonObject.accumulate("contrasena", contrasena)
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
}
