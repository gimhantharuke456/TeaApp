package com.example.teaapp.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: String
)

data class RegisterResponse(
    val id: String,
    val username: String,
    val email: String,
    val role: String
)