package com.bernacelik.akillioda

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
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
    private lateinit var tvEmergencyPhone: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var tvBirthdayCountdown: TextView
    private lateinit var tvSleepTime: TextView
    private lateinit var tvSleepRemaining: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard) // İLK OLARAK BURASI

        // Arka planı sabit yapıyoruz (cinsiyete göre değil)
        val rootLayout = findViewById<LinearLayout>(R.id.dashboardRootLayout)
        rootLayout.setBackgroundColor(Color.parseColor("#E0F2FF"))

        // View'ları bağla
        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvCO2 = findViewById(R.id.tvCO2)
        tvSleepTime = findViewById(R.id.tvSleepTime)
        tvSleepRemaining = findViewById(R.id.tvSleepRemaining)
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
            .baseUrl("http://192.168.1.8:5000/") // kendi IP’nle değiştir
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getDashboardData(userId).enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val dashboard = response.body()!!

                    tvWelcome.text = "Merhaba ${dashboard.childName} bebeğin ebeveyni ${dashboard.userName}!"

                    // Telefon numarasını boşluklu yaz
                    tvEmergencyPhone.text = formatPhoneNumber(dashboard.emergencyContact)

                    // Sensör verileri
                    dashboard.sensorData?.let {
                        tvTemperature.text = "${it.temperature ?: "--"} °C"
                        tvHumidity.text = "${it.humidity ?: "--"} %"
                        tvCO2.text = "${it.co2 ?: "--"} ppm"
                    }

                    // Uyku bilgisi
                    tvSleepTime.text = "${dashboard.childName}’ın yatma saati: ${dashboard.sleepSchedule}"
                    try {
                        val formatter = DateTimeFormatter.ofPattern("HH:mm")
                        val sleepTime = LocalTime.parse(dashboard.sleepSchedule, formatter)
                        val now = LocalTime.now()
                        val duration = Duration.between(now, sleepTime)
                        val adjustedDuration = if (duration.isNegative) duration.plusHours(24) else duration
                        val hoursLeft = adjustedDuration.toHours()
                        val minutesLeft = adjustedDuration.toMinutes() % 60
                        tvSleepRemaining.text = "${hoursLeft} saat ${minutesLeft} dk kaldı 😴"
                    } catch (e: Exception) {
                        tvSleepRemaining.text = "Uyku saati geçersiz"
                        Log.e("SLEEP_PARSE_ERROR", "Uyku saati işlenemedi: ${e.message}")
                    }


                    // Doğum günü geri sayım
                    tvBirthdayCountdown.text = try {
                        val today = LocalDate.now()
                        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                        val birthDate = LocalDate.parse(dashboard.childBirthDate, formatter)
                        var nextBirthday = birthDate.withYear(today.year)
                        if (!nextBirthday.isAfter(today)) {
                            nextBirthday = nextBirthday.plusYears(1)
                        }
                        val daysLeft = ChronoUnit.DAYS.between(today, nextBirthday)
                        "Doğum gününe $daysLeft gün kaldı 🎂"
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

    private fun formatPhoneNumber(raw: String): String {
        val cleaned = raw.replace("[^\\d]".toRegex(), "")
        return if (cleaned.length == 10) {
            // Türk numara formatı: 532 123 45 67
            "${cleaned.substring(0, 3)} ${cleaned.substring(3, 6)} ${cleaned.substring(6, 8)} ${cleaned.substring(8, 10)}"
        } else {
            raw // Uyuşmazsa olduğu gibi döndür
        }
    }
}
