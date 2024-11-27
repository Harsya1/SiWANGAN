package com.example.siwangan.Activity.Admin.Create

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.Activity.Admin.AdminActivity
import com.example.siwangan.Domain.itemBanner
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityBannerCreateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BannerCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBannerCreateBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBannerCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupWindowInsets()

        // Menginisialisasi referensi Firebase
        database = FirebaseDatabase.getInstance().getReference("Banner")

        // Menambahkan event listener untuk tombol
        binding.btnTambahDataBanner.setOnClickListener {
            saveData()
        }

        binding.btnBackCreateBanner.setOnClickListener {
            finish()
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun saveData() {
        val idbanner = binding.dataIdBanner.text.toString().trim()
        val urlbanner = binding.dataUrlBanner.text.toString().trim()

        // Validasi input
        if (idbanner.isEmpty()) {
            binding.dataIdBanner.error = "Masukkan ID Banner"
            return
        }
        if (urlbanner.isEmpty()) {
            binding.dataUrlBanner.error = "Masukkan URL Banner"
            return
        }

        // Membuat objek itemBanner
        val bannerAdd = itemBanner(
            idB = idbanner,
            url = urlbanner
        )

        // Menyimpan data ke Firebase
        database.child(idbanner).setValue(bannerAdd).addOnSuccessListener {
            // Membersihkan input
            binding.dataIdBanner.text.clear()
            binding.dataUrlBanner.text.clear()

            // Memberi notifikasi
            Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

            // Kembali ke AdminActivity
            val intent = Intent(this@BannerCreateActivity, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show()
        }
    }
}
