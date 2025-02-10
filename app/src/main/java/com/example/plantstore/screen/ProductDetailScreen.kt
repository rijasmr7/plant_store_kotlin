package com.example.plantstore.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.plantstore.R
import com.example.plantstore.components.CommonFooter
import com.example.plantstore.data.TopPickDataSource
import com.example.plantstore.data.IndoorDataSource
import com.example.plantstore.data.OutdoorDataSource
import com.example.plantstore.data.PotsDataSource
import com.example.plantstore.model.TopPick
import com.example.plantstore.model.Indoor
import com.example.plantstore.model.Outdoor
import com.example.plantstore.model.Pots
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavHostController, productId: Int, category: String) {
    var selectedTab by remember { mutableStateOf(BottomNavItem.Home) }
    
    val product = when (category) {
        "Top pick" -> TopPickDataSource().getTopPick().find { it.id == productId }
        "Indoor" -> IndoorDataSource().getIndoor().find { it.id == productId }
        "Outdoor" -> OutdoorDataSource().getOutdoor().find { it.id == productId }
        "Pots" -> PotsDataSource().getPots().find { it.id == productId }
        else -> null
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Column {
                CommonFooter()
                CustomBottomNavigationBar(selectedTab) { selectedTab = it }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                product == null -> {
                    Text(
                        text = "Product not found",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    val name = when (product) {
                        is TopPick -> product.name
                        is Indoor -> product.name
                        is Outdoor -> product.name
                        is Pots -> product.name
                        else -> "Unknown"
                    }

                    val price = when (product) {
                        is TopPick -> product.price
                        is Indoor -> product.price
                        is Outdoor -> product.price
                        is Pots -> product.price
                        else -> 0
                    }

                    val imageResId = when (product) {
                        is TopPick -> product.imageResId
                        is Indoor -> product.imageResId
                        is Outdoor -> product.imageResId
                        is Pots -> product.imageResId
                        else -> R.drawable.ic_launcher_foreground // fallback image
                    }

                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = name,
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = name,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Price: Rs.$price",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = when (category) {
                            "Top pick" -> "Our most popular and highly rated plants, carefully selected for their unique beauty and ease of care. These plants are customer favorites and perfect for both beginners and experienced gardeners."
                            "Indoor" -> "Perfect for adding life to your indoor spaces. These plants are specially chosen for their ability to thrive in indoor conditions with moderate light and regular indoor temperatures."
                            "Outdoor" -> "Hardy and weather-resistant plants that will beautify your garden. These plants are selected for their ability to adapt to outdoor conditions and create stunning landscape displays."
                            "Pots" -> "High-quality containers designed to provide the perfect home for your plants. Our pots come in various sizes and materials, ensuring proper drainage and root health for your plants."
                            else -> ""
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (category != "Pots") {
                        Text(
                            text = "Care Instructions",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = when (category) {
                                "Top pick" -> "• Water moderately when soil feels dry\n" +
                                        "• Place in bright, indirect sunlight\n" +
                                        "• Keep in room temperature (18-24°C)\n" +
                                        "• Fertilize monthly during growing season"
                                "Indoor" -> "• Water when top soil is dry\n" +
                                        "• Maintain humidity with regular misting\n" +
                                        "• Protect from direct sunlight\n" +
                                        "• Rotate pot weekly for even growth"
                                "Outdoor" -> "• Water deeply but infrequently\n" +
                                        "• Ensure good soil drainage\n" +
                                        "• Prune regularly for healthy growth\n" +
                                        "• Protect from extreme weather"
                                else -> ""
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    } else {
                        Text(
                            text = "Pot Features",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "• Drainage holes for proper water flow\n" +
                                    "• Durable and weather-resistant material\n" +
                                    "• UV protected to prevent fading\n" +
                                    "• Easy to clean and maintain",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Added to cart!")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Add to Cart", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                navController.navigate("productOrder/$category/$productId")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Make Order", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
} 