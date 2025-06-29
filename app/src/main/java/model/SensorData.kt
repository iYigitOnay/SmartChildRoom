package com.bernacelik.akillioda.model

data class SensorData(
    val temperature: Float? = null,
    val humidity: Float? = null,
    val co2: Int? = null,
    val sleepSchedule: String,
    val emergencyPhone: String


)
