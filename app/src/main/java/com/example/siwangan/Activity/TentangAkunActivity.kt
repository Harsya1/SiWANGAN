package com.example.siwangan.Activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.R

class TentangAkunActivity : AppCompatActivity() {

    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tentang_aplikasi)  // Pastikan layout ini sesuai dengan nama XML yang Anda gunakan

        backButton = findViewById(R.id.backButton4) // Menemukan tombol berdasarkan ID-nya

        backButton.setOnClickListener {
            // Saat tombol diklik, aktivitas ini akan ditutup dan kembali ke aktivitas sebelumnya
            finish()
        }
    }
}
