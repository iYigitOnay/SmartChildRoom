package com.bernacelik.akillioda

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logo = findViewById<ImageView>(R.id.maskotImage)
        val title = findViewById<TextView>(R.id.makuseTextView)

        val goToLogin = Intent(this, LoginActivity::class.java)

        logo.setOnClickListener {
            startActivity(goToLogin)
        }

        title.setOnClickListener {
            startActivity(goToLogin)
        }
    }
}
