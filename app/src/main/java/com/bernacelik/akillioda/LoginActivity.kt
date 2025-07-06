package com.bernacelik.akillioda

import android.content.Intent
import java.net.URL
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.HttpURLConnection
import java.io.OutputStreamWriter

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerText = findViewById<TextView>(R.id.registerText)

        loginButton.setOnClickListener {
            Log.d("TEST_CLICK", "Login butonuna tıklandı!")
            val usernameStr = username.text.toString()
            val passwordStr = password.text.toString()

            Log.d("TEST_VALUES", "Kullanıcı adı: $usernameStr, Şifre: $passwordStr")

            if (usernameStr.isBlank() || passwordStr.isBlank()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Thread {
                try {
                    val url = URL("http://192.168.1.8:5000/api/user/login")
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doOutput = true

                    val jsonInput = JSONObject()
                    jsonInput.put("kullaniciAdi", usernameStr)
                    jsonInput.put("sifre", passwordStr)

                    val outputStreamWriter = OutputStreamWriter(connection.outputStream)
                    outputStreamWriter.write(jsonInput.toString())
                    outputStreamWriter.flush()

                    val responseCode = connection.responseCode

                    val responseText = try {
                        val stream = if (responseCode in 200..299)
                            connection.inputStream
                        else
                            connection.errorStream

                        stream?.bufferedReader()?.use { it.readText() } ?: "Boş yanıt geldi"
                    } catch (ex: Exception) {
                        Log.e("NETWORK", "Yanıt okunurken hata: ${ex.message}")
                        "HATA: ${ex.message}"
                    }

                    runOnUiThread {
                        if (responseCode in 200..299) {
                            val jsonResponse = JSONObject(responseText)
                            val userId = jsonResponse.getString("userId")
                            Log.d("DEBUG_USERID", "Gelen userId: $userId")

                            val intent = Intent(this, DashboardActivity::class.java)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.e("NETWORK", "Giriş başarısız: $responseText")
                            Toast.makeText(this, "Giriş başarısız: $responseText", Toast.LENGTH_LONG).show()
                        }
                    }

                    connection.disconnect()

                } catch (e: Exception) {
                    Log.e("NETWORK", "Sunucuya bağlanılamadı: ${e.message}")
                    runOnUiThread {
                        Toast.makeText(this, "Sunucuya bağlanılamadı: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }.start()

        }

        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
