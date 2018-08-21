package com.example.revisionequipamiento.Clases

class Usuario {

    var id : Int = 0
    var username : String = ""
    var password : String = ""
    var nombre : String = ""
    var email : String = ""

    constructor(id:Int,username:String,password:String,nombre:String,email:String){
        this.id=id
        this.username=username
        this.password=password
        this.nombre=nombre
        this.email=email
    }

}