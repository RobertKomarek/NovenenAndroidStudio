package com.robertkomarek.novenen.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.robertkomarek.novenen.model.Novene

class RepositoryNovenen (private val context: Context) {

    fun loadNovenenData(context: Context): List<Novene> {
        val jsonString = context.assets.open("Novenen.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val type = object : TypeToken<List<Novene>>() {}.type

        return gson.fromJson(jsonString, type)
    }
}

