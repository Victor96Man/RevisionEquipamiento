package com.example.revisionequipamiento

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class BusquedaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)
    }

    override fun onBackPressed() {
        finish()
    }
}
