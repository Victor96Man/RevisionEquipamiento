package com.example.revisionequipamiento.Clases

class EquipamientoItem {
    var id_equipamiento: String? = null
    var familia: String? = null
    var ubicacion: String? = null
    var fecha: String? = null
    var trabajador: String? = null

    constructor(id_equipamiento: String?, familia: String?, ubicacion: String?, fecha: String?,trabajador: String?) {
        this.id_equipamiento = id_equipamiento
        this.familia = familia
        this.ubicacion = ubicacion
        this.fecha = fecha
        this.trabajador = trabajador
    }

}