package com.example.siwangan.Activity.Admin.Update

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import java.util.ResourceBundle.getBundle

class BannerUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBannerUpdateBinding
    private var itemBanner: itemBanner? = null

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
        binding.button.setOnClickListener {
            finish()
        }
        binding.btnUpdateData.setOnClickListener {
            updateData()
        }

    }

    private fun updateData() {
        // Ambil ID dari TextView (ID sudah diset sebelumnya)
        val id = binding.textViewId.text.toString().trim()  // ID tidak akan diubah

        // Ambil data lainnya dari EditText
        val picBaru = binding.updateData1.text.toString().trim()


        // Membuat referensi database Firebase berdasarkan ID
        val database = FirebaseDatabase.getInstance().getReference("Banner").child(id)

        // Membuat Map untuk update data (update hanya field yang ada isinya)
        val updates = mutableMapOf<String, Any>()

        // Cek dan hanya update jika data ada
        if (picBaru.isNotEmpty()) {
            updates["url"] = picBaru
        }

        // Periksa apakah ada data yang akan diupdate
        if (updates.isNotEmpty()) {
            // Update hanya data yang diubah
            database.updateChildren(updates).addOnSuccessListener {
                Toast.makeText(this, "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show()

                // Bersihkan input setelah update

                binding.updateData1.text.clear()

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
            txtData1.text = item.url
        }
    }
}