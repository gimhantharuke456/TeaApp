package com.example.teaapp.model

data class CartItem(
    val id: Long = 0, // SQLite will auto-generate this ID
    val itemId: String,
    val itemName: String,
    val itemPrice: Double,
    val quantity: Int,
    val imageUrl: String
)
