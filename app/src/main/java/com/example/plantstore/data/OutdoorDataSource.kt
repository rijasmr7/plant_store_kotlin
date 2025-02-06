package com.example.plantstore.data

import com.example.plantstore.R
import com.example.plantstore.model.Outdoor

class OutdoorDataSource {
    fun getOutdoor(): List<Outdoor> {
        return listOf(
            Outdoor(1, "Banana", 900.00, R.drawable.banana ),
            Outdoor(2, "Ixora", 300.00, R.drawable.ixora),
            Outdoor(3, "Temple tree", 500.00, R.drawable.temple),
            Outdoor(4, "Hibiscus", 600.00, R.drawable.hibiscus),
            Outdoor(5, "Ceylon Ironwood", 1000.00, R.drawable.ironwood),
            Outdoor(6, "Mango", 400.00, R.drawable.mango),
        )
    }
}