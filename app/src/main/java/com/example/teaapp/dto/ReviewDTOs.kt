package com.example.teaapp.dto
data class CreateReviewDTO(
    val userId: String,
    val shopItemId: String,
    val reviewText: String,
    val stars: Int
)

data class CreateReviewResponse(
    val id: String,
    val userId: String,
    val shopItemId: String,
    val reviewText: String,
    val stars: Int,
    val createdAt: String
)
