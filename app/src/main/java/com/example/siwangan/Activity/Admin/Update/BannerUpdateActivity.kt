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
import com.example.siwangan.Activity.Admin.AdminBannerActivity
import com.example.siwangan.Activity.Admin.AdminUmkmActivity
import com.example.siwangan.Domain.itemBanner
import com.example.siwangan.Domain.itemUmkm
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityBannerUpdateBinding
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.ResourceBundle.getBundle

class BannerUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBannerUpdateBinding
    private var itemBanner: itemBanner? = null

    private var uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityBannerUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent dengan validasi
        getBundle()

        // Gunakan data jika tidak null
        itemBanner?.let {
            setupUI(it)
        } ?: run {
            // Data null, tampilkan pesan error atau kembali ke aktivitas sebelumnya
            finish() // Menghindari crash jika data null
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.imageUpdateBanner.setImageURI(it)
            if (it != null) {
                uri = it
            }
        }

        binding.pickImage.setOnClickListener {
            pickImage.launch("image/*")
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
        val id = binding.textViewId.text.toString().trim()  // ID tidak akan diubah

        // Membuat referensi database Firebase berdasarkan ID
        val database = FirebaseDatabase.getInstance().getReference("Banner").child(id)

        // Membuat Map untuk update data (update hanya field yang ada isinya)
        val updates = mutableMapOf<String, Any>()

        // Jika ada gambar baru yang dipilih, konversi ke Base64 dan update
        uri?.let {
            val base64Image = uriToBase64(this, it)
            if (base64Image != null) {
                updates["url"] = base64Image // Update gambar ke dalam url
            } else {
                Toast.makeText(this, "Gagal mengonversi gambar ke Base64", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Periksa apakah ada data yang akan diupdate
        if (updates.isNotEmpty()) {
            // Update hanya data yang diubah
            database.updateChildren(updates).addOnSuccessListener {
                Toast.makeText(this, "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
                // Kembali ke AdminActivity
                val intent = Intent(this@BannerUpdateActivity, AdminBannerActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Data Gagal Diperbarui", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Tidak ada data yang diubah", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getBundle() {
        itemBanner = intent.getParcelableExtra("itemBanner")
    }

    private fun setupUI(item: itemBanner) {
        binding.apply {
            textViewId.text = item.idB
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