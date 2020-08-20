package com.kitkat.buzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.kitkat.buzz.ui.auth.LoginActivity
import com.kitkat.buzz.ui.home.HomeActivity
import com.kitkat.buzz.utils.startHomeActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 5000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }

        }, SPLASH_TIME_OUT)
    }
}