package com.example.plantstore.data

import com.example.plantstore.R
import com.example.plantstore.model.Pots

class PotsDataSource {
    fun getPots(): List<Pots> {
        return listOf(
            Pots(1, "Concrete pots", 1500.00, R.drawable.concrete ),
            Pots(2, "Fibre glass pots", 2000.00, R.drawable.fibreglass),
            Pots(3, "Plastic pots", 300.00, R.drawable.plastic),
            Pots(4, "Terracotta pots", 1000.00, R.drawable.terracotta),
            Pots(5, "Hanging pots", 500.00, R.drawable.hanging),
            Pots(6, "Ceramic pots", 1000.00, R.drawable.ceramic),
        )
    }
}