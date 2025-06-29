package com.bernacelik.akillioda.network

import com.bernacelik.akillioda.model.DashboardResponse
import com.bernacelik.akillioda.model.SensorData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


data class RegisterRequest(
    val userName: String,
    val childName: String,
    val childBirthDate: String,
    val emergencyContact: String,
    val sleepSchedule: String,
    val sensorData: SensorData
)


data class RegisterResponse(
    val message: String
)

interface ApiService {
    @GET("api/sensors/latest")
    fun getLatestSensorData(): Call<SensorData>

    @GET("api/user/dashboard/{userId}")
    fun getDashboardData(@Path("userId") userId: String): Call<DashboardResponse>


    @POST("register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>
}

