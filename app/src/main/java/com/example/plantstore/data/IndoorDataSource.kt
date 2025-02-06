package com.example.plantstore.data

import com.example.plantstore.R
import com.example.plantstore.model.Indoor

class IndoorDataSource {
    fun getIndoor(): List<Indoor> {
        return listOf(
            Indoor(1, "Aloe vera", 800.00, R.drawable.aloevera ),
            Indoor(2, "Spider plant", 1000.00, R.drawable.spider),
            Indoor(3, "Snake plant", 500.00, R.drawable.snake),
            Indoor(4, "ZZ plant", 1000.00, R.drawable.zz),
            Indoor(5, "Rubber plant", 500.00, R.drawable.rubber),
            Indoor(6, "Money plant", 300.00, R.drawable.money),
        )
    }
}