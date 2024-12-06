package com.example.siwangan.Activity.Admin.Update

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
import com.example.siwangan.Activity.Admin.AdminLayananActivity
import com.example.siwangan.Activity.Admin.AdminUmkmActivity
import com.example.siwangan.Domain.itemBanner
import com.example.siwangan.Domain.itemLayanan
import com.example.siwangan.Domain.itemUmkm
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityLayananUpdateBinding
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class LayananUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayananUpdateBinding
    private var itemLayanan: itemLayanan? = null

    private var uriPicLayanan: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLayananUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent dengan validasi
//        getBundle()

        val titledata = intent.getStringExtra("title")
        val descriptiondata = intent.getStringExtra("description")
        val pricedata = intent.getStringExtra("price")
        val score = intent.getDoubleExtra("score",0.0)

        binding.apply {
            textViewId.text = titledata
            txtData1.text = descriptiondata
            txtData2.text = pricedata
            txtData3.text = score.toString()
        }

        // Gunakan data jika tidak null
//        itemLayanan?.let {
//            setupUI(it)
//        } ?: run {
//            // Data null, tampilkan pesan error atau kembali ke aktivitas sebelumnya
//            finish() // Menghindari crash jika data null
//        }

        val pickImageUmkm = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uriPicLayanan = it
                binding.imageViewUpdateLayanan.setImageURI(it)
            }
        }
        binding.pilihFotoLayanan.setOnClickListener { //untuk ambil image umkm
            pickImageUmkm.launch("image/*")
        }

        binding.button.setOnClickListener {
            finish()
        }

        binding.btnUpdateData.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        // Ambil ID dari TextView (ID tidak akan diubah)
        val id = binding.textViewId.text.toString().trim()

        // Ambil data lainnya dari EditText
        val desc = binding.updateData1.text.toString().trim()
        val harga = binding.updateData2.text.toString().trim()
        val ratingString = binding.updateData3.text.toString().trim()

        // Validasi rating (jika ada)
        val rating = if (ratingString.isNotEmpty()) {
            try {
                ratingString.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Rating tidak valid", Toast.LENGTH_SHORT).show()
                return
            }
        } else null

        // Membuat Map untuk update data
        val updates = mutableMapOf<String, Any>()
        if (desc.isNotEmpty()) updates["description"] = desc
        if (harga.isNotEmpty()) updates["price"] = harga
        rating?.let { updates["score"] = it }

        uriPicLayanan?.let {
            val base64Image = uriToBase64(this, it)
            if (!base64Image.isNullOrEmpty()) updates["pic"] = base64Image
        }

        // Jika tidak ada data yang diubah
        if (updates.isEmpty()) {
            Toast.makeText(this, "Tidak ada data yang diubah", Toast.LENGTH_SHORT).show()
            return
        }

        // Update data di Firebase
        val database = FirebaseDatabase.getInstance().getReference("Layanan").child(id)
        database.updateChildren(updates).addOnSuccessListener {
            Toast.makeText(this, "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
            clearInputs()
            startActivity(Intent(this@LayananUpdateActivity, AdminLayananActivity::class.java))
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Data Gagal Diperbarui", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputs() {
        binding.apply {
            updateData1.text.clear()
            updateData2.text.clear()
            updateData3.text.clear()
        }
    }

//    private fun getBundle() {
//        itemLayanan = intent.getParcelableExtra("itemLayanan")
//    }

//    private fun setupUI(item: itemLayanan) {
//
//    }
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