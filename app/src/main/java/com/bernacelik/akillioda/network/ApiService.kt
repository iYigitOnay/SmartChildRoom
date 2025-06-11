package com.bernacelik.akillioda.com.bernacelik.akillioda.network


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class RegisterRequest(val username: String, val password: String)
data class LoginRequest(val username: String, val password: String)
data class AuthResponse(val token: String, val message: String)

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/register")
    fun registerUser(@Body request: RegisterRequest): Call<AuthResponse>

    @Headers("Content-Type: application/json")
    @POST("api/login")
    fun loginUser(@Body request: LoginRequest): Call<AuthResponse>
}


