package com.example.chocodelight.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.chocodelight.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ShopViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _chocolates = MutableStateFlow<List<Chocolate>>(emptyList())
    val chocolates: StateFlow<List<Chocolate>> = _chocolates

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart

    // ---------------- CHECKOUT FLOW STATE MANAGERS ----------------
    private val _checkoutItems = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val checkoutItems: StateFlow<List<Map<String, Any>>> = _checkoutItems.asStateFlow()

    private val _checkoutTotal = MutableStateFlow<Int>(0) // 🛠️ Changed from 0.0 to 0
    val checkoutTotal: StateFlow<Int> = _checkoutTotal.asStateFlow()

    // Flag to tell checkout() whether to wipe the cart database or not
    private var isSingleProductCheckout = false
    private var singleProductIdFallback = ""

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    // ---------------- USER PROFILE ----------------
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userPhone = MutableStateFlow("")
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()

    private val _userAddress = MutableStateFlow("")
    val userAddress: StateFlow<String> = _userAddress.asStateFlow()

    // ---------------- WISHLIST ----------------
    private val _wishlist = MutableStateFlow<List<Chocolate>>(emptyList())
    val wishlist: StateFlow<List<Chocolate>> = _wishlist.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _currentUser.value = user

            if (user != null) {
                fetchOrders(user.uid)
                fetchUserProfile(user.uid)
                fetchCartItemsPersistent(user.uid)
            } else {
                // 🛑 FIXED: We no longer wipe out data models or profile fields here on logout!
                // This ensures product reviews retain author configurations and don't break database security checks.
                Log.d("ShopViewModel", "User logged out, preserving temporary memory cache for smooth viewing experience.")
            }
        }
        fetchProducts()
        uploadLuxuryProductsBatch20()
    }
    fun uploadLuxuryProductsBatch20() {
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

        // Clean out any old broken documents to ensure a perfectly clean database sync!
        db.collection("products").get().addOnSuccessListener { snapshot ->
            for (doc in snapshot.documents) {
                doc.reference.delete()
            }

            val products = listOf(
                hashMapOf(
                    "name" to "70% Dark Chocolate Bar",
                    "description" to "Rich dark cocoa chocolate.",
                    "price" to 110,
                    "imageUrl" to "https://cdn.milenio.com/uploads/media/2025/12/12/marcas-mexicanas-chocolates-especial-discover.jpeg",
                    "category" to "Bars"
                ),
                hashMapOf(
                    "name" to "85% Extra Dark Chocolate",
                    "description" to "Intense premium cocoa bar.",
                    "price" to 125,
                    "imageUrl" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSozaHcyng8I54ZzRmmYKEJqBQOMy5sR3XYEQ&s",
                    "category" to "Bars"
                ),
                hashMapOf(
                    "name" to "White Chocolate Bar",
                    "description" to "Creamy white cocoa butter chocolate.",
                    "price" to 90,
                    "imageUrl" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSKKLBgwzGDuyxZgmvEqjGudxeROHYARIiA6g&s",
                    "category" to "Bars"
                ),
                hashMapOf(
                    "name" to "Chocolate Truffle Box",
                    "description" to "Luxury dark chocolate truffles.Set of 15",
                    "price" to 250,
                    "imageUrl" to "https://media.wired.com/photos/67af785b6d815ae74e2f76ef/3:2/w_2560%2Cc_limit/Chocolate-candy-box-GettyImages-123365972-imagedepotpro-(cropped).jpg",
                    "category" to "Boxes"
                ),
                hashMapOf(
                    "name" to "Milk Chocolate Truffles",
                    "description" to "Creamy filled truffles.",
                    "price" to 220,
                    "imageUrl" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTOSaoWXx9pKv69jvYG6vIG9YQbSj60zB7ziw&s",
                    "category" to "Truffles"
                ),
                hashMapOf(
                    "name" to "Hazelnut Chocolate Pralines",
                    "description" to "Nut-filled chocolate pralines.",
                    "price" to 180,
                    "imageUrl" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJ3wTLNsVpn8XDTzzITkdegQevKtEXtsQgPg&s",
                    "category" to "Pralines"
                ),
                hashMapOf(
                    "name" to "Chocolate Almond Bark",
                    "description" to "Crunchy almond chocolate bark.",
                    "price" to 140,
                    "imageUrl" to "https://www.namkeenwale.in/image/cache/data/moddy/rosted%20almond1_600x600-500x500.jpg",
                    "category" to "Bark"
                ),
                hashMapOf(
                    "name" to "Chocolate Fudge Squares",
                    "description" to "Soft cocoa fudge pieces.",
                    "price" to 135,
                    "imageUrl" to "https://assets.cntraveller.in/photos/698dad60e1de9224a1eb04bd/4:5/w_960,c_limit/Manam%20Chocolate_Indulgence%20&%20Snacking%20Collection_%20Photo%20Courtesy%20Lost%20&%20Hungry%20Studios_2.jpg",
                    "category" to "Fudge"
                ),
                hashMapOf(
                    "name" to "Caramel Chocolate Bar",
                    "description" to "Chocolate filled with caramel.",
                    "price" to 145,
                    "imageUrl" to "https://prd-upmarket.s3.ap-south-1.amazonaws.com/AA0001/generated/ar1x1/large/Darkmilk-Large.jpg",
                    "category" to "Bars"
                ),
                hashMapOf(
                    "name" to "Belgian Dark Chocolate",
                    "description" to "Premium Belgian cocoa chocolate.",
                    "price" to 160,
                    "imageUrl" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS3tPkNWhaFtTV4I7XtEZ-nHfv8TT9aXpcs6Q&s",
                    "category" to "Bars"
                ),
                hashMapOf(
                    "name" to "Ruby Chocolate Bar",
                    "description" to "Fruity pink chocolate.",
                    "price" to 170,
                    "imageUrl" to "https://theconfectionary.com/cdn/shop/products/mix_05_1200x1200.jpg?v=1605742010",
                    "category" to "Bars"
                ),
                hashMapOf(
                    "name" to "Coconut Chocolate Truffles",
                    "description" to "Coconut coated truffles.",
                    "price" to 190,
                    "imageUrl" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRpcsM6H7Q56HTWFd4nPq2DP0IaxG4sXXcNmw&s",
                    "category" to "Truffles"
                ),
                hashMapOf(
                    "name" to "Sea Salt Dark Chocolate",
                    "description" to "Dark chocolate with savory crystals.",
                    "price" to 130,
                    "imageUrl" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS3srP-Hn3vDeXagzh_hRcmwEmnIZdHCNQ_iw&s",
                    "category" to "Bars"
                ),
                hashMapOf(
                    "name" to "Triple Chocolate Bar",
                    "description" to "Dark milk white chocolate layers.",
                    "price" to 155,
                    "imageUrl" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJKFkw5qxfhyedOltV0KqCpvwRpSALPKKbeQ&s",
                    "category" to "Bars"
                ),
                hashMapOf(
                    "name" to "Mint Dark Chocolate",
                    "description" to "Refreshing mint chocolate.",
                    "price" to 115,
                    "imageUrl" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWk4qtWoXzbwZy3H4TxdWIyDz392YVXa6r1A&s",
                    "category" to "Bars"
                ),
                hashMapOf(
                    "name" to "Luxury Chocolate Box",
                    "description" to "Premium assorted chocolates.",
                    "price" to 300,
                    "imageUrl" to "https://www.musimmas.com/wp-content/uploads/2024/07/shutterstock_361496144-scaled.jpg",
                    "category" to "Boxes"
                ),
                hashMapOf(
                    "name" to "Chocolate Espresso Truffles",
                    "description" to "Coffee infused chocolate truffles.",
                    "price" to 200,
                    "imageUrl" to "https://images.unsplash.com/photo-1606312619070-d48b4c652a52?auto=format&fit=crop&w=800&q=80",
                    "category" to "Truffles"
                ),
                hashMapOf(
                    "name" to "Milk Chocolate Bar",
                    "description" to "Smooth creamy chocolate",
                    "price" to 95,
                    "imageUrl" to "https://images.unsplash.com/photo-1511381939415-e44015466834?auto=format&fit=crop&w=800&q=80",
                    "category" to "Bars"
                ),
                hashMapOf(
                    "name" to "Almond Dark Pralines",
                    "description" to "Roasted almond centers layered with premium dark shell.",
                    "price" to 185,
                    "imageUrl" to "https://www.indianhealthyrecipes.com/wp-content/uploads/2024/05/chocolate-burfi-recipe.jpg",
                    "category" to "Pralines"
                ),
                hashMapOf(
                    "name" to "Creamy Strips",
                    "description" to "Tasty cocos dipped inside rich cocoa glaze.",
                    "price" to 140,
                    "imageUrl" to "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=800&q=80",
                    "category" to "Boxes"
                )
            )

            // Inject everything into Firestore
            for (item in products) {
                db.collection("products").add(item)
            }
            android.util.Log.d("FIREBASE_SYNC", "Successfully uploaded all 20 premium products!")
        }
    }
    // ---------------- AUTH WITH REDIRECTION CALLBACKS ----------------
    fun registerNewUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
                onComplete(true)
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
                onComplete(false)
            }
    }

    fun loginExistingUser(email: String, password: String, onCheckComplete: (Boolean) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
                onCheckComplete(true)
            }
            .addOnFailureListener { e ->
                val friendlyMessage = when (e) {
                    is FirebaseAuthInvalidCredentialsException -> "Incorrect password. Please try again."
                    is FirebaseAuthInvalidUserException -> "Account does not exist. Please register first."
                    else -> e.message ?: "Authentication failed"
                }
                _authState.value = AuthState.Error(friendlyMessage)
                onCheckComplete(false)
            }
    }

    // ---------------- PASSWORD RESET ENGINE ----------------
    fun sendPasswordReset(email: String, onResult: (Boolean, String) -> Unit) {
        if (email.isBlank()) {
            onResult(false, "Please enter your email address first.")
            return
        }

        db.collection("users").whereEqualTo("email", email).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && !snapshot.isEmpty) {
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onResult(true, "Password reset link sent to your email!")
                            } else {
                                onResult(false, task.exception?.message ?: "Failed to send reset link.")
                            }
                        }
                } else {
                    onResult(false, "This email is not registered. Please create an account.")
                }
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Error verifying account existence.")
            }
    }

    fun logoutUser() {
        auth.signOut()
        _authState.value = AuthState.Idle
        // Keep datasets assigned in memory cache so UI components stay fully populated!
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    // ---------------- PRODUCTS ----------------
    private fun fetchProducts() {
        db.collection("products")
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents?.mapNotNull { doc ->
                    // 🛠️ FIXED: Changed .toDouble() to .toInt() and fallback 0.0 to 0
                    val price = (doc.get("price") as? Number)?.toInt() ?: 0

                    Chocolate(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        price = price, // 🔑 This will compile perfectly now!
                        imageUrl = doc.getString("imageUrl") ?: "",
                        category = doc.getString("category") ?: ""
                    )
                } ?: emptyList()

                _chocolates.value = list
            }
    }


    // ---------------- WISHLIST ----------------
    fun toggleWishlist(chocolate: Chocolate) {
        val current = _wishlist.value.toMutableList()
        val exists = current.any { it.id == chocolate.id }

        if (exists) {
            current.removeAll { it.id == chocolate.id }
        } else {
            current.add(chocolate)
        }
        _wishlist.value = current
    }

    // ---------------- PROFILE MANAGEMENT ----------------
    fun fetchUserProfile(uid: String, onComplete: () -> Unit = {}) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    _userName.value = doc.getString("name") ?: ""
                    _userPhone.value = doc.getString("phone") ?: ""
                    _userAddress.value = doc.getString("address") ?: ""
                }
                onComplete()
            }
            .addOnFailureListener { onComplete() }
    }

    fun saveProfileToFirebase(name: String, phone: String, address: String, onResult: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val email = auth.currentUser?.email ?: ""

        val userProfileMap = hashMapOf(
            "name" to name,
            "phone" to phone,
            "address" to address,
            "email" to email
        )

        db.collection("users").document(uid).set(userProfileMap)
            .addOnSuccessListener {
                _userName.value = name
                _userPhone.value = phone
                _userAddress.value = address
                onResult(true)
            }
            .addOnFailureListener { onResult(false) }
    }

    // ---------------- PERSISTENT FIREBASE CART SYSTEM ----------------
    private fun fetchCartItemsPersistent(userId: String) {
        db.collection("users").document(userId).collection("cart")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener

                val updatedCartList = snapshot.documents.mapNotNull { doc ->
                    val productId = doc.id
                    val quantity = (doc.get("quantity") as? Number)?.toInt() ?: 1
                    val matchingChocolate = _chocolates.value.find { it.id == productId }

                    matchingChocolate?.let { CartItem(it, quantity) }
                }
                _cart.value = updatedCartList
            }
    }

    fun addToCartWithQuantity(chocolate: Chocolate, quantity: Int) {
        val userId = auth.currentUser?.uid ?: return
        val cartDocRef = db.collection("users").document(userId).collection("cart").document(chocolate.id)

        cartDocRef.get().addOnSuccessListener { snapshot ->
            val structuralQuantity = if (snapshot.exists()) {
                ((snapshot.get("quantity") as? Number)?.toInt() ?: 0) + quantity
            } else {
                quantity
            }

            val data = hashMapOf("quantity" to structuralQuantity)
            cartDocRef.set(data)
        }
    }

    fun updateCartItemQuantity(chocolateId: String, newQty: Int) {
        val userId = auth.currentUser?.uid ?: return
        val cartDocRef = db.collection("users").document(userId).collection("cart").document(chocolateId)

        if (newQty <= 0) {
            cartDocRef.delete()
        } else {
            cartDocRef.update("quantity", newQty)
        }
    }

    // ---------------- PERSISTENT CONFIRMED CHECKOUTS ----------------
    // Call this when checking out from the Full Cart
    fun prepareCartCheckout() {
        isSingleProductCheckout = false
        // 🛠️ FIX: sumOf automatically calculates integers now because chocolate.price is an Int
        _checkoutTotal.value = _cart.value.sumOf { it.chocolate.price * it.quantity }
        _checkoutItems.value = _cart.value.map {
            mapOf(
                "name" to it.chocolate.name,
                "quantity" to it.quantity,
                // 🛠️ FIX: Removed unnecessary .toInt() call since the math is already Int
                "totalAmount" to (it.chocolate.price * it.quantity)
            )
        }
    }

    // Call this when clicking "Buy Now" on a Single Product from Home or Wishlist
    fun prepareSingleProductCheckout(chocolate: Chocolate, quantity: Int) {
        isSingleProductCheckout = true
        singleProductIdFallback = chocolate.id
        // 🛠️ FIX: Assigns a pure Int calculation directly to our updated state flow variable
        _checkoutTotal.value = chocolate.price * quantity
        _checkoutItems.value = listOf(
            mapOf(
                "name" to chocolate.name,
                "quantity" to quantity,
                "totalAmount" to (chocolate.price * quantity)
            )
        )
    }

    // Call this when clicking "Buy Now" on a Single Product from Home or Wishlist


    // THE UNIFIED FINAL CONFIRMATION BUTTON TRIGGER
    fun executeFinalCheckout(deliveryAddress: String, onSuccess: () -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        if (_checkoutItems.value.isEmpty()) return

        val orderId = db.collection("orders").document().id
        val orderMap = hashMapOf(
            "id" to orderId,
            "items" to _checkoutItems.value,
            "totalAmount" to _checkoutTotal.value,
            "status" to "Ordered",
            "deliveryAddress" to deliveryAddress,
            "userId" to userId,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("orders").document(orderId).set(orderMap)
            .addOnSuccessListener {
                db.collection("users").document(userId).collection("orders").document(orderId).set(orderMap)
                    .addOnSuccessListener {
                        if (!isSingleProductCheckout) {
                            // Clear full cart database batch safely if it wasn't a single product order
                            db.collection("users").document(userId).collection("cart").get()
                                .addOnSuccessListener { cartSnapshot ->
                                    val batch = db.batch()
                                    for (doc in cartSnapshot.documents) batch.delete(doc.reference)
                                    batch.commit().addOnSuccessListener {
                                        _cart.value = emptyList()
                                        onSuccess()
                                    }
                                }
                        } else {
                            onSuccess()
                        }
                    }
            }
    }

    fun placeSingleProductOrder(
        chocolateName: String,
        price: Double,
        quantity: Int,
        onSuccess: () -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        // 🔑 FIXED: Strictly reads dynamically from memory stream cache instead of assuming generic profile strings
        val fallbackAddress = _userAddress.value.ifBlank { "Address Provided On File" }

        val singleItemMap = mapOf(
            "name" to chocolateName,
            "quantity" to quantity,
            "totalAmount" to (price * quantity).toInt()
        )

        val orderId = db.collection("orders").document().id

        val orderMap = hashMapOf(
            "id" to orderId,
            "items" to listOf(singleItemMap),
            "totalAmount" to (price * quantity),
            "status" to "Packed",
            "deliveryAddress" to fallbackAddress,
            "userId" to userId,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("orders").document(orderId).set(orderMap)
            .addOnSuccessListener {
                db.collection("users").document(userId).collection("orders").document(orderId).set(orderMap)
                    .addOnSuccessListener { onSuccess() }
            }
    }

    // ---------------- ACTIVE ORDER FETCH ENGINE ----------------

     fun fetchOrders(userId: String) {
        db.collection("orders")
            .whereEqualTo("userId", userId) // 👈 FIX: This isolates the data to the logged-in user!
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents?.mapNotNull { doc ->
                    // 🛠️ FIX 1: Change to .toInt() and fallback value 0 to match Int migration
                    val total = (doc.get("totalAmount") as? Number)?.toInt() ?: 0
                    val itemsRaw = doc.get("items") as? List<Map<String, Any>> ?: emptyList()

                    Order(
                        orderId = doc.id, // 🛠️ FIX 2: Rename parameter from 'id' to 'orderId'
                        items = itemsRaw,
                        totalAmount = total,
                        status = doc.getString("status") ?: "Packed",
                        deliveryAddress = doc.getString("deliveryAddress") ?: "Provided On Profile"
                    )
                } ?: emptyList()

                _orders.value = list
            }
    }
    // ---------------- IMAGE & TEXT REVIEWS MODULE ----------------
    fun uploadProductReview(productId: String, text: String, imageUri: Uri?, onComplete: (Boolean) -> Unit) {
        // Keeps user identifier references synchronized perfectly
        val currentAuthorName = _userName.value.ifBlank { "Connoisseur" }
        val reviewId = UUID.randomUUID().toString()

        if (imageUri != null) {
            val imageRef = storage.reference.child("review_images/$productId/$reviewId.jpg")
            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        saveReviewToFirestore(productId, reviewId, currentAuthorName, text, downloadUrl.toString(), onComplete)
                    }
                }
                .addOnFailureListener { onComplete(false) }
        } else {
            saveReviewToFirestore(productId, reviewId, currentAuthorName, text, "", onComplete)
        }
    }

    private fun saveReviewToFirestore(
        productId: String,
        reviewId: String,
        author: String,
        text: String,
        imageUrl: String,
        onComplete: (Boolean) -> Unit
    ) {
        val reviewData = hashMapOf(
            "id" to reviewId,
            "authorName" to author,
            "reviewText" to text,
            "attachedImageUrl" to imageUrl,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("products").document(productId)
            .collection("reviews").document(reviewId)
            .set(reviewData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}