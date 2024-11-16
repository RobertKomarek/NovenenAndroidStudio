package com.robertkomarek.novenen.model

import com.robertkomarek.novenen.R
import com.robertkomarek.novenen.annotation.DrawableResource

data class Novene(
    val Novenenname: String,
    val Tag: String,
    val Tagesueberschrift: String,
    val Zeitraum: String,
    val Tagestext: String,
    val Litaneiueberschrift: String,
    val Litanei: String,
    @DrawableResource(R.drawable::class)
    val Bild: String,
    val Lfdnr: Int
)