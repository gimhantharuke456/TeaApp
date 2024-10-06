package com.example.teaapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.teaapp.service.LocalPrefsService

class SplashActivity : AppCompatActivity() {
    private lateinit var localPrefsService: LocalPrefsService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        localPrefsService = LocalPrefsService(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigate to OnboardActivityOne after 3 seconds (3000 ms)
        Handler(Looper.getMainLooper()).postDelayed({
            if(localPrefsService.getString("USER_TOKEN")!=null){
                val intent = Intent(this, ViewContainerActivity::class.java)
                startActivity(intent)
                finish() // Optional: Closes SplashActivity so user can't return to it
            }else{
                val intent = Intent(this, OnBoardActivityOne::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
    }
}
