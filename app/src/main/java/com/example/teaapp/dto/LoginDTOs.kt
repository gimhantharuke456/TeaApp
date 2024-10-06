package com.example.teaapp.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: Token
)

data class Token(
    val id: String,
    val role: String,
    val token: String
)