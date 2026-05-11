package com.example.application_with_api

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.application_with_api.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.hide()

        // After 2 seconds, navigate to ComplaintFormActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, ComplaintFormActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
