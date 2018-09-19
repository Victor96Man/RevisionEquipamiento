package com.example.revisionequipamiento.Files

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.widget.Toast
import com.example.revisionequipamiento.Clases.Revision
import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

fun EnviarRevi(n_serie :String,url:String, context: Context){
    CogerRevision(n_serie, url, context)
}

private fun CogerRevision(n_serie :String,url:String,context: Context) {
    val bbddsqlite = BBDDSQLite(context)
    val db = bbddsqlite.writableDatabase
    val cusrsor: Cursor
    cusrsor = db.rawQuery("Select * FROM revisiones WHERE revisiones.id_equipamiento= '${n_serie}'", null)
    if (cusrsor != null) {
        if (cusrsor.count > 0) {
            if (cusrsor.moveToFirst()) {
                val newRevision = Revision(cusrsor.getInt(cusrsor.getColumnIndex("id")),
                        cusrsor.getString(cusrsor.getColumnIndex("id_equipamiento")),
                        cusrsor.getInt(cusrsor.getColumnIndex("id_usuario")),
                        cusrsor.getString(cusrsor.getColumnIndex("fecharevision")),
                        cusrsor.getInt(cusrsor.getColumnIndex("estado")),
                        cusrsor.getInt(cusrsor.getColumnIndex("enviado")),
                        cusrsor.getInt(cusrsor.getColumnIndex("vp1")),
                        cusrsor.getInt(cusrsor.getColumnIndex("vp2")),
                        cusrsor.getInt(cusrsor.getColumnIndex("vp3")),
                        cusrsor.getInt(cusrsor.getColumnIndex("vp4")),
                        cusrsor.getInt(cusrsor.getColumnIndex("vp5")),
                        cusrsor.getInt(cusrsor.getColumnIndex("vp6")),
                        cusrsor.getInt(cusrsor.getColumnIndex("vp7")),
                        cusrsor.getInt(cusrsor.getColumnIndex("vp8")),
                        cusrsor.getInt(cusrsor.getColumnIndex("vp9")),
                        cusrsor.getInt(cusrsor.getColumnIndex("vp10")),
                        cusrsor.getString(cusrsor.getColumnIndex("obp1")),
                        cusrsor.getString(cusrsor.getColumnIndex("obp2")),
                        cusrsor.getString(cusrsor.getColumnIndex("obp3")),
                        cusrsor.getString(cusrsor.getColumnIndex("obp4")),
                        cusrsor.getString(cusrsor.getColumnIndex("obp5")),
                        cusrsor.getString(cusrsor.getColumnIndex("obp6")),
                        cusrsor.getString(cusrsor.getColumnIndex("obp7")),
                        cusrsor.getString(cusrsor.getColumnIndex("obp8")),
                        cusrsor.getString(cusrsor.getColumnIndex("obp9")),
                        cusrsor.getString(cusrsor.getColumnIndex("obp10")),
                        cusrsor.getString(cusrsor.getColumnIndex("firma")),
                        cusrsor.getString(cusrsor.getColumnIndex("firma_trabajador")),
                        cusrsor.getString(cusrsor.getColumnIndex("objeciones")),
                        cusrsor.getString(cusrsor.getColumnIndex("peticiones")))
                db.close()

                AsyncTaskHandleJSON(newRevision, n_serie, context).execute(url)
            }else{

            }
        }else{

        }
    }else{

    }
}

private  class AsyncTaskHandleJSON(revision: Revision,n_serie: String,context :Context): AsyncTask<String, String, String>() {
    var rev = revision
    val context = context
    val n_serie = n_serie
    override fun onPreExecute() {
        super.onPreExecute()

    }

    override fun doInBackground(vararg url: String): String {
        return POST(url[0], rev)

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        handleJson(result, n_serie, context)
    }
}

private fun handleJson(jsonString: String? ,n_serie: String,context: Context) {
    val jsonobject = JSONObject(jsonString)
    val bbddsqlite = BBDDSQLite(context)
    val db = bbddsqlite.writableDatabase
    if(jsonobject.getInt("code")==1){
        db.delete("revisiones", "id_equipamiento= '$n_serie'", null)
        Toast.makeText(context,jsonobject.getString("message"),Toast.LENGTH_SHORT).show()
    }else{
        Toast.makeText(context,jsonobject.getString("message"),Toast.LENGTH_SHORT).show()
    }
}

private fun POST(url: String, revision: Revision): String {
    var inputStream: InputStream? = null
    var result = ""
    val httpclient = DefaultHttpClient()
    val httpPost = HttpPost(url)
    var json = revision.toString().replace("'","\"")
    val se = StringEntity(json)
    httpPost.entity = se as HttpEntity?
    httpPost.setHeader("Accept", "application/json")
    httpPost.setHeader("Content-type", "application/json")
    val httpResponse = httpclient.execute(httpPost)
    inputStream = httpResponse.entity.content
    if (inputStream != null) {
        result = convertInputStreamToString(inputStream)
    }else{
        result = "{\"message\": \"Algo salió mal.\", \"code\": -9}"
    }

    return result
}

private fun convertInputStreamToString(inputStream: InputStream): String {

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