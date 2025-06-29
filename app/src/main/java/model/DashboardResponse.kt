package com.bernacelik.akillioda.model

// DashboardResponse.kt
data class DashboardResponse(
    val userName: String,
    val childName: String,
    val childBirthDate: String,
    val sleepSchedule: String,
    val emergencyContact: String,
    val sensorData: SensorData
)

