package com.example.siwangan.Activity.Admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siwangan.Activity.Admin.Create.LayananCreateActivity
import com.example.siwangan.Adapter.LayananAdminAdapter
import com.example.siwangan.Adapter.UMKMAdminAdapter
import com.example.siwangan.Domain.itemLayanan
import com.example.siwangan.Domain.itemUmkm
import com.example.siwangan.R
import com.example.siwangan.ViewModel.MainViewModel
import com.example.siwangan.databinding.ActivityAdminLayananBinding

class AdminLayananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminLayananBinding
    private val viewModelLayanan = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Inisialisasi binding
        binding = ActivityAdminLayananBinding.inflate(layoutInflater)
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

        binding.btnCreateLayanan.setOnClickListener{
            val intent = Intent(this, LayananCreateActivity::class.java)
            startActivity(intent)
        }
        initlayananadmin()
    }

    private fun initlayananadmin() {
        binding.apply {
            progressBarAdminLayanan.visibility = View.VISIBLE
            viewModelLayanan.load().observe(this@AdminLayananActivity) { items ->
                val mappedItems: List<itemLayanan> = items.map { item ->
                    itemLayanan(
                        title = item.title, // Sesuaikan dengan properti `item`
                        price = item.price, // Sesuaikan dengan properti `item`
                        description = item.description, // Sesuaikan dengan properti `item`
                        score = item.score, // Sesuaikan dengan properti `item`
                        pic = item.pic, // Sesuaikan dengan properti `item`
                    )
                }
                recyclerViewLayananAdmin.layoutManager = LinearLayoutManager(
                    this@AdminLayananActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                recyclerViewLayananAdmin.adapter = LayananAdminAdapter(mappedItems)

                progressBarAdminLayanan.visibility = View.GONE
            }
        }
    }
}