package com.robertkomarek.novenen.model

import android.graphics.Bitmap

data class Bibelstelle (
    val LfdNr: String,
    val ID: String,
    val Buchname: String,
    val Buch: String,
    val Kapitel:String,
    val Kapiteltext: String,
    var imagePath: String? = null
)