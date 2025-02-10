package com.example.plantstore.util

import android.content.Context
import com.example.plantstore.model.PlantGuideList
import com.google.gson.Gson
import java.io.InputStreamReader

object JsonReader {
    fun getPlantGuides(context: Context): PlantGuideList {
        val inputStream = context.assets.open("plant_guides.json")
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, PlantGuideList::class.java)
    }
} 