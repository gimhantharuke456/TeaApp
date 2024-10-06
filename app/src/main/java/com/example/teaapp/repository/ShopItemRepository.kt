package com.example.teaapp.repository

import com.example.teaapp.Constants
import com.example.teaapp.model.ShopItem
import com.example.teaapp.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShopItemRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun getShopItems(): Result<List<ShopItem>> {
        return try {
            val response = apiService.getShopItems()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch shop items: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}