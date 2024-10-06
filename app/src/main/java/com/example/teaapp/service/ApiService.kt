package com.example.teaapp.service

import com.example.teaapp.dto.*
import com.example.teaapp.model.ShopItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("/api/Users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/api/Users")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("/api/ShopItems")
    suspend fun getShopItems(): Response<List<ShopItem>>

    @POST("/api/Orders")
    suspend fun createOrder(@Header("Authorization") token: String, @Body orderRequest: CreateOrderRequest): Response<OrderResponse>

    @GET("/api/Orders")
    suspend fun getOrders(@Header("Authorization") token: String,   @Header("Content-Type") contentType: String = "application/json",): Response<List<OrderResponse>>
}