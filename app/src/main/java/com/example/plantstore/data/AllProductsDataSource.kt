package com.example.plantstore.data

import com.example.plantstore.R
import com.example.plantstore.model.AllProducts

class AllProductsDataSource {
    fun getAllProducts(): List<AllProducts> {
        return listOf(
            AllProducts(1, "Adagio", 1500.00, R.drawable.adagio ),
            AllProducts(2, "Andorra", 1000.00, R.drawable.andorra),
            AllProducts(3, "Bougainvillea", 500.00, R.drawable.boganvilia),
            AllProducts(4, "Jasmine", 300.00, R.drawable.jasmine),
            AllProducts(5, "Ceylon Cinnamon", 500.00, R.drawable.cinamon),
            AllProducts(6, "Coconut", 1000.00, R.drawable.coconut),
            AllProducts(7, "Aloe vera", 800.00, R.drawable.aloevera ),
            AllProducts(8, "Spider plant", 1000.00, R.drawable.spider),
            AllProducts(9, "Snake plant", 500.00, R.drawable.snake),
            AllProducts(10, "ZZ plant", 1000.00, R.drawable.zz),
            AllProducts(11, "Rubber plant", 500.00, R.drawable.rubber),
            AllProducts(12, "Money plant", 300.00, R.drawable.money),
            AllProducts(13, "Banana", 900.00, R.drawable.banana ),
            AllProducts(14, "Ixora", 300.00, R.drawable.ixora),
            AllProducts(15, "Temple tree", 500.00, R.drawable.temple),
            AllProducts(16, "Hibiscus", 600.00, R.drawable.hibiscus),
            AllProducts(17, "Ceylon Ironwood", 1000.00, R.drawable.ironwood),
            AllProducts(18, "Mango", 400.00, R.drawable.mango),
            AllProducts(19, "Concrete pots", 1500.00, R.drawable.concrete ),
            AllProducts(20, "Fibre glass pots", 2000.00, R.drawable.fibreglass),
            AllProducts(21, "Plastic pots", 300.00, R.drawable.plastic),
            AllProducts(22, "Terracotta pots", 1000.00, R.drawable.terracotta),
            AllProducts(23, "Hanging pots", 500.00, R.drawable.hanging),
            AllProducts(24, "Ceramic pots", 1000.00, R.drawable.ceramic),
        )
    }
}