package com.example.plantstore.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import com.example.plantstore.R


@Composable
fun WishlistScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var plantName by remember { mutableStateOf("") }
    var plantSpecs by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var selectedTab by remember { mutableStateOf(BottomNavItem.Wishlist) }

    Scaffold(
        topBar = { Header(onProfileClick = { navController.navigate("startScreen") },
            onLogoClick = { navController.navigate("homeScreen") }) },
        bottomBar = {
            CustomBottomNavigationBar(selectedTab) { selectedTab = it }
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
            Text(
                text = "Your Plant Wishlist",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                ),
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = stringResource(R.string.wishlist),
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column (modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )


                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = plantName,
                    onValueChange = { plantName = it },
                    label = { Text("Plant Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = plantSpecs,
                    onValueChange = { plantSpecs = it },
                    label = { Text("Plant Specifications (optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (name.isNotEmpty() && phone.isNotEmpty() && plantName.isNotEmpty()) {
                                snackbarHostState.showSnackbar("Your wishlist is now under consideration")
                            } else {
                                snackbarHostState.showSnackbar("Please fill in all the fields.")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Submit",
                        fontWeight = FontWeight.Bold
                    )
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

            }

            BottomNavItem.Plants -> {
                navController.navigate("plantScreen") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
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
