package com.example.plantstore.util

import android.content.Context
import com.example.plantstore.model.ApiPlant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object JsonUtil {
    fun loadPlantsFromAssets(context: Context): List<ApiPlant> {
        return try {
            val jsonString = context.assets.open("offline_plants.json")
                .bufferedReader()
                .use { it.readText() }
            
            val gson = Gson()
            val plantsType = object : TypeToken<Map<String, List<ApiPlant>>>() {}.type
            val plantsMap: Map<String, List<ApiPlant>> = gson.fromJson(jsonString, plantsType)
            plantsMap["plants"] ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
} 