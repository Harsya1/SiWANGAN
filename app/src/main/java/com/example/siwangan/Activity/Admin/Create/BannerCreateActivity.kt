package com.example.siwangan.Activity.Admin.Create

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.Activity.Admin.AdminActivity
import com.example.siwangan.Activity.Admin.AdminBannerActivity
import com.example.siwangan.Domain.itemBanner
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityBannerCreateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class BannerCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBannerCreateBinding
    private lateinit var database: DatabaseReference

    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBannerCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupWindowInsets()

        // Menginisialisasi referensi Firebase
        database = FirebaseDatabase.getInstance().getReference("Banner")

        // Menambahkan event listener untuk tombol
        binding.btnTambahDataBanner.setOnClickListener {
            val idBanner = binding.dataIdBanner.text.toString().trim()
            if (idBanner.isEmpty()) {
                Toast.makeText(this, "Harap isi ID Banner", Toast.LENGTH_SHORT).show()
            } else {
                saveData(idBanner)
            }
        }

        binding.btnBackCreateBanner.setOnClickListener {
            finish()
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.imageViewBanner.setImageURI(it)
            if (it != null) {
                uri = it
            }
        }

        binding.btnPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun saveData(idBanner: String) {
        uri?.let {
            val base64Image = uriToBase64(this, it)
            if (base64Image != null) {
                val bannerAdd = itemBanner(idB = idBanner,
                    url = base64Image
                )

                // Menyimpan data ke Firebase
                database.child(idBanner).setValue(bannerAdd)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AdminBannerActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { error ->
                        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Gagal mengonversi gambar ke Base64", Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(this, "Harap pilih gambar", Toast.LENGTH_SHORT).show()
    }

    private fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
