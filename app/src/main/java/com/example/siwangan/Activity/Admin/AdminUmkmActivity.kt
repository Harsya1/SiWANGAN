package com.example.siwangan.Activity.Admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siwangan.Activity.Admin.Create.UmkmCreateActivity
import com.example.siwangan.Adapter.UMKMAdapter
import com.example.siwangan.Adapter.UMKMAdminAdapter
import com.example.siwangan.Domain.itemUmkm
import com.example.siwangan.R
import com.example.siwangan.ViewModel.UMKMViewModel
import com.example.siwangan.databinding.ActivityAdminUmkmBinding
import com.example.siwangan.databinding.ActivityUmkmBinding

class AdminUmkmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUmkmBinding
    private val viewModelUmkm = UMKMViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Inisialisasi binding
        binding = ActivityAdminUmkmBinding.inflate(layoutInflater)
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

        binding.btnCreateUmkm.setOnClickListener {
            val intent = Intent(this, UmkmCreateActivity::class.java)
            startActivity(intent)
        }

        initumkmadmin()
    }

    private fun initumkmadmin() {
        binding.apply {
            progressBarAdminUmkm.visibility = View.VISIBLE
            viewModelUmkm.load().observe(this@AdminUmkmActivity) { items ->
                val mappedItems: List<itemUmkm> = items.map { item ->
                    itemUmkm(
                        id = item.id, // Sesuaikan dengan properti `item`
                        titleumkm = item.titleumkm, // Sesuaikan dengan properti `item`
                        descriptionumkm = item.description, // Sesuaikan dengan properti `item`
                        picumkm = item.pic, // Sesuaikan dengan properti `item`
                        menu = item.menu, // Sesuaikan dengan properti `item`
                        contact = item.contact // Sesuaikan dengan properti `item`
                    )
                }
                recyclerAdminUmkm.layoutManager = LinearLayoutManager(
                    this@AdminUmkmActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                recyclerAdminUmkm.adapter = UMKMAdminAdapter(mappedItems)

                progressBarAdminUmkm.visibility = View.GONE
            }
        }
    }

}