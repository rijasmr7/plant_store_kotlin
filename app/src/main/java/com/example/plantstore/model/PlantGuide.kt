package com.example.plantstore.model

data class PlantGuide(
    val id: Int,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val lightRequirement: String,
    val wateringSchedule: String,
    val difficulty: String,
    val imageResId: String
)

data class PlantGuideList(
    val guides: List<PlantGuide>
) 