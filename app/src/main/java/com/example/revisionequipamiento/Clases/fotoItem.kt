package com.example.revisionequipamiento.Clases

import android.graphics.Bitmap

class fotoItem {
    var imagen: Bitmap? = null
    var observacion: String? = null

    constructor(imagen: Bitmap?, observacion: String?) {
        this.imagen = imagen
        this.observacion = observacion
    }
}