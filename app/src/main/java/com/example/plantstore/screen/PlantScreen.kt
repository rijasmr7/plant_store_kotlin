package com.example.plantstore.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.plantstore.components.CommonFooter
import com.example.plantstore.data.PlantDataSource
import com.example.plantstore.model.ApiPlant
import com.example.plantstore.repository.PlantRepository
import com.example.plantstore.util.NetworkConnectivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PlantScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val plantRepository = remember { PlantRepository(context) }
    
    var plants by remember { mutableStateOf<List<ApiPlant>>(emptyList()) }
    var selectedTab by remember { mutableStateOf(BottomNavItem.Plants) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = plantRepository.getPlants()
                if (response.isSuccessful) {
                    plants = response.body() ?: emptyList()
                } else {
                    error = "Failed to fetch plants: ${response.message()}"
                    Toast.makeText(
                        context,
                        error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                isLoading = false
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Haala Flower Garden",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    drawerState.close()
                                    navController.navigate("plantGuides")
                                }
                            }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Plant Care Guides",
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    drawerState.close()
                                    navController.navigate("profileScreen")
                                }
                            }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Profile",
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    drawerState.close()
                                    navController.navigate("reviewScreen")
                                }
                            }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Reviews",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Header(
                    onLogoClick = { navController.navigate("homeScreen") },
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            bottomBar = {
                Column {
                    CommonFooter()
                    CustomBottomNavigationBar(selectedTab) { selectedTab = it }
                }
            },
        ) { padding ->
            Column(modifier = modifier.fillMaxSize()) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(plants) { plant ->
                            PlantCard(plant) {
                                navController.navigate("plantDetailScreen/${plant.id}")
                            }
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
    }
}

@Composable
fun PlantCard(plant: ApiPlant, onClick: () -> Unit) {
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
            AsyncImage(
                model = if (plant.image != null) {
                    "https://haalaflowergarden.xyz/storage/${plant.image}"
                } else {
                    "https://haalaflowergarden.xyz/images/placeholder.png"
                },
                contentDescription = plant.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = plant.name, fontWeight = FontWeight.Bold)
            Text(text = "Rs.${plant.price}", color = Color.Gray)
            Text(text = "${plant.size} size", color = Color.Gray)
            Text(text = "${plant.leave_color} leaves", color = Color.Gray)
            Text(
                text = if (plant.is_available == 1) "In stock" else "Arrives soon",
                color = if (plant.is_available == 1) Color.Green else Color.Red
            )
        }
    }
}
