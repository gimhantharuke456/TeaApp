package com.example.teaapp.service
import com.example.teaapp.Constants.BASE_URL
import com.example.teaapp.dto.CreateReviewDTO
import com.example.teaapp.dto.CreateReviewResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ReviewApi {
    @POST("api/Review")
    suspend fun createReview(@Body request: CreateReviewDTO): CreateReviewResponse


    @GET("api/Review/shopitem/{itemId}")
    suspend fun getReviewsByShopId(@Path("itemId") shopId: String): List<CreateReviewResponse>
}

object ReviewService {
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

    private val reviewApi: ReviewApi by lazy {
        retrofit.create(ReviewApi::class.java)
    }

    suspend fun create(request: CreateReviewDTO): CreateReviewResponse {
        return reviewApi.createReview(request)
    }

    suspend fun getReviewsShopId(shopId: String): List<CreateReviewResponse> {
        return reviewApi.getReviewsByShopId(shopId)
    }


}