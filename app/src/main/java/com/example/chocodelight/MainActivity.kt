package com.example.chocodelight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chocodelight.data.Chocolate
import com.example.chocodelight.ui.theme.ChocoDelightTheme
import com.example.chocodelight.uicatalog.*
import com.example.chocodelight.viewmodel.ShopViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val shopViewModel: ShopViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChocoDelightTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0F0604)
                ) {
                    val currentUser by shopViewModel.currentUser.collectAsState()
                    var screen by remember { mutableStateOf("welcome") } // Starts directly on welcome!
                    var selectedChocolate by remember { mutableStateOf<Chocolate?>(null) }
                    var isCheckingDatabase by remember { mutableStateOf(true) }
                    var hasClickedNext by remember { mutableStateOf(false) }

                    val wishlistedItems by shopViewModel.wishlist.collectAsState()
                    val cartItemsList by shopViewModel.cart.collectAsState()
                    val totalCartCount = cartItemsList.sumOf { it.quantity }

                    // --- OPTIMIZED FLUID SESSION SYNC ENGINE ---
                    // --- FOOLPROOF ROUTING ENGINE INSIDE MAINACTIVITY ---
                    LaunchedEffect(currentUser, hasClickedNext) {
                        val user = currentUser

                        if (user == null) {
                            isCheckingDatabase = false
                            // Only route to login if they actively tap past the slideshow
                            if (screen == "welcome" && hasClickedNext) {
                                screen = "login"
                            }
                        } else {
                            // Active Session Found -> Quietly fetch profile properties in the background
                            shopViewModel.fetchUserProfile(user.uid) {
                                isCheckingDatabase = false

                                // 🛑 THE ABSOLUTE GUARD: If they are on the welcome screen,
                                // DO NOT change the screen unless they explicitly tapped "Next View" (hasClickedNext == true)
                                if (screen == "welcome") {
                                    if (hasClickedNext) {
                                        screen = if (shopViewModel.userName.value.isNotBlank()) "home" else "forced_profile_setup"
                                    }
                                } else {
                                    // If they are already on home/cart/etc. and the app recomposes, keep them logged in smoothly
                                    if (shopViewModel.userName.value.isBlank() && screen != "forced_profile_setup") {
                                        screen = "forced_profile_setup"
                                    }
                                }
                            }
                        }
                    }

                    // The list of primary dashboard destinations where the bottom nav bar is active
                    val dashboardScreens = listOf("home", "wishlist_view", "cart", "tracking")

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.statusBars) // Keeps text safe from notifications bar overlap
                    ) {
                        if (isCheckingDatabase && screen != "welcome" && screen != "login") {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = LuxuryGold)
                            }
                        } else {
                            if (screen in dashboardScreens) {
                                Scaffold(
                                    bottomBar = {
                                        NavigationBar(
                                            containerColor = DarkChocoBrown,
                                            tonalElevation = 8.dp,
                                            modifier = Modifier.height(72.dp)
                                        ) {
                                            NavigationBarItem(
                                                selected = (screen == "home"),
                                                onClick = { screen = "home" },
                                                icon = { Icon(Icons.Default.Home, contentDescription = "Home View") },
                                                label = { Text("Home", fontSize = 11.sp) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = DarkChocoBrown, selectedTextColor = LuxuryGold, indicatorColor = LuxuryGold, unselectedIconColor = MutedText, unselectedTextColor = MutedText
                                                )
                                            )
                                            NavigationBarItem(
                                                selected = (screen == "wishlist_view"),
                                                onClick = { screen = "wishlist_view" },
                                                icon = {
                                                    BadgedBox(badge = {
                                                        if (wishlistedItems.isNotEmpty()) Badge(containerColor = LuxuryGold) {
                                                            Text(wishlistedItems.size.toString(), color = DarkChocoBrown)
                                                        }
                                                    }) {
                                                        Icon(Icons.Default.Favorite, contentDescription = "Wishlist View")
                                                    }
                                                },
                                                label = { Text("Wishlist", fontSize = 11.sp) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = DarkChocoBrown, selectedTextColor = LuxuryGold, indicatorColor = LuxuryGold, unselectedIconColor = MutedText, unselectedTextColor = MutedText
                                                )
                                            )
                                            NavigationBarItem(
                                                selected = (screen == "cart"),
                                                onClick = { screen = "cart" },
                                                icon = {
                                                    BadgedBox(badge = {
                                                        if (totalCartCount > 0) Badge(containerColor = LuxuryGold) {
                                                            Text(totalCartCount.toString(), color = DarkChocoBrown)
                                                        }
                                                    }) {
                                                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart View")
                                                    }
                                                },
                                                label = { Text("Cart", fontSize = 11.sp) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = DarkChocoBrown, selectedTextColor = LuxuryGold, indicatorColor = LuxuryGold, unselectedIconColor = MutedText, unselectedTextColor = MutedText
                                                )
                                            )
                                            NavigationBarItem(
                                                selected = (screen == "tracking"),
                                                onClick = { screen = "tracking" },
                                                icon = { Icon(Icons.Default.List, contentDescription = "Orders View") },
                                                label = { Text("Orders", fontSize = 11.sp) },
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = DarkChocoBrown, selectedTextColor = LuxuryGold, indicatorColor = LuxuryGold, unselectedIconColor = MutedText, unselectedTextColor = MutedText
                                                )
                                            )
                                        }
                                    }
                                ) { innerPadding ->
                                    Box(modifier = Modifier.padding(innerPadding)) {
                                        BackHandler(enabled = screen != "home") { screen = "home" }
                                        when (screen) {
                                            "home" -> HomeScreen(
                                                viewModel = shopViewModel,
                                                onViewProfile = { screen = "profile_view" },
                                                onProductSelect = { chocolateItem ->
                                                    selectedChocolate = chocolateItem
                                                    screen = "detail"
                                                },
                                                onLogout = {
                                                    shopViewModel.logoutUser()
                                                    hasClickedNext = false
                                                    screen = "login"
                                                }
                                            )

                                            "wishlist_view" -> WishlistScreen(
                                                viewModel = shopViewModel,
                                                onProductSelect = { chocolateItem ->
                                                    selectedChocolate = chocolateItem
                                                    screen = "detail"
                                                },
                                                // 🔑 FIXED: Change this parameter name from onCheckoutDirectly to onNavigateToSummary
                                                onNavigateToSummary = {
                                                    screen = "order_summary"
                                                }
                                            )

                                            "cart" -> ShoppingCartScreen(
                                                viewModel = shopViewModel,
                                                // 🔑 Updates from previous instantaneous direct order to summary gate redirection
                                                onNavigateToSummary = { screen = "order_summary" }
                                            )

                                            "tracking" -> OrderTrackingScreen(
                                                viewModel = shopViewModel
                                            )
                                        }
                                    }
                                }
                            } else {
                                // Independent Entry Workspaces
                                when (screen) {
                                    "welcome" -> WelcomeEntryScreen(
                                        onNextClicked = {
                                            hasClickedNext = true
                                            if (FirebaseAuth.getInstance().currentUser != null) {
                                                screen = if (shopViewModel.userName.value.isNotBlank()) "home" else "forced_profile_setup"
                                            } else {
                                                screen = "login"
                                            }
                                        }
                                    )

                                    "login" -> LoginScreen(
                                        viewModel = shopViewModel,
                                        onLoginSuccess = { screen = "home" }
                                    )

                                    // 🔑 NEW STANDALONE FLIPKART-STYLE ORDER SUMMARY SCREEN GATE
                                    "order_summary" -> {
                                        BackHandler { screen = "cart" }
                                        OrderSummaryScreen(
                                            viewModel = shopViewModel,
                                            onNavigateToTracking = { screen = "tracking" },
                                            onOrderConfirmed = { screen = "tracking" },
                                            onCancel = { screen = "cart" }
                                        )
                                    }

                                    "forced_profile_setup" -> UserProfileScreen(
                                        viewModel = shopViewModel,
                                        isFirstTimeSetup = true,
                                        onProfileSaveSuccess = { screen = "home" },
                                        onSetupLater = { screen = "home" },
                                        onBackToHome = { screen = "home" }
                                    )

                                    "profile_view" -> {
                                        BackHandler { screen = "home" }
                                        UserProfileScreen(
                                            viewModel = shopViewModel,
                                            isFirstTimeSetup = false,
                                            onProfileSaveSuccess = { screen = "home" },
                                            onBackToHome = { screen = "home" }
                                        )
                                    }

                                    "detail" -> selectedChocolate?.let { chocolateItem ->
                                        BackHandler { screen = "home" }
                                        ProductDetailScreen(
                                            chocolate = chocolateItem,
                                            viewModel = shopViewModel,
                                            onNavigateToSummary = { screen = "order_summary" }, // 🔑 ADD THIS LINE HERE
                                            onBack = { screen = "home" }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}