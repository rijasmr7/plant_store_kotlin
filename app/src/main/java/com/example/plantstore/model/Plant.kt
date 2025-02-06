package com.example.plantstore.model

data class Plant(
    val id: Int,
    val name: String,
    val price: Double,
    val inStock: Int,
    val imageResId: Int
)
