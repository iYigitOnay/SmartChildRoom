package com.bernacelik.akillioda.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Burada ApiService importu olmalı:
import com.bernacelik.akillioda.network.ApiService

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.8:5000/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
