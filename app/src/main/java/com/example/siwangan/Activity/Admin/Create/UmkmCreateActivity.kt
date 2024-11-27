package com.example.siwangan.Activity.Admin.Create

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.Activity.Admin.AdminActivity
import com.example.siwangan.Activity.Admin.AdminUmkmActivity
import com.example.siwangan.Domain.itemUmkm
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityUmkmCreateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UmkmCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUmkmCreateBinding

    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUmkmCreateBinding.inflate(layoutInflater)

        database = FirebaseDatabase.getInstance().getReference("Umkm")

        setContentView(binding.root)
        supportActionBar?.hide()

        // Mengatur padding untuk tampilan
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Menambahkan listener untuk tombol simpan data UMKM
        binding.btnTambahDataUMKM.setOnClickListener {
            saveData()
        }


        binding.btnBack.setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }

    }

    private fun saveData() {
        val nama = binding.dataNamaUmkm.text.toString().trim()
        val id = binding.dataId.text.toString().trim()
        val descumkm = binding.dataDeskripsiUmkm.text.toString().trim()
        val contact = binding.dataContact.text.toString().trim()
        val foto = binding.dataUrlFotoUmkm.text.toString().trim()
        val menu = binding.dataUrlMenu.text.toString().trim()

        // Validasi form
        if (nama.isEmpty()) {
            binding.dataNamaUmkm.error = "Masukkan Data"
            return
        }
        if (id.isEmpty()) {
            binding.dataId.error = "Masukkan Data"
            return
        }
        if (descumkm.isEmpty()) {
            binding.dataDeskripsiUmkm.error = "Masukkan Data"
            return
        }
        if (contact.isEmpty()) {
            binding.dataContact.error = "Masukkan Data"
            return
        }
        if (foto.isEmpty()) {
            binding.dataUrlFotoUmkm.error = "Masukkan Data"
            return
        }
        if (menu.isEmpty()) {
            binding.dataUrlMenu.error = "Masukkan Data"
            return
        }

        // Melanjutkan hanya jika validasi berhasil
        database = FirebaseDatabase.getInstance().getReference("Umkm")
        val UMKMadd = itemUmkm(
            titleumkm = nama,
            descriptionumkm = descumkm,
            picumkm = foto,
            menu = menu,
            id = id,
            contact = contact
        )

        database.child(id).setValue(UMKMadd).addOnSuccessListener {
            binding.dataNamaUmkm.text.clear()
            binding.dataId.text.clear()
            binding.dataDeskripsiUmkm.text.clear()
            binding.dataContact.text.clear()
            binding.dataUrlFotoUmkm.text.clear()
            binding.dataUrlMenu.text.clear()

            Toast.makeText(this, "Data Telah Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@UmkmCreateActivity, AdminUmkmActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Data gagal ditambahkan", Toast.LENGTH_SHORT).show()
        }
    }

}
