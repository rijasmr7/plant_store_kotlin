package com.example.plantstore.data

import android.content.Context
import com.example.plantstore.model.Review
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ReviewManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("reviews", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveReview(review: Review) {
        val reviews = getAllReviews().toMutableList()
        val newReview = if (review.id == 0) {
            review.copy(id = (reviews.maxOfOrNull { it.id } ?: 0) + 1)
        } else review

        val index = reviews.indexOfFirst { it.id == newReview.id }
        if (index != -1) {
            reviews[index] = newReview
        } else {
            reviews.add(newReview)
        }
        
        saveReviews(reviews)
    }

    //getting all reviews
    fun getAllReviews(): List<Review> {
        val json = sharedPreferences.getString("reviews", null) ?: return emptyList()
        val type = object : TypeToken<List<Review>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    //deleting a review
    fun deleteReview(reviewId: Int) {
        val reviews = getAllReviews().toMutableList()
        reviews.removeAll { it.id == reviewId }
        saveReviews(reviews)
    }

    //getting average rating
    fun getAverageRating(): Float {
        val reviews = getAllReviews()
        if (reviews.isEmpty()) return 0f
        return reviews.map { it.rating }.average().toFloat()
    }

    //helper to save reviews list
    private fun saveReviews(reviews: List<Review>) {
        val json = gson.toJson(reviews)
        sharedPreferences.edit().putString("reviews", json).apply()
    }
}
