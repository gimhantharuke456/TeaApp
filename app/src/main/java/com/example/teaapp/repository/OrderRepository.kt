package com.example.teaapp.repository

import android.util.Log
import com.example.teaapp.Constants
import com.example.teaapp.dto.CreateOrderRequest
import com.example.teaapp.dto.OrderResponse
import com.example.teaapp.service.ApiService
import com.example.teaapp.service.LocalPrefsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderRepository(private val localPrefsService: LocalPrefsService) {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private fun getBearerToken(): String {
        val token = localPrefsService.getString("USER_TOKEN", "")

        return "Bearer $token"
    }

    suspend fun createOrder(orderRequest: CreateOrderRequest): Result<OrderResponse> {
        return try {
            Log.d("CheckoutActivity", "Token: ${getBearerToken()}")
            val response = apiService.createOrder(getBearerToken(), orderRequest)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create order: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrders(): Result<List<OrderResponse>> {
        return try {
            val response = apiService.getOrders(getBearerToken())
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch orders: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}