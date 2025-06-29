package com.bernacelik.akillioda

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bernacelik.akillioda.model.DashboardResponse
import com.bernacelik.akillioda.network.ApiService
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DashboardActivity : AppCompatActivity() {

    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvCO2: TextView
    private lateinit var tvSleepSchedule: TextView
    private lateinit var tvEmergencyPhone: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var tvBirthdayCountdown: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvCO2 = findViewById(R.id.tvCO2)
        tvSleepSchedule = findViewById(R.id.tvSleepSchedule)
        tvEmergencyPhone = findViewById(R.id.tvEmergencyPhone)
        tvWelcome = findViewById(R.id.textWelcome)
        tvBirthdayCountdown = findViewById(R.id.textBirthday)

        val userId = intent.getStringExtra("userId") ?: run {
            Toast.makeText(this, "User ID bulunamadı!", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        fetchDashboardData(userId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchDashboardData(userId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.8:5000/") // kendi backend IP + port
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getDashboardData(userId).enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                Log.d("DashboardActivity", "Response code: ${response.code()}")
                Log.d("DashboardActivity", "Response body: ${response.body()}")

                if (response.isSuccessful && response.body() != null) {
                    val dashboard = response.body()!!

                    tvWelcome.text = "Merhaba ${dashboard.childName}'nın ebeveyni ${dashboard.userName}!"
                    tvEmergencyPhone.text = "Acil Kişi: ${dashboard.emergencyContact}"

                    tvTemperature.text = "${dashboard.sensorData.temperature ?: "--"} °C"
                    tvHumidity.text = "${dashboard.sensorData.humidity ?: "--"} %"
                    tvCO2.text = "${dashboard.sensorData.co2 ?: "--"} ppm"

                    // Uyku saati kalan süre hesabı
                    try {
                        val formatter = DateTimeFormatter.ofPattern("HH:mm")
                        val sleepTime = LocalTime.parse(dashboard.sleepSchedule, formatter)
                        val now = LocalTime.now()
                        var duration = Duration.between(now, sleepTime)
                        if (duration.isNegative) {
                            duration = duration.plusHours(24)
                        }
                        val hours = duration.toHours()
                        val minutes = duration.minusHours(hours).toMinutes()
                        tvSleepSchedule.text = "Yatma Saati: ${dashboard.sleepSchedule} ($hours saat $minutes dk kaldı)"
                    } catch (e: Exception) {
                        tvSleepSchedule.text = "Uyku saati geçersiz"
                    }

                    // Doğum gününe kaç gün kaldı hesabı
                    tvBirthdayCountdown.text = try {
                        val today = LocalDate.now()
                        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        val birthDate = LocalDate.parse(dashboard.childBirthDate, formatter)
                        var nextBirthday = birthDate.withYear(today.year)

                        if (nextBirthday.isBefore(today) || nextBirthday.isEqual(today)) {
                            nextBirthday = nextBirthday.plusYears(1)
                        }

                        val daysLeft = ChronoUnit.DAYS.between(today, nextBirthday)
                        "Doğum gününe $daysLeft gün kaldı"
                    } catch (e: Exception) {
                        "Doğum günü bilgisi geçersiz"
                    }

                } else {
                    Toast.makeText(this@DashboardActivity, "Dashboard verisi alınamadı", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Hata: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
