package com.example.revisionequipamiento.Clases

class Trabajador {
    var id : Int =0
    var nombre :String =""
    var zona: Int =0
    var ubicacion: Int =0

    constructor(id: Int, nombre: String, zona: Int, ubicacion: Int) {
        this.id = id
        this.nombre = nombre
        this.zona = zona
        this.ubicacion = ubicacion
    }
}