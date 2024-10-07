package com.example.teaapp.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: String,
    val firstName: String,
    val lastName: String,
    val address: String,
    val phoneNumber: String,
    val gender: String
)

data class RegisterResponse(
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