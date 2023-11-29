package com.example.determinemostimprovebasketballplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var bottomNav: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()

        bottomNav = findViewById(R.id.bottom_nav)

        bottomNav?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
                    true
                }
                R.id.statistic -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, StatisticFragment()).commit()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}