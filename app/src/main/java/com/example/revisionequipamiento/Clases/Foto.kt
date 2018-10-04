package com.example.revisionequipamiento.Clases

class Foto {
    var idRevision: Int = 0
    var ruta: String = ""
    var nomDes: String = ""
    var observacion: String = ""
    constructor(){
        this.idRevision = idRevision
        this.ruta = ruta
        this.nomDes = nomDes
        this.observacion = observacion
    }

    constructor(idRevision: Int, ruta: String, nomDes: String, observacion: String) {
        this.idRevision = idRevision
        this.ruta = ruta
        this.nomDes = nomDes
        this.observacion = observacion
    }



    override fun toString(): String {
        return "{'idRevision':$idRevision, 'ruta':'$ruta', 'nomDes':'$nomDes', 'observacion':'$observacion'}"
    }
}