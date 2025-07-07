package com.bernacelik.akillioda

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

class RegisterActivity : AppCompatActivity() {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var childName: EditText
    private lateinit var childGender: Spinner
    private lateinit var childAge: EditText
    private lateinit var childBirthDate: EditText
    private lateinit var emergencyContact: EditText
    private lateinit var sleepSchedule: EditText
    private lateinit var registerButton: Button
    private lateinit var loginText: TextView
    private lateinit var passwordEditText: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // UI elementlerini bağla
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        usernameEditText = findViewById(R.id.etUsername)
        childName = findViewById(R.id.childName)
        childGender = findViewById(R.id.childGender)
        childAge = findViewById(R.id.childAge)
        childBirthDate = findViewById(R.id.childBirthDate)
        emergencyContact = findViewById(R.id.emergencyContact)
        sleepSchedule = findViewById(R.id.sleepSchedule)
        registerButton = findViewById(R.id.registerButton)
        loginText = findViewById(R.id.loginText)
        passwordEditText = findViewById(R.id.passwordEditText)

        // Spinner için cinsiyet seçenekleri
        val genderOptions = arrayOf("Erkek", "Kız")
        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item_white,  // beyaz yazı için özel layout
            genderOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        childGender.adapter = adapter


        // Doğum tarihi seçici (DatePicker)
        childBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                childBirthDate.setText("$d/${m + 1}/$y")
            }, year, month, day)
            datePicker.show()
        }

        // Uyku saati seçici (TimePicker)
        sleepSchedule.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                sleepSchedule.setText(formattedTime)
            }, hour, minute, true)
            timePicker.show()
        }

        // Kayıt butonuna tıklayınca
        registerButton.setOnClickListener {
            Log.d("RegisterActivity", "Register butonuna tıklandı")

            val fields = listOf(
                firstName, lastName, usernameEditText,
                childName, childAge, childBirthDate,
                emergencyContact, sleepSchedule
            )

            if (fields.any { it.text.isNullOrBlank() }) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "Boş alan var, kayıt yapılmadı")
                return@setOnClickListener
            }

            val jsonBody = JSONObject().apply {
                Log.d("DEBUG_REGISTER", "Doğum Tarihi Backend'e: ${childBirthDate.text}")
                put("kullaniciAdi", usernameEditText.text.toString())
                put("sifre", passwordEditText.text.toString())
                put("ad", firstName.text.toString())
                put("soyad", lastName.text.toString())
                put("cocukAdi", childName.text.toString())
                put("cocukCinsiyeti", childGender.selectedItem.toString())
                put("cocukYasi", childAge.text.toString().toIntOrNull() ?: 0)
                put("cocukDogumTarihi", childBirthDate.text.toString())
                put("acilDurumKisisi", emergencyContact.text.toString())
                put("uykuZamani", sleepSchedule.text.toString())
            }

            thread {
                try {
                    Log.d("RegisterActivity", "Sunucuya istek hazırlanıyor")
                    val url = URL("http://192.168.1.8:5000/api/user/register")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true

                    val writer = OutputStreamWriter(connection.outputStream)
                    writer.write(jsonBody.toString())
                    writer.flush()
                    writer.close()

                    val responseCode = connection.responseCode
                    val responseStream = if (responseCode in 200..299) connection.inputStream else connection.errorStream
                    val responseText = responseStream.bufferedReader().use { it.readText() }

                    Log.d("RegisterActivity", "Sunucudan cevap geldi: $responseText, Kod: $responseCode")

                    runOnUiThread {
                        Toast.makeText(this, "Sunucu cevabı: $responseText", Toast.LENGTH_LONG).show()
                        if (responseCode in 200..299) {
                            Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Kayıt başarısız! Kod: $responseCode", Toast.LENGTH_SHORT).show()
                        }
                    }

                    connection.disconnect()
                } catch (e: Exception) {
                    Log.e("RegisterActivity", "Sunucuya bağlanılamadı: ${e.message}")
                    runOnUiThread {
                        Toast.makeText(this, "Sunucuya bağlanılamadı: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        // Login sayfasına geçiş
        loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
