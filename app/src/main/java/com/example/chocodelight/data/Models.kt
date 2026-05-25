package com.example.chocodelight.data
import com.google.firebase.Timestamp


// 🍫 Blueprint configuration for a single chocolate card
data class Chocolate(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0, // 👈 Must be Double!
    val imageUrl: String = "",
    val category: String = ""
)


// 🛒 Keeps track of how many items a customer stacks inside their cart
data class CartItem(
    val chocolate: Chocolate = Chocolate(),
    val quantity: Int = 1
)

// 🚚 Blueprints configuration for tracking a placed customer purchase

data class Order(
    val orderId: String = "",
    val items: List<Map<String, Any>> = emptyList(),
    val totalAmount: Int = 0,
    val status: String = "Ordered", // e.g., "Ordered", "Shipped", "Out for Delivery", "Delivered"
    val deliveryAddress: String = "",
    val orderDate: Timestamp? = null // 🔑 Add this to track the exact creation date
)