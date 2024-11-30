package com.example.siwangan.Activity.Admin

import BannerAdminAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siwangan.Activity.Admin.Create.BannerCreateActivity
//import com.example.siwangan.Adapter.BannerAdminAdapter
import com.example.siwangan.Domain.itemBanner
import com.example.siwangan.R
import com.example.siwangan.ViewModel.BannerViewModel
import com.example.siwangan.databinding.ActivityAdminBannerBinding

class AdminBannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBannerBinding
    private val viewModelLayanan = BannerViewModel()

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
        binding.btnBack.setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }

        binding.btnCreateBanner.setOnClickListener{
            val intent = Intent(this, BannerCreateActivity::class.java)
            startActivity(intent)
        }

        innitbanneradmin()
    }

    private fun innitbanneradmin() {
        binding.apply {
            progressBar3.visibility = View.VISIBLE
            viewModelLayanan.load().observe(this@AdminBannerActivity) { items ->
                // Tambahkan log untuk memeriksa data
                items.forEachIndexed { index, item ->
                    android.util.Log.d("DEBUG_BANNER", "Item $index: ${item.idB}, URL: ${item.url}")
                }

                val itemBanner: List<itemBanner> = items.map { item ->
                    itemBanner(
                        idB = item.idB,
                        url = item.url
                    )
                }

                recyclerView2.layoutManager = LinearLayoutManager(
                    this@AdminBannerActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                recyclerView2.adapter = BannerAdminAdapter(itemBanner.toMutableList())
                progressBar3.visibility = View.GONE
            }
        }
    }

}