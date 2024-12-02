package com.example.siwangan.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.Activity.BookingTicket.BookingActivity
import com.example.siwangan.Domain.ItemHolder
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityDetailLayananBinding
import java.io.ByteArrayInputStream

class DetailLayananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLayananBinding
    private lateinit var item: ItemHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailLayananBinding.inflate(layoutInflater)

        setContentView(binding.root)
        getBundle()

        binding.btnWhatsapp.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

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

            // Konversi Base64 ke Bitmap dan set ke ImageView
            val bitmap = base64ToBitmap(item.pic) // Assuming `item.pic` contains the Base64 string
            if (bitmap != null) {
                imgLayanan.setImageBitmap(bitmap)
            } else {
                imgLayanan.setImageResource(R.drawable.error_image) // Placeholder image if decoding fails
            }
        }
    }
    private fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedBytes)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}