package com.example.teaapp.model
import java.io.Serializable
data class ShopItem(
    val id: String,
    val name: String,
    val instockAmount: Int,
    val description: String,
    val imageUrl: String,
    val shopId: String,
    val price:Double
): Serializable