package com.example.plantstore.screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    NavHost(navController, startDestination = "startScreen") {
        composable("startScreen") { MainScreen(navController) }
        composable("loginScreen") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("homeScreen") {
                        popUpTo("startScreen") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("registerScreen")
                }
            )
        }
        composable("registerScreen") {
            RegisterScreen(navController) {
                navController.navigate("loginScreen") {
                    popUpTo("startScreen") { inclusive = true }
                }
            }
        }
        composable("homeScreen") { PlantStoreApp(auth = auth, navController) }
        composable("wishlistScreen") { WishlistScreen(navController) }
        composable("plantScreen") { PlantsScreen(navController) }
        composable(
            "plantDetailScreen/{plantId}",
            enterTransition = { fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = { -it }) },
            exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutVertically(targetOffsetY = { -it }) }
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId")?.toIntOrNull() ?: 0
            PlantDetailScreen(navController, plantId)
        }
        composable(
            "orderScreen/{plantId}",
            enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -it }) }
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId")?.toIntOrNull() ?: 0
            OrderScreen(navController, plantId)
        }
        composable("paymentScreen") {
            PaymentScreen(navController)
        }
        composable("appointmentScreen") {
            AppointmentScreen(navController)
        }
        composable("textUsScreen") {
            TextUsScreen(navController)
        }
        composable(
            route = "productDetail/{category}/{productId}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            ProductDetailScreen(
                navController = navController,
                productId = backStackEntry.arguments?.getInt("productId") ?: 0,
                category = backStackEntry.arguments?.getString("category") ?: ""
            )
        }
        composable(
            route = "productOrder/{category}/{productId}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            ProductOrderScreen(
                navController = navController,
                productId = backStackEntry.arguments?.getInt("productId") ?: 0,
                category = backStackEntry.arguments?.getString("category") ?: ""
            )
        }
        composable("plantGuides") {
            PlantGuidesScreen(navController)
        }
        composable(
            route = "plantGuideDetail/{guideId}",
            arguments = listOf(
                navArgument("guideId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            PlantGuideDetailScreen(
                navController = navController,
                guideId = backStackEntry.arguments?.getInt("guideId") ?: 0
            )
        }
        composable("profileScreen") {
            ProfileScreen(navController)
        }
    }
}
