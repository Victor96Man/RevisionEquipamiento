package com.example.revisionequipamiento.Files

import android.content.Context
import com.example.revisionequipamiento.Clases.*
import org.json.JSONArray

fun ParseoFile(jsonString: String?, context: Context, code :Int){


    val jsonarray = JSONArray(jsonString)
    val jsonobject = jsonarray.getJSONObject(0)

    if(code ==1){
        val usuarios= jsonobject.getJSONArray("usuarios")
        val usuariosjsonobject = usuarios.getJSONObject(0)
        val idusuario = usuariosjsonobject.getInt("id")
        val username = usuariosjsonobject.getString("username")
        val contraseña = usuariosjsonobject.getString("password")
        val nombreusuario = usuariosjsonobject.getString("nombre")
        val email = usuariosjsonobject.getString("email")

        val usuario = Usuario(idusuario,username,contraseña,nombreusuario,email)
        val bbddsqlite = BBDDSQLite(context)
        bbddsqlite.insertUser(usuario)
        bbddsqlite.close()
    }


    val jsonobject1 = jsonarray.getJSONObject(1)
    val marcas= jsonobject1.getJSONArray("marcas")
    for(i in 0 until marcas.length()){
        val marcasjsonobject = marcas.getJSONObject(i)
        val idmarca = marcasjsonobject.getInt("id")
        val nombremarca = marcasjsonobject.getString("nombremarca")

        val marca = Marca(idmarca,nombremarca)
        val bbddsqlite = BBDDSQLite(context)
        bbddsqlite.insertMarca(marca)
        bbddsqlite.close()
    }
    val jsonobject2 = jsonarray.getJSONObject(2)
    val zonas= jsonobject2.getJSONArray("zonas")
    for (i in 0 until zonas.length()) {
        val zonasjsonobject = zonas.getJSONObject(i)
        val idzona = zonasjsonobject.getInt("id")
        val nombrezona = zonasjsonobject.getString("nombrezona")
        val zona = Zona(idzona,nombrezona)
        val bbddsqlite = BBDDSQLite(context)
        bbddsqlite.insertZona(zona)
        bbddsqlite.close()
    }
    val jsonobject3 = jsonarray.getJSONObject(3)
    val usuarioszonas= jsonobject3.getJSONArray("usuarioszonas")
    for (i in 0 until usuarioszonas.length()) {
        val usuarioszonasjsonobject = usuarioszonas.getJSONObject(i)
        val idusuarioz = usuarioszonasjsonobject.getInt("id_usuario")
        val idzonau = usuarioszonasjsonobject.getInt("id_zona")
        val usuarioszona = UsuariosZonas(idusuarioz,idzonau)
        val bbddsqlite = BBDDSQLite(context)
        bbddsqlite.insertUsuariosZona(usuarioszona)
        bbddsqlite.close()
    }
    val jsonobject4 = jsonarray.getJSONObject(4)
    val trabajadores= jsonobject4.getJSONArray("trabajadores")
    for (i in 0 until trabajadores.length()) {
        val trabajadoresjsonobject = trabajadores.getJSONObject(i)
        val idtrabajador = trabajadoresjsonobject.getInt("id")
        val nombretrabajador = trabajadoresjsonobject.getString("nombretrabajador")
        val idzonat = trabajadoresjsonobject.getInt("id_zona")
        val idubicaciont = trabajadoresjsonobject.getInt("id_ubicacion")
        val trabajador = Trabajador(idtrabajador,nombretrabajador,idzonat,idubicaciont)
        val bbddsqlite = BBDDSQLite(context)
        bbddsqlite.insertTrabajador(trabajador)
        bbddsqlite.close()
    }
    val jsonobject5 = jsonarray.getJSONObject(5)
    val ubicaciones= jsonobject5.getJSONArray("ubicaciones")
    for (i in 0 until ubicaciones.length()) {
        val ubicacionesjsonobject = ubicaciones.getJSONObject(i)
        val idubicacion = ubicacionesjsonobject.getInt("id")
        val idzonaub = ubicacionesjsonobject.getInt("id_zona")
        val nombreubicacion = ubicacionesjsonobject.getString("nombreubicacion")
        val ubicacion = Ubicacion(idubicacion,nombreubicacion,idzonaub)
        val bbddsqlite = BBDDSQLite(context)
        bbddsqlite.insertUbicacion(ubicacion)
        bbddsqlite.close()
    }
    val jsonobject6 = jsonarray.getJSONObject(6)
    val familias= jsonobject6.getJSONArray("familias")
    for (i in 0 until familias.length()) {
        val familiasjsonobject = familias.getJSONObject(i)
        val idfamilia = familiasjsonobject.getInt("id")
        val nombrefamilia = familiasjsonobject.getString("nombrefamilia")
        val informacion = familiasjsonobject.getString("informacion")
        val diasrevisionperiodica = familiasjsonobject.getInt("diasrevisionperiodica")
        val diasrevisionreparacion = familiasjsonobject.getInt("diasrevisionreparacion")
        val p1 = familiasjsonobject.getString("pregunta1")
        val p2 = familiasjsonobject.getString("pregunta2")
        val p3 = familiasjsonobject.getString("pregunta3")
        val p4 = familiasjsonobject.getString("pregunta4")
        val p5 = familiasjsonobject.getString("pregunta5")
        val p6 = familiasjsonobject.getString("pregunta6")
        val p7 = familiasjsonobject.getString("pregunta7")
        val p8 = familiasjsonobject.getString("pregunta8")
        val p9 = familiasjsonobject.getString("pregunta9")
        val p10 = familiasjsonobject.getString("pregunta10")
        val familia = Familia(idfamilia,nombrefamilia,informacion,diasrevisionperiodica,diasrevisionreparacion,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10)
        val bbddsqlite = BBDDSQLite(context)
        bbddsqlite.insertFamilia(familia)
        bbddsqlite.close()
    }
    val jsonobject7 = jsonarray.getJSONObject(7)
    val equipamientos= jsonobject7.getJSONArray("equipamientos")
    for (i in 0 until equipamientos.length()) {
        val equipamientosjsonobject = equipamientos.getJSONObject(i)
        val n_serieequipamiento = equipamientosjsonobject.getString("n_serie")
        val id_familiae = equipamientosjsonobject.getInt("id_familia")
        val id_marcae = equipamientosjsonobject.getInt("id_marca")
        val id_ubicacione = equipamientosjsonobject.getInt("id_ubicacion")
        val id_zonae = equipamientosjsonobject.getInt("id_zona")
        val id_trabajadore = equipamientosjsonobject.getInt("id_trabajador")
        val modelo = equipamientosjsonobject.getString("modelo")
        val fecha_compra = equipamientosjsonobject.getString("fecha_compra")
        val fecha_puesta_funcionamiento = equipamientosjsonobject.getString("fecha_puesta_funcionamiento")
        val fecha_proxima_revision = equipamientosjsonobject.getString("fecha_proxima_revision")
        val fecha_revision = equipamientosjsonobject.getString("fecha_revision")
        val fecha_caducidad = equipamientosjsonobject.getString("fecha_caducidad")
        val fecha_baja = equipamientosjsonobject.getString("fecha_baja")
        val referencia_normativa = equipamientosjsonobject.getString("referencia_normativa")
        val estadoe = equipamientosjsonobject.getInt("estado")
        val id_serie_reemplaza = equipamientosjsonobject.getString("id_serie_reemplazada")
        val bitacora = equipamientosjsonobject.getString("bitacora")
        val situacion = equipamientosjsonobject.getString("situacion")
        val equipamiento = Equipamiento(n_serieequipamiento,id_familiae,id_marcae,id_ubicacione,id_zonae,modelo,fecha_compra,fecha_puesta_funcionamiento,fecha_proxima_revision,fecha_revision,fecha_caducidad,fecha_baja,referencia_normativa,estadoe,id_serie_reemplaza,id_trabajadore,bitacora,situacion)
        val bbddsqlite = BBDDSQLite(context)
        bbddsqlite.insertEquipamiento(equipamiento)
        bbddsqlite.close()
    }
}