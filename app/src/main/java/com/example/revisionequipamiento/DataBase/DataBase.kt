package com.example.revisionequipamiento.DataBase

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(Usuarios::class), version = 1)
abstract class Database : RoomDatabase() {
    abstract fun DAO(): DAO
}