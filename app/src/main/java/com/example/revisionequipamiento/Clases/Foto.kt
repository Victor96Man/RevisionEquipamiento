package com.example.revisionequipamiento.Clases

class Foto{
    var id: Int = 0
    var idRevision: Int = 0
    var ruta: String = ""
    var nomDes: String = ""
    var observacion: String = ""



    constructor(idRevision: Int, ruta: String, nomDes: String, observacion: String) {
        this.idRevision = idRevision
        this.ruta = ruta
        this.nomDes = nomDes
        this.observacion = observacion
    }




    override fun toString(): String {
        return "'foto':{'nomDes':'$nomDes', 'observacion':'$observacion', 'ruta':'$ruta'}"
    }
}