package com.robertkomarek.novenen.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.robertkomarek.novenen.model.Bibelstelle

class RepositoryBibelstelle(private val context: Context) {

    fun loadBibelstelle(): Bibelstelle? {
        // Step 1: Load JSON from assets
        val json = context.assets.open("Einheitsuebersetzung1980.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Bibelstelle>>() {}.type
        // NICHT ALLE BIBELSTELLEN LADEN SONDERN VORHER HERAUSFILTERN
        val bibelstellen = Gson().fromJson<List<Bibelstelle>>(json, type)
        
        // Step 2: Get list of image files form "assets/images_bible"
        val imageFiles = context.assets.list("images_bible") ?: emptyArray()

        // Step 3: Select a single random Bibelstelle
        val randomBibelstelle = bibelstellen.randomOrNull()

        // Step 4: Assign a random image to the selected Bibelstelle
        if (randomBibelstelle != null && imageFiles.isNotEmpty()) {
            val randomImage = imageFiles.random()
            randomBibelstelle.imagePath = "images_bible/$randomImage"
        }

        return randomBibelstelle
    }
}