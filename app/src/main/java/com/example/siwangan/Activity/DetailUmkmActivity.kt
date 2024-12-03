package com.example.siwangan.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.siwangan.Domain.ItemHolder
import com.example.siwangan.Helper.ImageCache
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityDetailUmkmBinding
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream

class DetailUmkmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUmkmBinding
    private lateinit var item: ItemHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailUmkmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("titleumkm")
        val description = intent.getStringExtra("descriptionumkm")

        binding.apply {
            txtTitle.text = title
            txtDesc.text = description
        }

        loadImageFromCache()

        binding.btnMassageWhatsapp.setOnClickListener {
            sendWhatsAppMessage()
        }
        binding.imgBack.setOnClickListener {
            finish()
        }

    }

    private fun sendWhatsAppMessage() {
        val nomer = intent.getStringExtra("contact")

        try {
            val adminNumber = "62" + nomer?.trim()
            if (adminNumber.length <= 2) { // Pastikan nomor valid
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Nomor kontak tidak valid.",
                    Snackbar.LENGTH_SHORT
                ).show()
                return
            }

            // Pesan yang akan dikirim
            val message = """
            Halo Kak,
            Saya ingin melakukan pemesanan makanan.
        """.trimIndent()

            // Buat URL WhatsApp
            val url = "https://api.whatsapp.com/send?phone=$adminNumber&text=${Uri.encode(message)}"

            // Intent untuk membuka WhatsApp
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            // Log error untuk membantu debug
            e.printStackTrace()
            Snackbar.make(
                findViewById(android.R.id.content),
                "Gagal membuka WhatsApp. Pastikan WhatsApp terpasang.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }



    private fun loadImageFromCache() {
        val base64Umkm = ImageCache.base64Image // Retrieve image from cache
        val base64Menu = ImageCache.base64Image

        // Convert Base64 to Bitmap
        val bitmap = base64ToBitmap(base64Umkm ?: "")
        if (bitmap != null) {
            binding.imgUmkm.setImageBitmap(bitmap)
        } else {
            binding.imgUmkm.setImageResource(R.drawable.error_image) // Placeholder if decoding fails
        }

        val bitmapmenu = base64ToBitmap(base64Menu ?: "")
        if (bitmapmenu != null) {
            binding.imageMenu.setImageBitmap(bitmapmenu)
        } else {
            binding.imageMenu.setImageResource(R.drawable.error_image) // Placeholder if decoding fails
        }
    }

    private fun saveImageToCacheAndGetUri(context: Context, base64Str: String): Uri? {
        val bitmap = base64ToBitmap(base64Str) ?: return null
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "image.png")
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
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