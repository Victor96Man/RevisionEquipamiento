package com.example.revisionequipamiento.DataBase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.revisionequipamiento.DataBase.DAOs.UsuarioDAO

@Database(entities = arrayOf(UsuarioDAO::class), version = 1)
abstract class DatabaseR : RoomDatabase() {
    abstract fun DAO(): UsuarioDAO

    companion object {
        private var INSTANCE: DatabaseR? = null

        fun getInstance(context: Context): DatabaseR? {
            if (INSTANCE == null) {
                synchronized(DatabaseR::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, DatabaseR::class.java, "user.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}