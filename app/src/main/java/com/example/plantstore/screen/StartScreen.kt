package com.example.plantstore.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScreen(navController: NavHostController) {
    var isLoggedIn by remember { mutableStateOf(false) }
    var isRegistering by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(auth.currentUser) {
        if (auth.currentUser != null) {
            isLoggedIn = true
            navController.navigate("homeScreen")
        } else {
            isLoggedIn = false
        }
    }

    when {
        isRegistering -> {
            RegisterScreen(
                navController = navController,
                onRegisterSuccess = {
                    isLoggedIn = true
                    isRegistering = false
                    navController.navigate("homeScreen")
                }
            )
        }
        else -> {
            LoginScreen(
                onLoginSuccess = {
                    isLoggedIn = true
                    navController.navigate("homeScreen")
                },
                onNavigateToRegister = {
                    isRegistering = true
                }
            )
        }
    }
}
