package com.bernacelik.akillioda

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val temperatureValue = findViewById<TextView>(R.id.temperatureValue)
        val humidityValue = findViewById<TextView>(R.id.humidityValue)
        val co2Value = findViewById<TextView>(R.id.co2Value)

        // Sensörlerden aldığın değerler örnek olarak
        temperatureValue.text = "23.5 °C"
        humidityValue.text = "45 %"
        co2Value.text = "400 ppm"
    }
}
