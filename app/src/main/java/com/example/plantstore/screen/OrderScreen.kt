package com.example.plantstore.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.plantstore.components.CommonFooter
import com.example.plantstore.data.PlantDataSource
import com.example.plantstore.model.Plant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(navController: NavHostController, plantId: Int) {
    var selectedTab by remember { mutableStateOf(BottomNavItem.Plants)}

    val plantDataSource = PlantDataSource()
    val plantDetail: Plant? = plantDataSource.getPlants().firstOrNull { it.id == plantId }

    var quantity by remember { mutableStateOf(1) }
    val totalAmount = (plantDetail?.price ?: 0.0) * quantity

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Order Details") },
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
    ) { padding ->
        if (plantDetail != null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = plantDetail.imageResId),
                    contentDescription = plantDetail.name,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = plantDetail.name,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Price: Rs.${plantDetail.price}",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (quantity > 1) quantity--
                        }
                    ) {
                        Text(text = "-")
                    }

                    Text(text = "$quantity", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface,)

                    Button(
                        onClick = { quantity++ }
                    ) {
                        Text(text = "+")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Total: Rs.${String.format("%.2f", totalAmount)}",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Name")
                    }
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Delivery Address") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Address")
                    }
                )

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "City")
                    }
                )

                OutlinedTextField(
                    value = postalCode,
                    onValueChange = { postalCode = it },
                    label = { Text("Postal Code") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Postal Code")
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (name.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && city.isNotEmpty() && postalCode.isNotEmpty()) {
                            Toast.makeText(context, "You are 90% there for the successful order", Toast.LENGTH_SHORT).show()
                            navController.navigate("paymentScreen")
                        } else {
                            Toast.makeText(context, "please fill all the fields", Toast.LENGTH_SHORT).show()
                        }

                    },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Proceed to Pay", fontWeight = FontWeight.Bold)
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
