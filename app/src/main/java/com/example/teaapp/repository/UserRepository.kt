package com.example.teaapp.repository

import com.example.teaapp.Constants
import com.example.teaapp.dto.*
import com.example.teaapp.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val response = apiService.register(RegisterRequest(username, email, password, "User"))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Registration failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}