package com.example.siwangan.Activity.Admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityAdminBannerBinding

class AdminScanQRActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Inisialisasi binding
        binding = ActivityAdminBannerBinding.inflate(layoutInflater)
        setContentView(binding.root) // Gunakan binding.root sebagai tampilan utama
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}