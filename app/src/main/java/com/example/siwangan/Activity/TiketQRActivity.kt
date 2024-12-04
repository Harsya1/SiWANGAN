package com.example.siwangan.Activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.databinding.ActivityQrTiketBinding

class TiketQRActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrTiketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrTiketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tiketQRBase64 = intent.getStringExtra("TiketQR")
        tiketQRBase64?.let {
            val bitmap = decodeBase64ToImage(it)
            binding.ivQrCode.setImageBitmap(bitmap)
        }
        binding.imageView3.setOnClickListener {
            finish()
        }
    }

    private fun decodeBase64ToImage(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            Log.e("TiketQRActivity", "Error decoding Base64 string: ${e.message}")
            null
        }
    }
}