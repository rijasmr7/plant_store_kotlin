package com.example.plantstore.data

import com.example.plantstore.model.AllProducts
import com.example.plantstore.model.Users

class UserDataSource {
    private val users = mutableListOf<Users>()

    fun registerUser(user: Users): Boolean {
        if (isEmailTaken(user.email)) {
            println("Email already taken: ${user.email}")
            return false
        }
        users.add(user)
        println("User registered: $user")
        return true
    }

    fun isEmailTaken(email: String): Boolean {
        return users.any { it.email == email }
    }

    fun getUsers(): List<Users> {
        return listOf(
            Users("Rijas", "rijas@gmail.com", "Akurana", "1234"),
        )}
}