package com.example.plantstore.repository

import android.content.Context
import com.example.plantstore.api.PlantApiService
import com.example.plantstore.model.ApiPlant
import com.example.plantstore.util.JsonUtil
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException

class PlantRepository(private val context: Context) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://haalaflowergarden.xyz/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val plantApiService = retrofit.create(PlantApiService::class.java)

    suspend fun getPlants(): Response<List<ApiPlant>> {
        return try {
            val response = plantApiService.getPlants()
            if (!response.isSuccessful) {
                Response.success(JsonUtil.loadPlantsFromAssets(context))
            }
            response
        } catch (e: UnknownHostException) {
            Response.success(JsonUtil.loadPlantsFromAssets(context))
        } catch (e: Exception) {
            Response.success(JsonUtil.loadPlantsFromAssets(context))
        }
    }
} 