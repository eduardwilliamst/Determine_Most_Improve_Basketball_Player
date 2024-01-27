package com.example.determinemostimprovebasketballplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MipActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mip)

        val btnAHP: Button = findViewById(R.id.btnAHP)
        val btnPM: Button = findViewById(R.id.btnPM)

        btnAHP.setOnClickListener {
            // Handling klik tombol AHP
            val intent = Intent(this, AhpActivity::class.java)
            startActivity(intent)
        }

        btnPM.setOnClickListener {
            // Handling klik tombol PM
            val intent = Intent(this, ProfileMatchingActivity::class.java)
            startActivity(intent)
        }
    }
}
