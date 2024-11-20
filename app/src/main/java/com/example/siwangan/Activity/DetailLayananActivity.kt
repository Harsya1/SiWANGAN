package com.example.siwangan.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.siwangan.Domain.Item
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityDetailLayananBinding
import java.util.ResourceBundle.getBundle

class DetailLayananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLayananBinding
    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailLayananBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getBundle()

    }

    private fun getBundle() {
        item = intent.getParcelableExtra("item")!!
        binding.apply {
            txtTitle.text = item.title
            txtDesc.text = item.description
            txtHarga.text = item.price

            imgBack.setOnClickListener {
                finish()
            }

            Glide.with(this@DetailLayananActivity)
                .load(item.pic)
                .into(imgLayanan)
        }
    }
}