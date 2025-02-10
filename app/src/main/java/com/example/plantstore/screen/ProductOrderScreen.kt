package com.example.plantstore.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.plantstore.data.*
import com.example.plantstore.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductOrderScreen(navController: NavHostController, productId: Int, category: String) {
    var selectedTab by remember { mutableStateOf(BottomNavItem.Home) }

    val product = when (category) {
        "Top pick" -> TopPickDataSource().getTopPick().find { it.id == productId }
        "Indoor" -> IndoorDataSource().getIndoor().find { it.id == productId }
        "Outdoor" -> OutdoorDataSource().getOutdoor().find { it.id == productId }
        "Pots" -> PotsDataSource().getPots().find { it.id == productId }
        else -> null
    }

    var quantity by remember { mutableStateOf(1) }
    val price = when (product) {
        is TopPick -> product.price
        is Indoor -> product.price
        is Outdoor -> product.price
        is Pots -> product.price
        else -> 0.0
    }
    val totalAmount = price * quantity

    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var customerAddress by remember { mutableStateOf("") }
    var customerCity by remember { mutableStateOf("") }
    var customerPostalCode by remember { mutableStateOf("") }

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
        }
    ) { padding ->
        if (product != null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val productName = when (product) {
                    is TopPick -> product.name
                    is Indoor -> product.name
                    is Outdoor -> product.name
                    is Pots -> product.name
                    else -> ""
                }

                val imageResId = when (product) {
                    is TopPick -> product.imageResId
                    is Indoor -> product.imageResId
                    is Outdoor -> product.imageResId
                    is Pots -> product.imageResId
                    else -> 0
                }

                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = productName,
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = productName,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Price: Rs.$price",
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

                    Text(
                        text = "$quantity",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

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

                TextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                TextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it },
                    label = { Text("Phone") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                )
                TextField(
                    value = customerAddress,
                    onValueChange = { customerAddress = it },
                    label = { Text("Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                TextField(
                    value = customerCity,
                    onValueChange = { customerCity = it },
                    label = { Text("City") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                TextField(
                    value = customerPostalCode,
                    onValueChange = { customerPostalCode = it },
                    label = { Text("Postal Code") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (customerName.isNotEmpty() && customerPhone.isNotEmpty() && 
                            customerAddress.isNotEmpty() && customerCity.isNotEmpty() && 
                            customerPostalCode.isNotEmpty()) {
                            navController.navigate("paymentScreen")
                        } else {
                            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Proceed to Pay", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
} 