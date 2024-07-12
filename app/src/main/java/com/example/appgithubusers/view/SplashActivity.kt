package com.example.appgithubusers.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appgithubusers.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setContentView(R.layout.activity_splash)

        val splashScreenDuration = 3000 // 3 seconds
        val mainActivityIntent = Intent(this, MainActivity::class.java)

        val splashScreenTimer = object : Thread() {
            override fun run() {
                try {
                    sleep(splashScreenDuration.toLong())
                    startActivity(mainActivityIntent)
                    finish()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        splashScreenTimer.start()
    }

}
