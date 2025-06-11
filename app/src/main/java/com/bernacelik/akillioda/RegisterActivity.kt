package com.bernacelik.akillioda

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class RegisterActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val firstName = findViewById<EditText>(R.id.firstName)
        val lastName = findViewById<EditText>(R.id.lastName)
        val childName = findViewById<EditText>(R.id.childName)
        val childGender = findViewById<Spinner>(R.id.childGender)
        val childAge = findViewById<EditText>(R.id.childAge)
        val childBirthDate = findViewById<EditText>(R.id.childBirthDate)
        val emergencyContact = findViewById<EditText>(R.id.emergencyContact)
        val sleepSchedule = findViewById<EditText>(R.id.sleepSchedule)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginText = findViewById<TextView>(R.id.loginText)


        val genders = arrayOf("Erkek", "Kız")
        val adapter = ArrayAdapter(this, R.layout.spinner_item_white, genders)
        adapter.setDropDownViewResource(R.layout.spinner_item_white)
        childGender.adapter = adapter


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

        registerButton.setOnClickListener {
            val fields = listOf(
                firstName, lastName, childName,
                childAge, childBirthDate, emergencyContact, sleepSchedule
            )

            if (fields.any { it.text.isNullOrBlank() }) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
