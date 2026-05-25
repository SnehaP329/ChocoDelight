package com.example.chocodelight.uicatalog

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.chocodelight.data.AuthState
import com.example.chocodelight.data.Chocolate
import com.example.chocodelight.viewmodel.ShopViewModel
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.Crossfade
import com.google.firebase.Timestamp

import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth // 👈 CRITICAL IMPORT
import java.text.SimpleDateFormat
import java.util.*


// ---------------- PREMIUM COSMETIC PALETTE ----------------
val PremiumBackgroundGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF0F0604), Color(0xFF26120B))
)
val DarkChocoBrown = Color(0xFF160A06)
val CardSurfaceColor = Color(0xFF2A150E)
val LuxuryGold = Color(0xFFE5A93C)
val CreamWhite = Color(0xFFF9F6F0)
val MutedText = Color(0xFFBCB1AE)

// ---------------- WELCOME ENTRY PAGE (CLEAN BACKGROUND) ----------------
@Composable
fun WelcomeEntryScreen(onNextClicked: () -> Unit) {
    val chocolateImages = listOf(
        "https://images.unsplash.com/photo-1541783245831-57d6fb0926d3?auto=format&fit=crop&w=1000&q=80",
        "https://images.unsplash.com/photo-1548907040-4d42b52125ca?auto=format&fit=crop&w=1000&q=80",
        "https://images.unsplash.com/photo-1511381939415-e44015466834?auto=format&fit=crop&w=1000&q=80",
        "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=1000&q=80"
    )

    var currentImageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            currentImageIndex = (currentImageIndex + 1) % chocolateImages.size
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Crossfade(
            targetState = chocolateImages[currentImageIndex],
            animationSpec = tween(durationMillis = 1500),
            modifier = Modifier.fillMaxSize()
        ) { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "Moving Chocolate Presentation",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.4f), Color(0xFF0F0604).copy(alpha = 0.85f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.dp, LuxuryGold.copy(alpha = 0.2f)), RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "CHOCO DELIGHT",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LuxuryGold,
                    letterSpacing = 3.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Got a craving?\nGrab some chocolates!!",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = CreamWhite,
                    textAlign = TextAlign.Center,
                    lineHeight = 44.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Feel the taste.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = LuxuryGold.copy(alpha = 0.8f),
                    letterSpacing = 1.sp
                )
            }

            Button(
                onClick = onNextClicked,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = DarkChocoBrown)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Grab it", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

// ---------------- INTRO SPLASH SCREEN ----------------
@Composable
fun SplashScreenIntro(onSplashFinished: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(PremiumBackgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(110.dp).background(CardSurfaceColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🍫", fontSize = 52.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "ChocoDelight", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = LuxuryGold, letterSpacing = 1.5.sp)
        }
    }
    LaunchedEffect(Unit) {
        delay(2000)
        onSplashFinished()
    }
}

// ---------------- LOGIN SCREEN ----------------
@Composable
fun LoginScreen(viewModel: ShopViewModel, onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("ChocoDelightPrefs", android.content.Context.MODE_PRIVATE)
    }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(Unit) {
        val savedEmail = sharedPreferences.getString("saved_email", "") ?: ""
        val savedPassword = sharedPreferences.getString("saved_password", "") ?: ""
        if (savedEmail.isNotBlank()) {
            email = savedEmail
            password = savedPassword
            rememberMe = true
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            if (rememberMe) {
                sharedPreferences.edit()
                    .putString("saved_email", email.trim())
                    .putString("saved_password", password.trim())
                    .apply()
            } else {
                sharedPreferences.edit()
                    .remove("saved_email")
                    .remove("saved_password")
                    .apply()
            }
            onLoginSuccess()
            viewModel.resetAuthState()
        } else if (authState is AuthState.Error) {
            Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
            viewModel.resetAuthState()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(PremiumBackgroundGradient)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🍫", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = if (isLoginMode) "Log In" else "Join ChocoDelight", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = CreamWhite)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it }, label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedLabelColor = LuxuryGold, unfocusedLabelColor = MutedText, focusedBorderColor = LuxuryGold, unfocusedBorderColor = CardSurfaceColor, focusedContainerColor = CardSurfaceColor.copy(alpha = 0.5f), unfocusedContainerColor = CardSurfaceColor.copy(alpha = 0.5f))
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else androidx.compose.ui.text.input.PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = null, tint = LuxuryGold)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedLabelColor = LuxuryGold, unfocusedLabelColor = MutedText, focusedBorderColor = LuxuryGold, unfocusedBorderColor = CardSurfaceColor, focusedContainerColor = CardSurfaceColor.copy(alpha = 0.5f), unfocusedContainerColor = CardSurfaceColor.copy(alpha = 0.5f))
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).clickable { rememberMe = !rememberMe }.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it }, colors = CheckboxDefaults.colors(checkedColor = LuxuryGold, checkmarkColor = DarkChocoBrown))
                Text("Remember me", color = MutedText, fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (authState is AuthState.Loading) {
                CircularProgressIndicator(color = LuxuryGold)
            } else {
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (isLoginMode) viewModel.loginExistingUser(email.trim(), password.trim()) { _ -> }
                        else {
                            viewModel.registerNewUser(email.trim(), password.trim()) { success ->
                                if (success) {
                                    com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                                    viewModel.resetAuthState()
                                    Toast.makeText(context, "Registration Success! Please Login.", Toast.LENGTH_SHORT).show()
                                    isLoginMode = true
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = DarkChocoBrown)
                ) {
                    Text(if (isLoginMode) "Log In" else "Register Account", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { isLoginMode = !isLoginMode }) {
                    Text(if (isLoginMode) "New? Register Here" else "Have an account? Log In", color = LuxuryGold)
                }
            }
        }
    }
}

// ---------------- PROFILE SETUP SCREEN ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: ShopViewModel,
    isFirstTimeSetup: Boolean,
    onProfileSaveSuccess: () -> Unit,
    onBackToHome: () -> Unit,
    onSetupLater: () -> Unit = {}
) {
    val context = LocalContext.current
    val savedName by viewModel.userName.collectAsState()
    val savedPhone by viewModel.userPhone.collectAsState()
    val savedAddress by viewModel.userAddress.collectAsState()

    var nameInput by remember { mutableStateOf(savedName) }
    var phoneInput by remember { mutableStateOf(savedPhone) }
    var addressInput by remember { mutableStateOf(savedAddress) }
    var isEditing by remember { mutableStateOf(isFirstTimeSetup || savedName.isBlank()) }

    LaunchedEffect(savedName, savedPhone, savedAddress) {
        if (!isEditing) {
            nameInput = savedName
            phoneInput = savedPhone
            addressInput = savedAddress
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkChocoBrown, titleContentColor = CreamWhite),
                title = { Text("Profile Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (!isFirstTimeSetup) {
                        IconButton(onClick = onBackToHome) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "", tint = CreamWhite)
                        }
                    }
                },
                actions = {
                    if (!isFirstTimeSetup && !isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = LuxuryGold)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(PremiumBackgroundGradient).padding(padding)) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Text("Shipping & Contact Records", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Full Name", color = MutedText, fontSize = 12.sp)
                    if (isEditing) {
                        OutlinedTextField(value = nameInput, onValueChange = { nameInput = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold))
                    } else {
                        Row(modifier = Modifier.fillMaxWidth().background(CardSurfaceColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)).clickable { isEditing = true }.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(savedName.ifBlank { "Not Setup Yet" }, color = CreamWhite, fontSize = 16.sp)
                            Icon(Icons.Default.Edit, null, tint = LuxuryGold, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Phone Number", color = MutedText, fontSize = 12.sp)
                    if (isEditing) {
                        OutlinedTextField(value = phoneInput, onValueChange = { phoneInput = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold))
                    } else {
                        Row(modifier = Modifier.fillMaxWidth().background(CardSurfaceColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)).clickable { isEditing = true }.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(savedPhone.ifBlank { "Not Setup Yet" }, color = CreamWhite, fontSize = 16.sp)
                            Icon(Icons.Default.Edit, null, tint = LuxuryGold, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Delivery Address", color = MutedText, fontSize = 12.sp)
                    if (isEditing) {
                        OutlinedTextField(value = addressInput, onValueChange = { addressInput = it }, modifier = Modifier.fillMaxWidth(), minLines = 3, shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold))
                    } else {
                        Row(modifier = Modifier.fillMaxWidth().background(CardSurfaceColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)).clickable { isEditing = true }.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(savedAddress.ifBlank { "Not Setup Yet" }, color = CreamWhite, fontSize = 15.sp)
                            Icon(Icons.Default.Edit, null, tint = LuxuryGold, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                if (isEditing) {
                    Button(
                        onClick = {
                            if (nameInput.isBlank() || phoneInput.isBlank() || addressInput.isBlank()) {
                                Toast.makeText(context, "Please fulfill all required fields", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.saveProfileToFirebase(nameInput.trim(), phoneInput.trim(), addressInput.trim()) { success ->
                                if (success) { isEditing = false; onProfileSaveSuccess() }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = DarkChocoBrown),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save Details", fontWeight = FontWeight.Bold)
                    }
                    if (isFirstTimeSetup) {
                        TextButton(onClick = onSetupLater, modifier = Modifier.fillMaxWidth()) {
                            Text("Skip for Now", color = LuxuryGold)
                        }
                    }
                }
            }
        }
    }
}

// ---------------- HOME SCREEN CONTENT ONLY ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ShopViewModel,
    onViewProfile: () -> Unit,
    onProductSelect: (Chocolate) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val catalogProductsList by viewModel.chocolates.collectAsState()
    val wishlistedItems by viewModel.wishlist.collectAsState()
    val savedName by viewModel.userName.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = remember(searchQuery, catalogProductsList) {
        if (searchQuery.isBlank()) catalogProductsList
        else catalogProductsList.filter { it.name.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize().background(PremiumBackgroundGradient)) {
        Column(modifier = Modifier.fillMaxWidth().background(DarkChocoBrown).padding(18.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("ChocoDelight", fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, color = LuxuryGold)

                Row(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(CardSurfaceColor).clickable { onViewProfile() }.padding(start = 10.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(savedName.ifBlank { "Profile" }.take(8), color = CreamWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Button(onClick = onLogout, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373).copy(alpha = 0.15f), contentColor = Color(0xFFEF5350)), shape = RoundedCornerShape(16.dp), contentPadding = PaddingValues(horizontal = 10.dp), modifier = Modifier.height(30.dp)) {
                        Text("Logout", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = searchQuery, onValueChange = { searchQuery = it }, placeholder = { Text("Search chocolates..", color = MutedText, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = LuxuryGold) }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold, unfocusedBorderColor = CardSurfaceColor.copy(alpha = 0.6f), focusedContainerColor = CardSurfaceColor.copy(alpha = 0.5f), unfocusedContainerColor = CardSurfaceColor.copy(alpha = 0.5f))
            )
        }

        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(14.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxSize()) {
            items(filteredProducts) { chocolate ->
                val isItemWishlisted = wishlistedItems.any { it.id == chocolate.id }
                Card(
                    onClick = { onProductSelect(chocolate) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurfaceColor)
                ) {
                    Column {
                        Box {
                            AsyncImage(model = chocolate.imageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().height(145.dp), contentScale = ContentScale.Crop)
                            IconButton(
                                onClick = {
                                    viewModel.toggleWishlist(chocolate)
                                    Toast.makeText(context, "Wishlist Updated!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).background(Color.Black.copy(alpha = 0.3f), CircleShape).size(32.dp)
                            ) {
                                Icon(imageVector = if (isItemWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = null, tint = if (isItemWishlisted) Color(0xFFEF5350) else Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(chocolate.name, color = CreamWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("₹${chocolate.price}", color = LuxuryGold, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)

                                IconButton(
                                    onClick = {
                                        viewModel.addToCartWithQuantity(chocolate, 1)
                                        Toast.makeText(context, "${chocolate.name} added to cart! 🛒", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.background(LuxuryGold, CircleShape).size(28.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add to Cart", tint = DarkChocoBrown, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- WISHLIST CONTENT ONLY ----------------
@Composable
fun WishlistScreen(
    viewModel: ShopViewModel,
    onProductSelect: (Chocolate) -> Unit,
    onNavigateToSummary: () -> Unit
) {
    val wishlistedItems by viewModel.wishlist.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(PremiumBackgroundGradient)) {
        Box(modifier = Modifier.fillMaxWidth().background(DarkChocoBrown).padding(16.dp)) {
            Text("Your Wishlist", color = CreamWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        if (wishlistedItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your wishlist is empty 🤍", color = MutedText, fontSize = 16.sp)
            }
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                items(wishlistedItems) { chocolate ->
                    Card(onClick = { onProductSelect(chocolate) }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardSurfaceColor)) {
                        Column {
                            Box {
                                AsyncImage(model = chocolate.imageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().height(130.dp), contentScale = ContentScale.Crop)
                                IconButton(onClick = { viewModel.toggleWishlist(chocolate) }, modifier = Modifier.align(Alignment.TopEnd)) {
                                    Icon(Icons.Filled.Favorite, null, tint = Color(0xFFEF5350))
                                }
                            }
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(chocolate.name, color = CreamWhite, fontWeight = FontWeight.Bold, maxLines = 1, fontSize = 14.sp)
                                Text("₹${chocolate.price}", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = {
                                        // 🔑 FIXED: Prepares single checkout and maps seamlessly into Flipkart Order Summary flow
                                        viewModel.prepareSingleProductCheckout(chocolate, 1)
                                        onNavigateToSummary()
                                    },
                                    modifier = Modifier.fillMaxWidth().height(32.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = DarkChocoBrown),
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Buy Now", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- SHOPPING CART CONTENT ONLY ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(
    viewModel: ShopViewModel,
    onNavigateToSummary: () -> Unit
) {
    val context = LocalContext.current
    val cartItems by viewModel.cart.collectAsState()
    val savedAddress by viewModel.userAddress.collectAsState()
    val savedPhone by viewModel.userPhone.collectAsState()
    val savedName by viewModel.userName.collectAsState()

    var showAddressSheet by remember { mutableStateOf(false) }
    var addressInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }

    LaunchedEffect(savedAddress, savedPhone, savedName) {
        addressInput = if (savedAddress == "Not Setup Yet" || savedAddress.isBlank()) "" else savedAddress
        phoneInput = savedPhone
        nameInput = savedName
    }

    Column(modifier = Modifier.fillMaxSize().background(PremiumBackgroundGradient)) {
        Box(modifier = Modifier.fillMaxWidth().background(DarkChocoBrown).padding(16.dp)) {
            Text("Your Cart", color = CreamWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your cart is empty", color = MutedText, fontSize = 16.sp)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(cartItems) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardSurfaceColor, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = item.chocolate.imageUrl,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                                Text(item.chocolate.name, color = CreamWhite, fontWeight = FontWeight.Bold)
                                Text("₹${item.chocolate.price} x ${item.quantity}", color = LuxuryGold)
                            }
                            IconButton(onClick = { viewModel.updateCartItemQuantity(item.chocolate.id, 0) }) {
                                Icon(Icons.Default.Delete, null, tint = Color(0xFFEF5350))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- UPDATED ADD MORE ITEMS BUTTON INSIDE SHOPPINGCARTSCREEN ---
                OutlinedButton(
                    onClick = {
                        // Safe-casts the local window context to a ComponentActivity to handle system back clicks
                        (context as? androidx.activity.ComponentActivity)?.onBackPressedDispatcher?.onBackPressed()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, LuxuryGold),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LuxuryGold)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add More Items", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        // 🔑 Prepares full cart checkout structure parameters in the background view model state
                        viewModel.prepareCartCheckout()
                        if (savedAddress.isBlank() || savedAddress == "Not Setup Yet" || savedPhone.isBlank()) {
                            showAddressSheet = true
                        } else {
                            onNavigateToSummary()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = DarkChocoBrown),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Place Order", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }

    if (showAddressSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddressSheet = false },
            containerColor = DarkChocoBrown,
            dragHandle = { BottomSheetDefaults.DragHandle(color = LuxuryGold) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Delivery Information", color = LuxuryGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Please specify where we should deliver your chocolates.", color = MutedText, fontSize = 14.sp)

                if (nameInput.isBlank()) {
                    OutlinedTextField(
                        value = nameInput, onValueChange = { nameInput = it }, label = { Text("Your Name", color = MutedText) }, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold)
                    )
                }

                OutlinedTextField(
                    value = phoneInput, onValueChange = { phoneInput = it }, label = { Text("Phone Number", color = MutedText) }, modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold)
                )

                OutlinedTextField(
                    value = addressInput, onValueChange = { addressInput = it }, label = { Text("Complete Shipping Address", color = MutedText) }, modifier = Modifier.fillMaxWidth(), minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (phoneInput.isBlank() || addressInput.isBlank() || (nameInput.isBlank() && savedName.isBlank())) {
                            Toast.makeText(context, "All delivery fields are required!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val clearName = nameInput.ifBlank { savedName }
                        viewModel.saveProfileToFirebase(clearName.trim(), phoneInput.trim(), addressInput.trim()) { success ->
                            if (success) {
                                showAddressSheet = false
                                onNavigateToSummary()
                            } else {
                                Toast.makeText(context, "Failed to verify details, please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = DarkChocoBrown),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Continue to Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

// ---------------- ORDER TRACKING CONTENT ONLY ----------------
@Composable
fun OrderTrackingScreen(viewModel: ShopViewModel) {
    // 🛠️ CRITICAL TRIGGER: Get current logged-in user ID and fetch their unique orders list
    val currentUserId = remember { FirebaseAuth.getInstance().currentUser?.uid ?: "" }

    LaunchedEffect(currentUserId) {
        if (currentUserId.isNotEmpty()) {
            viewModel.fetchOrders(currentUserId) // 👈 Calls your updated filtered function
        }
    }

    val orders by viewModel.orders.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(PremiumBackgroundGradient)) {
        Box(modifier = Modifier.fillMaxWidth().background(DarkChocoBrown).padding(16.dp)) {
            Text("Track Orders", color = CreamWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        if (orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No active orders found", color = MutedText, fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders) { order ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurfaceColor)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            // 🗓️ FLIPKART-STYLE DELIVERY DATE CALCULATOR
                            val deliveryDateText = remember(order.orderDate) {
                                val date = order.orderDate?.toDate()
                                if (date != null) {
                                    val calendar = Calendar.getInstance()
                                    calendar.time = date
                                    calendar.add(Calendar.DAY_OF_YEAR, 4) // 🚚 Estimates delivery in exactly 4 days

                                    val formatter = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
                                    formatter.format(calendar.time)
                                } else {
                                    "In 3-4 Days"
                                }
                            }

                            // Header Section
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val orderItemNames = order.items.mapNotNull { it["name"] as? String }
                                Text(
                                    text = if (orderItemNames.isNotEmpty()) orderItemNames.joinToString(", ") else "Chocolate Assortment",
                                    color = LuxuryGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1
                                )

                                // 🌟 FLIPKART DELIVERY DATE DISPLAY BADGE
                                Text(
                                    text = if (order.status == "Delivered") "Delivered" else "Delivery by $deliveryDateText",
                                    color = if (order.status == "Delivered") Color(0xFF4CAF50) else Color(0xFF2196F3),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Sub-text showing current administrative milestone status
                            Text(
                                text = "Status: ${order.status}",
                                color = MutedText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                            Spacer(modifier = Modifier.height(12.dp))

                            val trackingStages = listOf("Ordered", "Packed", "Shipped", "Delivered")
                            val currentStatusIndex = trackingStages.indexOfFirst {
                                it.equals(order.status, ignoreCase = true)
                            }.let { if (it == -1) 0 else it }

                            trackingStages.forEachIndexed { index, stage ->
                                val isPassedOrActive = index <= currentStatusIndex
                                val isCurrentStep = index == currentStatusIndex

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .background(
                                                color = if (isPassedOrActive) LuxuryGold else Color.Gray.copy(alpha = 0.3f),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isCurrentStep) {
                                            Box(modifier = Modifier.size(8.dp).background(DarkChocoBrown, CircleShape))
                                        } else if (isPassedOrActive) {
                                            Icon(Icons.Default.Check, null, tint = DarkChocoBrown, modifier = Modifier.size(12.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Text(
                                        text = stage,
                                        color = if (isCurrentStep) LuxuryGold else if (isPassedOrActive) CreamWhite else MutedText,
                                        fontSize = 15.sp,
                                        fontWeight = if (isCurrentStep) FontWeight.Bold else FontWeight.Normal
                                    )
                                }

                                if (index < trackingStages.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 9.dp)
                                            .width(2.dp)
                                            .height(16.dp)
                                            .background(if (index < currentStatusIndex) LuxuryGold else Color.Gray.copy(alpha = 0.2f))
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
// ---------------- PRODUCT DETAIL SCREEN ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    chocolate: Chocolate,
    viewModel: ShopViewModel,
    onNavigateToSummary: () -> Unit, // 🔑 FIXED: Accepting matching lambda references
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val savedAddress by viewModel.userAddress.collectAsState()
    val savedPhone by viewModel.userPhone.collectAsState()
    var showAddressSheet by remember { mutableStateOf(false) }

    var addressInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    val savedName by viewModel.userName.collectAsState()

    LaunchedEffect(savedAddress, savedPhone) {
        addressInput = if (savedAddress == "Not Setup Yet" || savedAddress.isBlank()) "" else savedAddress
        phoneInput = savedPhone
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkChocoBrown, titleContentColor = CreamWhite),
                title = { Text(chocolate.name, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = CreamWhite)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PremiumBackgroundGradient)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // 🎨 1. RESTORED PRODUCT DETAILS DISPLAY
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurfaceColor)
                ) {
                    // Displays the premium chocolate product image banner
                    AsyncImage(
                        model = chocolate.imageUrl,
                        contentDescription = chocolate.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Product Title Header Name
                Text(
                    text = chocolate.name,
                    color = CreamWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Product Cost Pricing Tag Display Label
                Text(
                    text = "₹${chocolate.price}",
                    color = LuxuryGold,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Product Description",
                    color = LuxuryGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Detailed Descriptive Body Text
                Text(
                    text = chocolate.description,
                    color = MutedText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 📦 2. THE WORKING ACCURATE BUTTON STACK COLUMN
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 🛒 BUTTON 1: ADD TO CART
                    OutlinedButton(
                        onClick = {
                            viewModel.addToCartWithQuantity(chocolate, quantity = 1)
                            Toast.makeText(context, "${chocolate.name} added to cart! 🍫", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, LuxuryGold),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = LuxuryGold)
                    ) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Add to Cart", modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Add to Cart", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    // 🛍️ BUTTON 2: BUY NOW
                    Button(
                        onClick = {
                            viewModel.prepareSingleProductCheckout(chocolate, quantity = 1)
                            onNavigateToSummary()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = DarkChocoBrown),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(20.dp))
                            Text(text = "Buy Now", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                } // Closes Button Stack Column
            } // Closes Inner Layout Column
        } // Closes Outer Box
    }

    if (showAddressSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddressSheet = false },
            containerColor = DarkChocoBrown,
            dragHandle = { BottomSheetDefaults.DragHandle(color = LuxuryGold) }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Delivery Details", color = LuxuryGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = phoneInput, onValueChange = { phoneInput = it }, label = { Text("Phone Number", color = MutedText) }, modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold)
                )

                OutlinedTextField(
                    value = addressInput, onValueChange = { addressInput = it }, label = { Text("Complete Shipping Address", color = MutedText) }, modifier = Modifier.fillMaxWidth(), minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (phoneInput.isBlank() || addressInput.isBlank()) {
                            Toast.makeText(context, "All parameters are required!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.saveProfileToFirebase(savedName.ifBlank { "User" }, phoneInput.trim(), addressInput.trim()) { success ->
                            if (success) {
                                showAddressSheet = false
                                onNavigateToSummary()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = DarkChocoBrown),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Continue to Summary", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------------- FLIPKART STYLE ORDER SUMMARY SCREEN ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    viewModel: ShopViewModel,
    onOrderConfirmed: () -> Unit,
    onNavigateToTracking: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    val checkoutItems by viewModel.checkoutItems.collectAsState()
    val totalItemPrice by viewModel.checkoutTotal.collectAsState()
    val savedAddress by viewModel.userAddress.collectAsState()
    val savedPhone by viewModel.userPhone.collectAsState()
    val savedName by viewModel.userName.collectAsState()

    var isEditingAddress by remember { mutableStateOf(false) }

    // 🛠️ FIX: Pass the saved database flows as keys to the remember block!
    var editedAddress by remember(savedAddress) { mutableStateOf(savedAddress ?: "") }
    var editedPhone by remember(savedPhone) { mutableStateOf(savedPhone ?: "") }
    var editedName by remember(savedName) { mutableStateOf(savedName ?: "") }

    LaunchedEffect(savedAddress, savedPhone, savedName) {
        if (!isEditingAddress) {
            editedAddress = savedAddress
            editedPhone = savedPhone
            editedName = savedName
        }
    }

    val deliveryCharge = if (totalItemPrice > 500 || totalItemPrice == 0) 0 else 40
    val grandTotal = totalItemPrice.toInt() + deliveryCharge

    // 🛠️ FIX: Removed the outer Column dot/brace mismatch to separate the layout scopes cleanly
    Column(modifier = Modifier.fillMaxSize().background(PremiumBackgroundGradient)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkChocoBrown)
                .padding(16.dp)
        ) {
            Text(text = "Verify Your Order Details", color = CreamWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Your cards (1. Delivery Address, 2. Items packaging, 3. Price details) go here...
            // 📍 1. DELIVERY ADDRESS CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardSurfaceColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Delivery Destination 📍", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        TextButton(onClick = {
                            if (isEditingAddress) {
                                if (editedName.isBlank() || editedPhone.isBlank() || editedAddress.isBlank()) {
                                    Toast.makeText(context, "Fields cannot be left empty!", Toast.LENGTH_SHORT).show()
                                    return@TextButton
                                }
                                viewModel.saveProfileToFirebase(editedName.trim(), editedPhone.trim(), editedAddress.trim()) { success ->
                                    if (success) isEditingAddress = false
                                }
                            } else {
                                isEditingAddress = true
                            }
                        }) {
                            Text(if (isEditingAddress) "Save Info" else "Edit Address", color = LuxuryGold, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditingAddress) {
                        OutlinedTextField(
                            value = editedName, onValueChange = { editedName = it }, label = { Text("Receiver Name", color = MutedText) },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold)
                        )
                        OutlinedTextField(
                            value = editedPhone, onValueChange = { editedPhone = it }, label = { Text("Contact Number", color = MutedText) },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold)
                        )
                        OutlinedTextField(
                            value = editedAddress, onValueChange = { editedAddress = it }, label = { Text("Shipping Address", color = MutedText) },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), minLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CreamWhite, unfocusedTextColor = CreamWhite, focusedBorderColor = LuxuryGold)
                        )
                    } else {
                        Text("Receiver: $editedName", color = CreamWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("Phone: $editedPhone", color = CreamWhite, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        // 🔑 FIXED: Changed 18.dp to 18.sp inside the final address Text component
                        Text(
                            text = if (editedAddress.isBlank() || editedAddress == "Not Setup Yet") {
                                "No explicit location provided! Click Edit to add."
                            } else {
                                editedAddress
                            },
                            color = MutedText,
                            fontSize = 13.sp,
                            lineHeight = 18.sp // 🛠️ SWAPPED FROM .dp TO .sp HERE
                        )
                    }
                }
            }

            // 📦 2. UNIVERSAL BREAKDOWN FOR CART OR SINGLE ITEM SELECTIONS
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardSurfaceColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Items Packaging Review 📦", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    checkoutItems.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item["name"]} (x${item["quantity"]})", color = CreamWhite, modifier = Modifier.weight(1f), fontSize = 14.sp)
                            Text("₹${item["totalAmount"]}", color = CreamWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 💰 3. BILLING CALCULATOR
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardSurfaceColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Price Details 💰", color = LuxuryGold, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    HorizontalDivider(color = Color.White.copy(alpha = 0.06f))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Items Subtotal", color = MutedText, fontSize = 14.sp)
                        Text("₹$totalItemPrice", color = CreamWhite, fontSize = 14.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery Fee Charges", color = MutedText, fontSize = 14.sp)
                        Text(
                            text = if (deliveryCharge == 0) "FREE" else "₹$deliveryCharge", // 🛠️ FIXED: Changed 0.0 to 0
                            color = if (deliveryCharge == 0) Color(0xFF4CAF50) else CreamWhite, // 🛠️ FIXED: Changed 0.0 to 0
                            fontSize = 14.sp
                        )
                    }

                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Amount Payable", color = CreamWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("₹$grandTotal", color = LuxuryGold, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                }
            }
        }

        // 🔘 4. ACTION BUTTON CARRIER
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFEF5350)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF5350))
            ) {
                Text("Cancel", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = {
                    // 🛠️ FIX 3: Ensure this check accurately reads your state variable
                    if (editedAddress.isBlank() || editedAddress.contains("No explicit location provided")) {
                        Toast.makeText(context, "Please complete address verification before confirmation!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Proceed to upload order map to Firebase Firestore here!
                        viewModel.executeFinalCheckout(
                            deliveryAddress = editedAddress,
                            onSuccess = {
                                onNavigateToTracking()
                            }
                        )
                    }
                },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LuxuryGold, contentColor = DarkChocoBrown),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Confirm Order", fontWeight = FontWeight.Bold)
            }
        }
    }
}