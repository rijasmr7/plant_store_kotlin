package com.example.plantstore.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.plantstore.model.Review
import com.example.plantstore.repository.ReviewRepository
import kotlinx.coroutines.launch

class ReviewViewModel(context: Context) : ViewModel() {
    private val repository = ReviewRepository(context)
    
    private val _reviews = mutableStateOf<List<Review>>(emptyList())
    val reviews: State<List<Review>> = _reviews
    
    private val _averageRating = mutableStateOf(0f)
    val averageRating: State<Float> = _averageRating

    init {
        loadReviews()
    }

    private fun loadReviews() {
        _reviews.value = repository.getAllReviews()
        _averageRating.value = repository.getAverageRating()
    }

    fun addReview(username: String, review: String, rating: Float) {
        repository.addReview(username, review, rating)
        loadReviews()
    }

    fun deleteReview(reviewId: Int) {
        repository.deleteReview(reviewId)
        loadReviews()
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReviewViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
