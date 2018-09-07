package com.example.revisionequipamiento.Clases

class Equipamiento {
    var id : String =""
    var familia : Int =0
    var marca : Int =0
    var ubicacion : Int =0
    var zona : Int =0
    var modelo :String =""
    var fechaCo :String =""
    var fechaP :String =""
    var fechaPR :String =""
    var fechaR :String =""
    var fechaCa :String =""
    var fechaB :String =""
    var RN :String =""
    var estado :Int =0
    var id_reemplaza :String =""
    var trabajador :Int =0
    var bitacora :String =""
    var situacion :String =""

    constructor(id: String, familia: Int, marca: Int, ubicacion: Int, zona: Int, modelo: String, fechaCo: String, fechaP: String,fechaPR: String, fechaR: String, fechaCa: String, fechaB: String, RN: String, estado: Int, id_reemplaza: String, trabajador: Int, bitacora: String, situacion: String) {
        this.id = id
        this.familia = familia
        this.marca = marca
        this.ubicacion = ubicacion
        this.zona = zona
        this.modelo = modelo
        this.fechaCo = fechaCo
        this.fechaP = fechaP
        this.fechaPR = fechaPR
        this.fechaR = fechaR
        this.fechaCa = fechaCa
        this.fechaB = fechaB
        this.RN = RN
        this.estado = estado
        this.id_reemplaza = id_reemplaza
        this.trabajador = trabajador
        this.bitacora = bitacora
        this.situacion = situacion
    }
}