package com.example.revisionequipamiento.Files

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.widget.Toast
import com.example.revisionequipamiento.Clases.*

val directorioexterno = Environment.getExternalStorageDirectory()
val DATABASE_NAME = "revisiones"
val VERSIONBBDD = 4

    class BBDDSQLite(var context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, VERSIONBBDD) {
        override fun onCreate(db: SQLiteDatabase?) {
            val CreateTableUser = "CREATE TABLE usuarios (" +
                    "id integer PRIMARY KEY ," +
                    "username VARCHAR(40)," +
                    "password VARCHAR(50)," +
                    "nombre VARCHAR(40)," +
                    "email VARCHAR(40))"

            val CreateTableFotos = "CREATE TABLE fotos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_revision INTEGER," +
                    "ruta TEXT," +
                    "nomdes VARCHAR(40)," +
                    "observacion VARCHAR(20))"

            val CreateTableMarcas = "CREATE TABLE marcas (" +
                    "id integer PRIMARY KEY ," +
                    "nombremarca VARCHAR(40))"

            val CreateTableZonas = "CREATE TABLE zonas (" +
                    "id INTEGER PRIMARY KEY ," +
                    "nombrezona VARCHAR(40))"

            val CreateTableUsuariosZonas = "CREATE TABLE usuariosZonas (" +
                    "id_usuario INTEGER ," +
                    "id_zona INTEGER )"

            val CreateTableUbicaciones = "CREATE TABLE ubicaciones (" +
                    "id INTEGER PRIMARY KEY ," +
                    "id_zona INTEGER,"+
                    "nombreubicacion VARCHAR(40))"

            val CreateTableTrabajadores = "CREATE TABLE trabajadores (" +
                    "id INTEGER PRIMARY KEY ," +
                    "nombretrabajador VARCHAR(40),"+
                    "id_zona INTEGER,"+
                    "id_ubicacion INTEGER)"

            val CreateTableFamilias = "CREATE TABLE familias (" +
                    "id INTEGER PRIMARY KEY," +
                    "nombrefamilia VARCHAR(20),"+
                    "informacion TEXT,"+
                    "diasrevisionperiodica INTEGER,"+
                    "diasrevisionreparacion INTEGER,"+
                    "pregunta1 TEXT,"+
                    "pregunta2 TEXT,"+
                    "pregunta3 TEXT,"+
                    "pregunta4 TEXT,"+
                    "pregunta5 TEXT,"+
                    "pregunta6 TEXT,"+
                    "pregunta7 TEXT,"+
                    "pregunta8 TEXT,"+
                    "pregunta9 TEXT,"+
                    "pregunta10 TEXT)"

            val CreateTableEquipamientos = "CREATE TABLE equipamientos (" +
                    "n_serie VARCHAR(40) PRIMARY KEY ," +
                    "id_familia INTEGER,"+
                    "id_marca INTEGER,"+
                    "id_ubicacion INTEGER,"+
                    "id_zona INTEGER,"+
                    "id_trabajador INTEGER,"+
                    "modelo VARCHAR(40),"+
                    "fecha_compra DATE,"+
                    "fecha_puesta_funcionamiento DATE,"+
                    "fecha_proxima_revision DATE,"+
                    "fecha_revision DATE,"+
                    "fecha_caducidad DATE,"+
                    "fecha_baja DATE,"+
                    "referencia_normativa  VARCHAR(40),"+
                    "estado  TINYINT(1),"+
                    "id_serie_reemplaza  VARCHAR(40),"+
                    "bitacora LONGTEXT,"+
                    "situacion  VARCHAR(40))"

            val CreateTableRevisiones = "CREATE TABLE revisiones (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_equipamiento VARCHAR(40),"+
                    "id_usuario INTEGER,"+
                    "fecharevision DATE,"+
                    "estado  TINYINT(1),"+
                    "enviado TINYINT(1),"+
                    "vp1 TINYINT(1),"+
                    "vp2 TINYINT(1),"+
                    "vp3 TINYINT(1),"+
                    "vp4 TINYINT(1),"+
                    "vp5 TINYINT(1),"+
                    "vp6 TINYINT(1),"+
                    "vp7 TINYINT(1),"+
                    "vp8 TINYINT(1),"+
                    "vp9 TINYINT(1),"+
                    "vp10 TINYINT(1),"+
                    "obp1 VARCHAR(30),"+
                    "obp2 VARCHAR(30),"+
                    "obp3 VARCHAR(30),"+
                    "obp4 VARCHAR(30),"+
                    "obp5 VARCHAR(30),"+
                    "obp6 VARCHAR(30),"+
                    "obp7 VARCHAR(30),"+
                    "obp8 VARCHAR(30),"+
                    "obp9 VARCHAR(30),"+
                    "obp10 VARCHAR(30),"+
                    "firma LONGTEXT,"+
                    "firma_trabajador LONGTEXT,"+
                    "objeciones VARCHAR(150),"+
                    "peticiones VARCHAR(150))"


            db?.execSQL(CreateTableUser)
            db?.execSQL(CreateTableMarcas)
            db?.execSQL(CreateTableFotos)
            db?.execSQL(CreateTableZonas)
            db?.execSQL(CreateTableUsuariosZonas)
            db?.execSQL(CreateTableUbicaciones)
            db?.execSQL(CreateTableTrabajadores)
            db?.execSQL(CreateTableFamilias)
            db?.execSQL(CreateTableEquipamientos)
            db?.execSQL(CreateTableRevisiones)


        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            context.deleteDatabase(DATABASE_NAME)
            BBDDSQLite(context)
        }

        fun insertUser(user : Usuario){
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("id",user.id)
            cv.put("username",user.username)
            cv.put("password",user.password)
            cv.put("nombre",user.nombre)
            cv.put("email",user.email)
            val result = db.insert("usuarios",null,cv)
            if(result == -1.toLong()){
                Toast.makeText(context,"Error usuario ${user.nombre}", Toast.LENGTH_SHORT).show()
            }else{

            }
            db.close()
        }

        fun updateContrasena(contrasena : String){
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("password",contrasena)
            db.update("usuarios", cv, null, null)
            db.close()
        }

        fun insertMarca(marca : Marca){
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("id",marca.id)
            cv.put("nombremarca",marca.nombre)
            val result = db.insert("marcas",null,cv)
            if(result == -1.toLong()){
                Toast.makeText(context,"Error marca ${marca.nombre}", Toast.LENGTH_SHORT).show()
            }else{

            }
            db.close()
        }

        fun insertZona(zona : Zona){
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("id",zona.id)
            cv.put("nombrezona",zona.nombre)
            val result = db.insert("zonas",null,cv)
            if(result == -1.toLong()){
                Toast.makeText(context,"Error zona ${zona.nombre}", Toast.LENGTH_SHORT).show()
            }else{

            }
            db.close()
        }

        fun insertUsuariosZona(usuZona : UsuariosZonas){
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("id_usuario",usuZona.usuario)
            cv.put("id_zona",usuZona.zona)
            val result = db.insert("usuariosZonas",null,cv)
            if(result == -1.toLong()){
                Toast.makeText(context,"Error usuzona ${usuZona.usuario} ${usuZona.zona}", Toast.LENGTH_SHORT).show()
            }else{

            }
            db.close()
        }


        fun insertUbicacion(ubicacion : Ubicacion){
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("id",ubicacion.id)
            cv.put("nombreubicacion",ubicacion.nombre)
            cv.put("id_zona",ubicacion.zona)
            val result = db.insert("ubicaciones",null,cv)
            if(result == -1.toLong()){
                Toast.makeText(context,"Error ubicacion ${ubicacion.nombre}", Toast.LENGTH_SHORT).show()
            }else{

            }
            db.close()
        }

        fun insertTrabajador(trabajador : Trabajador){
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("id",trabajador.id)
            cv.put("nombretrabajador",trabajador.nombre)
            cv.put("id_zona",trabajador.zona)
            cv.put("id_ubicacion",trabajador.ubicacion)
            val result = db.insert("trabajadores",null,cv)
            if(result == -1.toLong()){
                Toast.makeText(context,"Error trabajador ${trabajador.nombre}", Toast.LENGTH_SHORT).show()
            }else{

            }
            db.close()
        }

        fun insertFamilia(familia : Familia){
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("id",familia.id)
            cv.put("nombrefamilia",familia.nombre)
            cv.put("informacion",familia.info)
            cv.put("diasrevisionperiodica",familia.diaP)
            cv.put("diasrevisionreparacion",familia.diaR)
            cv.put("pregunta1",familia.p1)
            cv.put("pregunta2",familia.p2)
            cv.put("pregunta3",familia.p3)
            cv.put("pregunta4",familia.p4)
            cv.put("pregunta5",familia.p5)
            cv.put("pregunta6",familia.p6)
            cv.put("pregunta7",familia.p7)
            cv.put("pregunta8",familia.p8)
            cv.put("pregunta9",familia.p9)
            cv.put("pregunta10",familia.p10)
            val result = db.insert("familias",null,cv)
            if(result == -1.toLong()){
                Toast.makeText(context,"Error familia ${familia.nombre}", Toast.LENGTH_SHORT).show()
            }else{

            }
            db.close()
        }

        fun insertEquipamiento(equipamiento : Equipamiento){
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("n_serie",equipamiento.id)
            cv.put("id_familia",equipamiento.familia)
            cv.put("id_marca",equipamiento.marca)
            cv.put("id_ubicacion",equipamiento.ubicacion)
            cv.put("id_zona",equipamiento.zona)
            cv.put("modelo",equipamiento.modelo)
            cv.put("fecha_compra",equipamiento.fechaCo)
            cv.put("fecha_puesta_funcionamiento",equipamiento.fechaP)
            cv.put("fecha_proxima_revision",equipamiento.fechaPR)
            cv.put("fecha_revision",equipamiento.fechaR)
            cv.put("fecha_caducidad",equipamiento.fechaCa)
            cv.put("fecha_baja",equipamiento.fechaB)
            cv.put("referencia_normativa",equipamiento.RN)
            cv.put("estado",equipamiento.estado)
            cv.put("id_serie_reemplaza",equipamiento.id_reemplaza)
            cv.put("id_trabajador",equipamiento.trabajador)
            cv.put("bitacora",equipamiento.bitacora)
            cv.put("situacion",equipamiento.situacion)
            val result = db.insert("equipamientos",null,cv)
            if(result == -1.toLong()){
                Toast.makeText(context,"Error Equipamiento ${equipamiento.id}", Toast.LENGTH_SHORT).show()
            }else{

            }
            db.close()
        }

        fun insertRevision(revision : RevisionObjeto) :Long{
            val result :Long
            if (buscarRevision(revision.equipamiento)){
                updateRevision(revision)
                result = revision.id.toLong()
            }else{
                val db = this.writableDatabase
                val cv = ContentValues()
                cv.put("id_equipamiento",revision.equipamiento)
                cv.put("id_usuario",revision.usuario)
                cv.put("fecharevision",revision.getfR())
                cv.put("estado",revision.estado)
                cv.put("enviado",revision.enviado)
                cv.put("vp1",revision.vp1)
                cv.put("vp2",revision.vp2)
                cv.put("vp3",revision.vp3)
                cv.put("vp4",revision.vp4)
                cv.put("vp5",revision.vp5)
                cv.put("vp6",revision.vp6)
                cv.put("vp7",revision.vp7)
                cv.put("vp8",revision.vp8)
                cv.put("vp9",revision.vp9)
                cv.put("vp10",revision.vp10)
                cv.put("obp1",revision.obp1)
                cv.put("obp2",revision.obp2)
                cv.put("obp3",revision.obp3)
                cv.put("obp4",revision.obp4)
                cv.put("obp5",revision.obp5)
                cv.put("obp6",revision.obp6)
                cv.put("obp7",revision.obp7)
                cv.put("obp8",revision.obp8)
                cv.put("obp9",revision.obp9)
                cv.put("obp10",revision.obp10)
                cv.put("firma", revision.firma)
                cv.put("firma_trabajador",revision.firmaT)
                cv.put("objeciones",revision.objecione)
                cv.put("peticiones",revision.peticiones)
                result = db.insert("revisiones",null,cv)
                if(result == -1.toLong()){
                    Toast.makeText(context,"Error revision ${revision.id} ", Toast.LENGTH_SHORT).show()
                }else{

                    Toast.makeText(context,"Revision Guardada ", Toast.LENGTH_SHORT).show()
                }
                db.close()
            }
            return result

        }

        fun updateRevision(revision : RevisionObjeto){
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("fecharevision",revision.getfR())
            cv.put("estado",revision.estado)
            cv.put("vp1",revision.vp1)
            cv.put("vp2",revision.vp2)
            cv.put("vp3",revision.vp3)
            cv.put("vp4",revision.vp4)
            cv.put("vp5",revision.vp5)
            cv.put("vp6",revision.vp6)
            cv.put("vp7",revision.vp7)
            cv.put("vp8",revision.vp8)
            cv.put("vp9",revision.vp9)
            cv.put("vp10",revision.vp10)
            cv.put("obp1",revision.obp1)
            cv.put("obp2",revision.obp2)
            cv.put("obp3",revision.obp3)
            cv.put("obp4",revision.obp4)
            cv.put("obp5",revision.obp5)
            cv.put("obp6",revision.obp6)
            cv.put("obp7",revision.obp7)
            cv.put("obp8",revision.obp8)
            cv.put("obp9",revision.obp9)
            cv.put("obp10",revision.obp10)
            cv.put("firma", revision.firma)
            cv.put("firma_trabajador",revision.firmaT)
            cv.put("objeciones",revision.objecione)
            cv.put("peticiones",revision.peticiones)
            db.update("revisiones", cv, "id_equipamiento = '${revision.equipamiento}'", null)
            db.close()
            Toast.makeText(context,"Revision Actualizada ", Toast.LENGTH_SHORT).show()
        }

        fun insertFoto(foto : Foto, idRevi: Int){
            if (updateFoto(foto)){

            }else{
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put("id_revision",idRevi)
            cv.put("nomdes",foto.nomDes)
            cv.put("observacion",foto.observacion)
            cv.put("ruta",foto.ruta)
            val result = db.insert("fotos",null,cv)
            if(result == -1.toLong()){
                Toast.makeText(context,"Error foto ${foto.ruta}", Toast.LENGTH_SHORT).show()
            }else{

            }
            db.close()
            }
        }

        fun updateFoto(foto : Foto) :Boolean{
            val db = this.writableDatabase
            val cusrsor: Cursor
            var fotoS = false
            cusrsor = db.rawQuery("Select * FROM fotos WHERE fotos.id_revision= ${foto.idRevision} AND fotos.nomDes='${foto.nomDes}'", null)
            if (cusrsor != null) {
                if (cusrsor.count > 0) {
                    if (cusrsor.moveToFirst()) {
                        val id: Int = cusrsor.getInt(cusrsor.getColumnIndex("id"))
                        val cv = ContentValues()
                        cv.put("observacion",foto.observacion)
                        cv.put("ruta",foto.ruta)
                        db.update("fotos",cv,"id = ${id}", null)
                    }
                    fotoS = true
                }
            }

            db.close()

            return fotoS
        }

        fun miEquipo(n_serie: String):Boolean{
            var miEquipo = false
            val db = this.writableDatabase
            val cusrsor: Cursor
            cusrsor = db.rawQuery("Select * FROM equipamientos, usuariosZonas, usuarios WHERE equipamientos.n_serie = '${n_serie}' AND equipamientos.id_zona = usuariosZonas.id_zona AND usuariosZonas.id_usuario = usuarios.id", null)
            if (cusrsor != null) {
                if (cusrsor.count > 0) {
                    if (cusrsor.moveToFirst()) {
                    }
                    miEquipo = true
                }
            }
            return miEquipo
        }

        fun buscarRevision(n_serie: String?): Boolean {
            val db = this.writableDatabase
            val cusrsor: Cursor
            var revision = false
            cusrsor = db.rawQuery("Select * FROM revisiones WHERE revisiones.id_equipamiento= '${n_serie}'", null)
            if (cusrsor != null) {
                if (cusrsor.count > 0) {
                    if (cusrsor.moveToFirst()) {
                    }
                    revision = true
                }
            }
            return revision
        }

        fun enviarColores(n_serie: String):Int{
            var color = 3
            val db = this.writableDatabase
            val cusrsor: Cursor
            cusrsor = db.rawQuery("Select estado FROM equipamientos WHERE n_serie= '${n_serie}' AND estado = 2", null)
            if (cusrsor != null) {
                if (cusrsor.count > 0) {
                    if (cusrsor.moveToFirst()) {
                    }
                    color = 0 //Color Negro
                } else {
                    val cursor: Cursor
                    cursor = db.rawQuery("Select estado FROM equipamientos WHERE n_serie= '${n_serie}' AND fecha_proxima_revision >= date('now') AND fecha_proxima_revision <= date('now','+10 days')", null)
                    if (cursor != null) {
                        if (cursor.count > 0) {
                            if (cursor.moveToFirst()) {
                            }
                            color = 2 //Color Naranja
                        } else {
                            val cursor1: Cursor
                            cursor1 = db.rawQuery("Select estado FROM equipamientos WHERE n_serie= '${n_serie}' AND fecha_proxima_revision < date('now')", null)
                            if (cursor1 != null) {
                                if (cursor1.count > 0) {
                                    if (cursor1.moveToFirst()) {
                                    }
                                    color = 1 //Color Rojo
                                }
                            } else {

                            }
                        }
                    }
                }
            }
            return color
        }
}