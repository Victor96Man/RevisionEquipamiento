package com.example.revisionequipamiento.DataBase

import android.arch.persistence.room.*

@Dao
interface DAO {
    @Query("SELECT * FROM usuarios")
    fun getUsuarios(): MutableList<Usuarios>

    @Query("SELECT * FROM usuarios where username like :username and password like :password")
    fun getlogin(username: String, password: String): Usuarios

    @Update
    fun updateContrasena(usuarios: Usuarios):Int
}