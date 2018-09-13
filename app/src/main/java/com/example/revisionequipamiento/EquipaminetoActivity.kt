package com.example.revisionequipamiento

import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.revisionequipamiento.Clases.Revision
import kotlinx.android.synthetic.main.activity_equipamineto.*
import kotlinx.android.synthetic.main.content_equipamineto.*
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class EquipaminetoActivity : AppCompatActivity() {

    var familia :String=""
    var marca :String=""
    var ubicacion :String=""
    var zona :String=""
    var modelo :String=""
    var fecha_compra :String=""
    var fecha_puesta_funcionamiento :String=""
    var fecha_proxima_revision :String=""
    var fecha_revision :String=""
    var fecha_caducidad :String=""
    var fecha_baja :String=""
    var referencia_normativa :String=""
    var estado :Int= 0
    var id_remplaza :String=""
    var trabajador :String=""
    var bitacora :String=""
    var situacion :String= ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipamineto)
        setSupportActionBar(toolbar)
        val n_serie = intent.getStringExtra("n_serie")
        supportActionBar?.title= n_serie
        buscarEquipamineto(n_serie)
        eq_familia_tx.text = familia
        fab.setOnClickListener { view ->
        }
    }

    fun buscarEquipamineto(n_serie: String?) {
        val bbddsqlite = BBDDSQLite(this@EquipaminetoActivity)
        val db = bbddsqlite.writableDatabase
        val cusrsor: Cursor
        cusrsor = db.rawQuery("SELECT t1.*, t2.nombrefamilia as nombrefamilia, t3.nombremarca as nombremarca, t4.nombreubicacion as nombreubicacion, t5.nombrezona as nombrezona, t6.nombretrabajador as nombretrabajador FROM equipamientos as t1, familias as t2, marcas as t3, ubicaciones as t4, zonas as t5, trabajadores as t6 WHERE t1.id_familia = t2.id AND t1.id_marca = t3.id AND t1.id_ubicacion = t4.id AND t1.id_zona = t5.id AND t1.id_trabajador = t6.id AND t1.n_serie= '${n_serie}'", null)
        if (cusrsor != null) {
            if (cusrsor.count > 0) {
                if (cusrsor.moveToFirst()) {
                    familia = cusrsor.getString(cusrsor.getColumnIndex("nombrefamilia"))
                    marca = cusrsor.getString(cusrsor.getColumnIndex("nombremarca"))
                    ubicacion = cusrsor.getString(cusrsor.getColumnIndex("nombreubicacion"))
                    zona = cusrsor.getString(cusrsor.getColumnIndex("nombrezona"))
                    modelo = cusrsor.getString(cusrsor.getColumnIndex("modelo"))
                    fecha_compra = cusrsor.getString(cusrsor.getColumnIndex("fecha_compra"))
                    fecha_puesta_funcionamiento = cusrsor.getString(cusrsor.getColumnIndex("fecha_puesta_funcionamiento"))
                    fecha_proxima_revision = cusrsor.getString(cusrsor.getColumnIndex("fecha_proxima_revision"))
                    fecha_revision = cusrsor.getString(cusrsor.getColumnIndex("fecha_revision"))
                    fecha_caducidad = cusrsor.getString(cusrsor.getColumnIndex("fecha_caducidad"))
                    fecha_baja = cusrsor.getString(cusrsor.getColumnIndex("fecha_baja"))
                    referencia_normativa = cusrsor.getString(cusrsor.getColumnIndex("referencia_normativa"))
                    estado = cusrsor.getInt(cusrsor.getColumnIndex("estado"))
                    id_remplaza = cusrsor.getString(cusrsor.getColumnIndex("id_serie_reemplaza"))
                    trabajador = cusrsor.getString(cusrsor.getColumnIndex("nombretrabajador"))
                    bitacora = cusrsor.getString(cusrsor.getColumnIndex("bitacora"))
                    situacion = cusrsor.getString(cusrsor.getColumnIndex("situacion"))
                    db.close()
                }else{
                    Toast.makeText(this@EquipaminetoActivity,getString(R.string.errorBD),Toast.LENGTH_SHORT).show()
                    finish()
                }
            }else{
                Toast.makeText(this@EquipaminetoActivity,getString(R.string.errorBD),Toast.LENGTH_SHORT).show()
                finish()
            }
        }else{
            Toast.makeText(this@EquipaminetoActivity,getString(R.string.errorBD),Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    inner class AsyncTaskHandleJSON(revision: Revision): AsyncTask<String, String, String>() {
        var rev = revision
        override fun onPreExecute() {
            super.onPreExecute()

            /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)*/
        }

        override fun doInBackground(vararg url: String): String {
            println(rev.toString().replace("'", "\""))
            return POST(url[0], rev )

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Toast.makeText(this@EquipaminetoActivity, result.toString(), Toast.LENGTH_LONG).show()

            //handleJson(result)
        }
    }

    private fun handleJson(jsonString: String?) {

        /*val jsonarray = JSONArray(jsonString)
        val jsonobject = jsonarray.getJSONObject(0)
        val codigo = jsonobject.getInt("codigo")
        if(codigo==2) {
            Toast.makeText(this@EquipaminetoActivity, getString(R.string.loginError), Toast.LENGTH_LONG).show()
            MyprogressBar.visibility = View.INVISIBLE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }*/

    }

    fun POST(url: String, revision: Revision): String {
        var inputStream: InputStream? = null
        var result = ""
        try {
            // 1. create HttpClient
            val httpclient = DefaultHttpClient()
            // 2. make POST request to the given URL
            val httpPost = HttpPost(url)
            var json = revision.toString().replace("'","\"")
            // 3. build jsonObject
            /*val jsonObject = JSONObject()
            jsonObject.accumulate("usuario", usuario)
            jsonObject.accumulate("contrasena", contrasena)*/
            //jsonObject.accumulate("twitter", person.getTwitter());
            // 4. convert JSONObject to JSON to String
            //json = jsonObject.toString()
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
                result = "Algo salió mal."

        } catch (e: Exception) {

        }
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
