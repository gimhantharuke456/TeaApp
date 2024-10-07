package com.example.teaapp.service

import com.example.teaapp.Constants.BASE_URL
import com.example.teaapp.dto.RegisterRequest
import com.example.teaapp.dto.RegisterResponse
import com.example.teaapp.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserApi {
    @POST("api/Users")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("api/Users/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): User

    @PUT("api/Users/{userId}")
    suspend fun updateProfile(@Path("userId") userId: String, @Body user: User): User
}

object UserService {
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

    private val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    suspend fun register(request: RegisterRequest): RegisterResponse {
        return userApi.register(request)
    }

    suspend fun getProfile(userId: String): User {
        return userApi.getProfile(userId)
    }

    suspend fun updateProfile(user: User): User {
        return userApi.updateProfile(user.id, user)
    }
}