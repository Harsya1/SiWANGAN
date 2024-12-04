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
import com.example.siwangan.Helper.ImageCache
import com.example.siwangan.Activity.Booking.BookingLayananActivity
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityDetailLayananBinding
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream

class DetailLayananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLayananBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailLayananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve item data from Intent directly
        val titledata = intent.getStringExtra("title")
        val descriptiondata = intent.getStringExtra("description")
        val pricedata = intent.getStringExtra("price")

        // Display item data in UI
        binding.apply {
            txtTitle.text = titledata
            txtDesc.text = descriptiondata
            txtHarga.text = pricedata
        }

        loadImageFromCache() // Load image from cache

        binding.btnPesan.setOnClickListener {
            val imageUri = saveImageToCacheAndGetUri(this, ImageCache.base64Image)
            val intent = Intent(this, BookingLayananActivity::class.java)
            intent.putExtra("title", binding.txtTitle.text.toString())
            intent.putExtra("imageUri", imageUri.toString())
            startActivity(intent)
            ImageCache.base64Image = null // Clear cache after passing the image
        }

        binding.imgBack.setOnClickListener {
            finish()
        }
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
        val base64Image = ImageCache.base64Image // Retrieve image from cache

        // Convert Base64 to Bitmap
        val bitmap = base64ToBitmap(base64Image ?: "")
        if (bitmap != null) {
            binding.imgLayanan.setImageBitmap(bitmap)
        } else {
            binding.imgLayanan.setImageResource(R.drawable.error_image) // Placeholder if decoding fails
        }
    }

    private fun saveImageToCacheAndGetUri(context: Context, base64Str: String?): Uri? {
        val bitmap = base64ToBitmap(base64Str) ?: return null
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "image.png")
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }
}