package com.example.plantstore.model

data class ApiPlant(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val size: String,
    val leave_color: String,
    val is_available: Int,
    val image: String?
) 