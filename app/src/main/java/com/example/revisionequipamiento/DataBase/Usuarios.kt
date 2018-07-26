package com.example.revisionequipamiento.DataBase


import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "usuarios")
data class Usuarios (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id:Int = 0,

        @ColumnInfo(name="username")
        var username:String = "",

        @ColumnInfo(name="password")
        var password:String = "",

        @ColumnInfo(name="email")
        var email:String ="",

        @ColumnInfo(name="nombre")
        var nombre:String = ""
)