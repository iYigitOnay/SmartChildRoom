package com.bernacelik.akillioda.model

import com.google.gson.annotations.SerializedName

data class SensorData(
    @SerializedName("temperature") val temperature: Double?,
    @SerializedName("humidity") val humidity: Double?,
    @SerializedName("co2") val co2: Double?,
    val sleepSchedule: String,
    val emergencyPhone: String


)
