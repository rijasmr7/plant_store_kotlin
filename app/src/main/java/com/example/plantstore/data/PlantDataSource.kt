package com.example.plantstore.data

import com.example.plantstore.R
import com.example.plantstore.model.Plant

class PlantDataSource {
    fun getPlants(): List<Plant> {
        return listOf(
            Plant(1, "Aloe vera", 800.00, 10, R.drawable.aloevera),
            Plant(2, "Spider plant", 1000.00, 50, R.drawable.spider),
            Plant(3, "Snake plant", 500.00, 60, R.drawable.snake),
            Plant(4, "ZZ plant", 1000.00, 30, R.drawable.zz),
            Plant(5, "Rubber plant", 500.00, 40, R.drawable.rubber),
            Plant(6, "Money plant", 300.00, 50, R.drawable.money),
            Plant(7, "Banana", 900.00,100, R.drawable.banana ),
            Plant(8, "Ixora", 300.00,70, R.drawable.ixora),
            Plant(9, "Temple tree", 500.00,20, R.drawable.temple),
            Plant(10, "Hibiscus", 600.00,10, R.drawable.hibiscus),
            Plant(11, "Ceylon Ironwood", 1000.00,20, R.drawable.ironwood),
            Plant(12, "Mango", 400.00,100, R.drawable.mango),
        )
    }
}