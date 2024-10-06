package com.example.teaapp.dto
data class CreateOrderRequest(
    val shopItemId: String,
    val shopId: String,
    val quantity: Int,
    val address: String,
    val status: String,
    val userId: String
)

data class OrderResponse(
    val id: String,
    val shopItemId: String,
    val shopId: String,
    val quantity: Int,
    val address: String,
    val status: String,
    val userId: String
)