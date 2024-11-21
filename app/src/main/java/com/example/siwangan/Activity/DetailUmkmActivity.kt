package com.example.siwangan.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.siwangan.Domain.Item
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityDetailUmkmBinding

class DetailUmkmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUmkmBinding
    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailUmkmBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getBundle()

    }

    private fun getBundle() {
        item = intent.getParcelableExtra("item")!!
        binding.apply {
            txtTitle.text = item.titleumkm
            txtDesc.text = item.descriptionumkm

            imgBack.setOnClickListener {
                finish()
            }

            Glide.with(this@DetailUmkmActivity)
                .load(item.picumkm)
                .into(imgUmkm)

            Glide.with(this@DetailUmkmActivity)
                .load(item.menu)
                .into(imageMenu)
        }
    }
}