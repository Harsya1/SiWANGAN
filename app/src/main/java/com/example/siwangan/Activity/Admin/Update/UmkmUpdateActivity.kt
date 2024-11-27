package com.example.siwangan.Activity.Admin.Update

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.Activity.Admin.AdminUmkmActivity
import com.example.siwangan.Domain.itemUmkm
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityUmkmUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ResourceBundle.getBundle

class UmkmUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUmkmUpdateBinding
    private lateinit var database: DatabaseReference

    private var itemUmkm: itemUmkm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityUmkmUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent dengan validasi
        getBundle()

        // Gunakan data jika tidak null
        itemUmkm?.let {
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
        val nama = binding.updateData1.text.toString().trim()
        val descumkm = binding.updateData3.text.toString().trim()
        val contact = binding.updateData4.text.toString().trim()
        val foto = binding.updateData5.text.toString().trim()
        val menu = binding.updateData6.text.toString().trim()

        // Membuat referensi database Firebase berdasarkan ID
        val database = FirebaseDatabase.getInstance().getReference("Umkm").child(id)

        // Membuat Map untuk update data (update hanya field yang ada isinya)
        val updates = mutableMapOf<String, Any>()

        // Cek dan hanya update jika data ada
        if (nama.isNotEmpty()) {
            updates["titleumkm"] = nama
        }
        if (descumkm.isNotEmpty()) {
            updates["descriptionumkm"] = descumkm
        }
        if (contact.isNotEmpty()) {
            updates["contact"] = contact
        }
        if (foto.isNotEmpty()) {
            updates["picumkm"] = foto
        }
        if (menu.isNotEmpty()) {
            updates["menu"] = menu
        }

        // Periksa apakah ada data yang akan diupdate
        if (updates.isNotEmpty()) {
            // Update hanya data yang diubah
            database.updateChildren(updates).addOnSuccessListener {
                Toast.makeText(this, "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show()

                // Bersihkan input setelah update
                binding.updateData1.text.clear()
                binding.updateData3.text.clear()
                binding.updateData4.text.clear()
                binding.updateData5.text.clear()
                binding.updateData6.text.clear()

                // Kembali ke AdminActivity
                val intent = Intent(this@UmkmUpdateActivity, AdminUmkmActivity::class.java)
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
        itemUmkm = intent.getParcelableExtra("itemUmkm")
    }

    private fun setupUI(item: itemUmkm) {
        binding.apply {
            textViewId.text = item.id
            txtData1.text = item.titleumkm
            txtData3.text = item.descriptionumkm
            txtData4.text = item.contact
            txtData5.text = item.picumkm
            txtData6.text = item.menu
        }
    }
}