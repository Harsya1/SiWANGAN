package com.example.siwangan.Activity.Admin.Create

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.Activity.Admin.AdminActivity
import com.example.siwangan.Activity.Admin.AdminLayananActivity
import com.example.siwangan.Domain.itemLayanan
import com.example.siwangan.Domain.itemUmkm
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityLayananCreateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class LayananCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayananCreateBinding
    private lateinit var database: DatabaseReference  // Harus DatabaseReference

    private var uriPicLayanan: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLayananCreateBinding.inflate(layoutInflater)
        database = FirebaseDatabase.getInstance().getReference("Layanan")
        setContentView(binding.root)

        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pickImageUmkm = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uriPicLayanan = it
                binding.imageViewLayanan.setImageURI(it)
            }
        }
        binding.buttonPilihFotoLayanan.setOnClickListener { //untuk ambil image umkm
            pickImageUmkm.launch("image/*")
        }

        binding.buttonTambahDataLayanan.setOnClickListener {
            saveData()
        }


        binding.btnlayanancreateback.setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }

    }

    private fun saveData() {
        val nama = binding.dataNamaLayanan.text.toString().trim()
        val descLayanan = binding.dataDeskripsiLayanan.text.toString().trim()
        val harga = binding.dataHargaLayanan.text.toString().trim()
        val ratingString = binding.dataRatingLayanan.text.toString().trim()

        // Validasi form
        if (nama.isEmpty()) {
            binding.dataNamaLayanan.error = "Masukkan Data"
            return
        }
        if (descLayanan.isEmpty()) {
            binding.dataDeskripsiLayanan.error = "Masukkan Data"
            return
        }
        if (harga.isEmpty()) {
            binding.dataHargaLayanan.error = "Masukkan Data"
            return
        }

        if (ratingString.isEmpty()) {
            binding.dataRatingLayanan.error = "Masukkan Data"
            return
        }

        val rating = ratingString.toDoubleOrNull()
        if (rating == null) {
            binding.dataRatingLayanan.error = "Rating harus berupa angka"
            return
        }

        val picLayananBase64 = uriPicLayanan?.let { uriToBase64(this, it) }

        // Pastikan gambar telah dipilih
        if (picLayananBase64 == null) {
            Toast.makeText(this, "Harap pilih gambar untuk Layanan", Toast.LENGTH_SHORT).show()
            return
        }

        // Melanjutkan hanya jika validasi berhasil
        database = FirebaseDatabase.getInstance().getReference("Layanan")
        val Layananadd = itemLayanan(
            title = nama,
            description = descLayanan,
            price = harga,
            score = rating,
            pic = picLayananBase64
        )

        database.child(nama).setValue(Layananadd).addOnSuccessListener {
            binding.dataNamaLayanan.text.clear()
            binding.dataDeskripsiLayanan.text.clear()
            binding.dataHargaLayanan.text.clear()
            binding.dataRatingLayanan.text.clear()

            Toast.makeText(this, "Data Telah Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@LayananCreateActivity, AdminLayananActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show()
        }
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