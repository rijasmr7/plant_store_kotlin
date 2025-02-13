package com.example.plantstore.repository

import android.content.Context
import com.example.plantstore.data.ReviewManager
import com.example.plantstore.model.Review

class ReviewRepository(context: Context) {
    private val reviewManager = ReviewManager(context)

    fun addReview(username: String, review: String, rating: Float) {
        val newReview = Review(
            id = 0,
            username = username,
            review = review,
            rating = rating
        )
        reviewManager.saveReview(newReview)
    }

    fun getAllReviews(): List<Review> {
        return reviewManager.getAllReviews()
    }

    fun deleteReview(reviewId: Int) {
        reviewManager.deleteReview(reviewId)
    }

    fun getAverageRating(): Float {
        return reviewManager.getAverageRating()
    }

    fun updateReview(review: Review) {
        reviewManager.saveReview(review)
    }
}
