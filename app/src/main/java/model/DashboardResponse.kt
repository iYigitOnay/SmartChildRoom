package com.bernacelik.akillioda.model

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    @SerializedName("userName") val userName: String,
    @SerializedName("childName") val childName: String,
    @SerializedName("childBirthDate") val childBirthDate: String,
    @SerializedName("sleepSchedule") val sleepSchedule: String,
    @SerializedName("emergencyContact") val emergencyContact: String,
    @SerializedName("sensorData") val sensorData: SensorData
)

