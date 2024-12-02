package com.example.siwangan.Activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.Domain.ItemHolder
import com.example.siwangan.databinding.ActivityDetailUmkmBinding
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayInputStream

class DetailUmkmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUmkmBinding
    private lateinit var item: ItemHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailUmkmBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getBundle()

        binding.btnMassageWhatsapp.setOnClickListener {
            sendWhatsAppMessage()
        }

    }

    private fun sendWhatsAppMessage() {
        item = intent.getParcelableExtra("item")!!

        val adminNumber = item.contact

        val message = """
        Halo Kak,
        Saya ingin melakukan pemesanan makanan
    """.trimIndent()

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$adminNumber&text=${Uri.encode(message)}"
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch (e: Exception) {
            Snackbar.make(findViewById(android.R.id.content), "Gagal membuka WhatsApp. Pastikan WhatsApp terpasang.", Snackbar.LENGTH_SHORT).show()
        }
    }



    private fun getBundle() {
        item = intent.getParcelableExtra("item")!!
        binding.apply {
            txtTitle.text = item.titleumkm
            txtDesc.text = item.descriptionumkm

            imgBack.setOnClickListener {
                finish()
            }

            val bitmapUmkm = base64ToBitmap(item.picumkm) // Assuming `item.pic` contains the Base64 string
            if (bitmapUmkm != null) {
                imgUmkm.setImageBitmap(bitmapUmkm)
            }

            val bitmapMenu = base64ToBitmap(item.menu) // Assuming `item.pic` contains the Base64 string
            if (bitmapMenu != null) {
                imageMenu.setImageBitmap(bitmapMenu)
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