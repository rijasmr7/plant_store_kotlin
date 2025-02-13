package com.example.plantstore.api

import com.example.plantstore.model.ApiPlant
import retrofit2.Response
import retrofit2.http.GET

interface PlantApiService {
    @GET("api/plants")
    suspend fun getPlants(): Response<List<ApiPlant>>
} 