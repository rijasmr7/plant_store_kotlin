package com.example.plantstore.data

import com.example.plantstore.R
import com.example.plantstore.model.TopPick

class TopPickDataSource {
    fun getTopPick(): List<TopPick> {
        return listOf(
            TopPick(1, "Adagio", 1500.00, R.drawable.adagio ),
            TopPick(2, "Andorra", 1000.00, R.drawable.andorra),
            TopPick(3, "Bougainvillea", 500.00, R.drawable.boganvilia),
            TopPick(4, "Jasmine", 300.00, R.drawable.jasmine),
            TopPick(5, "Ceylon Cinnamon", 500.00, R.drawable.cinamon),
            TopPick(6, "Coconut", 1000.00, R.drawable.coconut),
        )
    }
}