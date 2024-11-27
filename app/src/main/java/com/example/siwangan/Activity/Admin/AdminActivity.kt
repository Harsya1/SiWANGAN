package com.example.siwangan.Activity.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()

        // Inisialisasi View Binding
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Padding untuk SystemBars
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Intent untuk adminBannerActivity
        binding.adminBannerActivity.setOnClickListener {
            val intent = Intent(this, AdminBannerActivity::class.java)
            startActivity(intent)
        }

        // Intent untuk adminLayananActivity
        binding.adminLayananActivity.setOnClickListener {
            val intent = Intent(this, AdminLayananActivity::class.java)
            startActivity(intent)
        }

        // Intent untuk adminUmkmActivity
        binding.adminUmkmActivity.setOnClickListener {
            val intent = Intent(this, AdminUmkmActivity::class.java)
            startActivity(intent)
        }

        // Log-Out button
        binding.btnAdminLogout.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
