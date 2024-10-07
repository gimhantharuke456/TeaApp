package com.example.teaapp.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val role: String,
    val firstName: String,
    val lastName: String,
    val address: String,
    val phoneNumber: String,
    val gender: String
)