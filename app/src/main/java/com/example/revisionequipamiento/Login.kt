package com.example.revisionequipamiento

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.revisionequipamiento.DataBase.DAOs.UsuarioDAO
import com.example.revisionequipamiento.DataBase.DatabaseR
import com.example.revisionequipamiento.DataBase.Entidades.Usuarios
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        entrar_login.setOnClickListener{
            var username = username_login.text.toString()
            var contrasena = contrasena_login.text.toString()

            DatabaseR.getInstance(this).

            //metodo que comprueba el login y descarga los datos en la base de datos
            //despues abre la pantalla principal
            /*if (soyUsuario(username,contrasena)) {
                startActivity(Intent(applicationContext, Principal::class.java))
                finish()
            }else{
                Toast.makeText(this@Login, "Campos erroneos.",Toast.LENGTH_LONG).show()
                username_login.setBackgroundColor(R.color.colorError)
                contrasena_login.setBackgroundColor(R.color.colorError)
            }*/
        }

    }
    fun soyUsuario(username :String, contrasena :String):Boolean{

        return false
    }

}
