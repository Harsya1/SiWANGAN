package com.example.siwangan.Activity.Admin.Update

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

class LayananUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayananUpdateBinding
    private var itemLayanan: itemLayanan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLayananUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent dengan validasi
        getBundle()

        // Gunakan data jika tidak null
        itemLayanan?.let {
            setupUI(it)
        } ?: run {
            // Data null, tampilkan pesan error atau kembali ke aktivitas sebelumnya
            finish() // Menghindari crash jika data null
        }
        binding.button.setOnClickListener {
            finish()
        }

        binding.btnUpdateData.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        // Ambil ID dari TextView (ID sudah diset sebelumnya)
        val nama = binding.textViewId.text.toString().trim()  // ID tidak akan diubah

        // Ambil data lainnya dari EditText
        val desc = binding.updateData1.text.toString().trim()
        val harga = binding.updateData2.text.toString().trim()
        val ratingString = binding.updateData3.text.toString().trim()
        val urlfoto = binding.updateData4.text.toString().trim()

        // Membuat referensi database Firebase berdasarkan ID
        val database = FirebaseDatabase.getInstance().getReference("Layanan").child(nama)

        // Membuat Map untuk update data (update hanya field yang ada isinya)
        val updates = mutableMapOf<String, Any>()

        // Cek dan hanya update jika data ada

        if (desc.isNotEmpty()) {
            updates["description"] = desc
        }
        if (harga.isNotEmpty()) {
            updates["price"] = harga
        }
        // Validasi rating jika ada
        if (ratingString.isNotEmpty()) {
            val rating = try {
                ratingString.toDouble()  // mencoba mengonversi string ke Double
            } catch (e: NumberFormatException) {
                // Jika gagal konversi, tampilkan pesan error
                Toast.makeText(this, "Rating tidak valid", Toast.LENGTH_SHORT).show()
                return  // Jika rating tidak valid, batalkan proses update
            }
            updates["score"] = rating
        }
        if (urlfoto.isNotEmpty()) {
            updates["pic"] = urlfoto
        }


        // Periksa apakah ada data yang akan diupdate
        if (updates.isNotEmpty()) {
            // Update hanya data yang diubah
            database.updateChildren(updates).addOnSuccessListener {
                Toast.makeText(this, "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show()

                // Bersihkan input setelah update
                binding.updateData1.text.clear()
                binding.updateData2.text.clear()
                binding.updateData3.text.clear()
                binding.updateData4.text.clear()

                // Kembali ke AdminActivity
                val intent = Intent(this@LayananUpdateActivity, AdminLayananActivity::class.java)
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
        itemLayanan = intent.getParcelableExtra("itemLayanan")
    }

    private fun setupUI(item: itemLayanan) {
        binding.apply {
            textViewId.text = item.title
            txtData1.text = item.description
            txtData2.text = item.price
            txtData3.text = item.score.toString()
            txtData4.text = item.pic
        }
    }
}