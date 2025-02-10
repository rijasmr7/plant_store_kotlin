package com.example.plantstore.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.plantstore.R
import com.example.plantstore.components.CommonFooter
import com.example.plantstore.data.AllProductsDataSource
import com.example.plantstore.data.IndoorDataSource
import com.example.plantstore.data.OutdoorDataSource
import com.example.plantstore.data.PotsDataSource
import com.example.plantstore.data.TopPickDataSource
import com.example.plantstore.model.AllProducts
import com.example.plantstore.model.Indoor
import com.example.plantstore.model.Outdoor
import com.example.plantstore.model.Pots
import com.example.plantstore.model.TopPick
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PlantStoreApp(auth: FirebaseAuth, navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val isSearching = searchQuery.isNotEmpty()
    var selectedCategory by remember { mutableStateOf("Top pick") }

    var selectedTab by remember { mutableStateOf(BottomNavItem.Home) }
    Scaffold(
        topBar = {
            Header(
                onProfileClick = {
                    auth.signOut()
                    navController.navigate("loginScreen") {
                        popUpTo("startScreen") { inclusive = true }
                    }
                },
                onLogoClick = { navController.navigate("homeScreen") }
            )
        },
        bottomBar = {
            Column {
                CommonFooter()
                CustomBottomNavigationBar(selectedTab) { selectedTab = it }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            MainImageSection(navController)
            SearchBar(onSearch = { query -> searchQuery = query })

            when (selectedTab) {
                BottomNavItem.Home -> {
                    if (isSearching) {
                        SearchedProducts(searchQuery)
                    } else {
                        CategoryTabs(
                            selectedCategory = selectedCategory,
                            onCategorySelected = { category -> selectedCategory = category }
                        )
                        ProductList(selectedCategory)
                    }
                }
                BottomNavItem.Wishlist -> navController.navigate("wishlistScreen") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
                BottomNavItem.Plants -> navController.navigate("plantScreen") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
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



@Composable
fun CustomBottomNavigationBar(selectedTab: BottomNavItem, onTabSelected: (BottomNavItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavItem.values().forEach { item ->
            Column(
                modifier = Modifier
                    .clickable { onTabSelected(item) }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = item.icon, contentDescription = item.title, tint = Color.White)
                Text(text = item.title, color = Color.White)
            }
        }
    }
}

enum class BottomNavItem(val title: String, val icon: ImageVector) {
    Home("Home", Icons.Default.Home),
    Wishlist("Wishlist", Icons.Default.Favorite),
    Plants("Plants", Icons.Default.ShoppingCart),
    Appointment("Appointment", Icons.Default.DateRange),
    TextUs("Text Us", Icons.Default.MailOutline)
}



@Composable
fun Header(onProfileClick: () -> Unit,
           onLogoClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(300.dp)
                .clickable {onLogoClick() })

        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = "Profile",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .clickable {onProfileClick() }
        )
    }
}
@Composable
fun MainImageSection(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(5.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.main_image),
            contentDescription = "Main Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
        )


        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Discover Natural Beauty",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { navController.navigate("plantScreen"){
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            } }
                , colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                )) {
                Text(text = "Shop Now",
                    fontWeight = FontWeight.Bold)

            }
        }
    }
}
@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var searchInput by remember { mutableStateOf("") }

    TextField(
        value = searchInput,
        onValueChange = { searchInput = it },
        label = { Text(stringResource(id = R.string.search_label)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )

    LaunchedEffect(searchInput) {
        onSearch(searchInput)
    }
}

@Composable
fun SearchedProducts(searchQuery: String) {
    val allProducts = AllProductsDataSource().getAllProducts()
    val filteredProducts = allProducts.filter { product ->
        product.name.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (filteredProducts.isEmpty()) {
            Text(text = "No products found for \"$searchQuery\"", fontSize = 16.sp)
        } else {
            filteredProducts.forEach { product ->
                ProductCardSearch(product)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProductCardSearch(product: AllProducts) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = product.name, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface,)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Rs. ${product.price}", fontSize = 16.sp)
            }
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Composable
fun CategoryTabs(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        CategoryTab(label = "Top pick", isSelected = selectedCategory == "Top pick", onClick = { onCategorySelected("Top pick") })
        CategoryTab(label = "Indoor", isSelected = selectedCategory == "Indoor", onClick = { onCategorySelected("Indoor") })
        CategoryTab(label = "Outdoor", isSelected = selectedCategory == "Outdoor", onClick = { onCategorySelected("Outdoor") })
        CategoryTab(label = "Pots", isSelected = selectedCategory == "Pots", onClick = { onCategorySelected("Pots") })
    }
}

@Composable
fun CategoryTab(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = label,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    )
}


@Composable
fun ProductList(selectedCategory: String) {
    val products = when (selectedCategory) {
        "Top pick" -> TopPickDataSource().getTopPick()
        "Indoor" -> IndoorDataSource().getIndoor()
        "Outdoor" -> OutdoorDataSource().getOutdoor()
        "Pots" -> PotsDataSource().getPots()
        else -> TopPickDataSource().getTopPick()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        products.forEach { product ->
            ProductCard(product)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun ProductCard(product: Any) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
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
                else -> 0
            }

            Column {
                Text(text = name, fontSize = 20.sp,color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Rs. $price", fontSize = 20.sp)
            }
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = name,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}



