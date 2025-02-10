package com.example.plantstore.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plantstore.data.PlantDataSource
import com.example.plantstore.model.Plant
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.plantstore.components.CommonFooter


@Composable
fun PlantsScreen(navController: NavHostController) {
    val plantDataSource = PlantDataSource()
    val plants = plantDataSource.getPlants()

    var selectedTab by remember { mutableStateOf(BottomNavItem.Plants) }

    Scaffold(
        topBar = { Header(onProfileClick = { navController.navigate("startScreen") },
            onLogoClick = { navController.navigate("homeScreen") }) },
        bottomBar = {
            Column {
                CommonFooter()
                CustomBottomNavigationBar(selectedTab) { selectedTab = it }
            }
        },
    ) { padding ->
        LazyVerticalGrid(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(plants) { plant ->
                PlantCard(plant) {
                    navController.navigate("plantDetailScreen/${plant.id}")
                }
            }
        }

        when (selectedTab) {
            BottomNavItem.Home -> {
                navController.navigate("homeScreen") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }

            BottomNavItem.Wishlist -> {
                navController.navigate("wishlistScreen") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }

            BottomNavItem.Plants -> {

            }

            BottomNavItem.Appointment -> {
                navController.navigate("appointmentScreen") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }

            BottomNavItem.TextUs -> {
                navController.navigate("textUsScreen") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }
}

@Composable
fun PlantCard(plant: Plant, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = plant.imageResId),
                contentDescription = plant.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = plant.name, fontWeight = FontWeight.Bold)
            Text(text = "Rs.${plant.price}", color = Color.Gray)
            Text(text = "In stock: ${plant.inStock}", color = Color.Gray)
        }
    }
}
