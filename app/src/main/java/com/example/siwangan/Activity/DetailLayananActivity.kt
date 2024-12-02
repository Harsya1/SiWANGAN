package com.example.siwangan.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.Domain.ItemHolder
import com.example.siwangan.Helper.ImageCache
import com.example.siwangan.Activity.BookingTicket.BookingActivity
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityDetailLayananBinding
import java.io.ByteArrayInputStream

class DetailLayananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLayananBinding
    private lateinit var item: ItemHolder // Deklarasi item hanya satu kali

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailLayananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil item dari Intent secara langsung
        val titledata = intent.getStringExtra("title")
        val descriptiondata = intent.getStringExtra("description")
        val pricedata = intent.getStringExtra("price")

        // Ambil data dari item untuk ditampilkan di UI
        binding.apply {
            txtTitle.text = titledata
            txtDesc.text = descriptiondata
            txtHarga.text = pricedata
        }

        loadImageFromCache() // Muat gambar dari cache

        binding.btnWhatsapp.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        binding.imgBack.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        ImageCache.base64Image = null  // Bersihkan cache setelah Activity dihancurkan
    }

    private fun base64ToBitmap(base64Str: String?): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedBytes)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun loadImageFromCache() {
        val base64Image = ImageCache.base64Image // Ambil gambar dari cache

        // Konversi Base64 ke Bitmap
        val bitmap = base64ToBitmap(base64Image ?: "")
        if (bitmap != null) {
            binding.imgLayanan.setImageBitmap(bitmap)
        } else {
            binding.imgLayanan.setImageResource(R.drawable.error_image) // Placeholder jika gagal
        }

        // Bersihkan cache setelah mengambil gambar
        ImageCache.base64Image = null
    }
}
