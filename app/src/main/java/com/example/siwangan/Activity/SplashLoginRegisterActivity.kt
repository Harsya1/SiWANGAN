package com.example.siwangan.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.MainActivity
import com.example.siwangan.R
import com.google.firebase.auth.FirebaseAuth

class SplashLoginRegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_login_register)

        mAuth = FirebaseAuth.getInstance()

        // Check if user is logged in
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            // User is logged in, redirect to main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // User is not logged in, stay on this activity
            findViewById<Button>(R.id.btn_loginPage).setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            findViewById<Button>(R.id.btn_registerPage).setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}