package com.example.teaapp.service

import com.example.teaapp.Constants.BASE_URL
import com.example.teaapp.dto.CreateOrderRequest
import com.example.teaapp.dto.OrderResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Header

interface OrderApi {
    @POST("api/Orders")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body order: CreateOrderRequest
    ): OrderResponse
}
public object OrderService {
    private val retrofit: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val orderApi: OrderApi by lazy {
        retrofit.create(OrderApi::class.java)
    }

    suspend fun createOrder(token: String, order: CreateOrderRequest): OrderResponse {
        return orderApi.createOrder("Bearer $token", order)
    }
}