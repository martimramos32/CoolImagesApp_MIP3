package com.example.mip2tp2.data.api

import com.example.mip2tp2.BuildConfig
import com.example.mip2tp2.data.model.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UnsplashApiService {

    @GET("photos/random")
    suspend fun getRandomImages(
        @Query("count") count: Int = 10,
        @Header("Authorization") authHeader: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): List<UnsplashImage>
}
