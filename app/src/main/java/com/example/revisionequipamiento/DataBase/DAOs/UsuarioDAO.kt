package com.example.revisionequipamiento.DataBase.DAOs

import android.arch.persistence.room.*
import com.example.revisionequipamiento.DataBase.Entidades.Usuarios

@Dao
interface UsuarioDAO {

    @Insert
    fun insertUser(user: Usuarios)

    @Query("SELECT * FROM usuarios")
    fun getUsuarios(): MutableList<Usuarios>

    @Query("SELECT * FROM usuarios where username like :username and password like :password")
    fun getLogin(username: String, password: String): Usuarios

    @Update
    fun updateContrasena(usuarios: Usuarios):Int
}