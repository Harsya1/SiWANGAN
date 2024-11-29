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
import com.example.siwangan.Activity.Admin.AdminUmkmActivity
import com.example.siwangan.Domain.itemUmkm
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityUmkmCreateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class UmkmCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUmkmCreateBinding
    private lateinit var database: DatabaseReference

    private var uriPicUmkm: Uri? = null
    private var uriMenu: Uri? = null

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

        val pickImageUmkm = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uriPicUmkm = it
                binding.imageView5.setImageURI(it)
            }
        }

        val pickImageMenu = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uriMenu = it
                binding.imageView6.setImageURI(it)
            }
        }
        binding.pickImageUmkm.setOnClickListener { //untuk ambil image umkm
            pickImageUmkm.launch("image/*")
        }

        binding.pickImageMenu.setOnClickListener { // untuk ambil image menu
            pickImageMenu.launch("image/*")
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

        // Konversi gambar ke Base64
        val picUmkmBase64 = uriPicUmkm?.let { uriToBase64(this, it) }
        val menuBase64 = uriMenu?.let { uriToBase64(this, it) }

        // Pastikan gambar telah dipilih
        if (picUmkmBase64 == null || menuBase64 == null) {
            Toast.makeText(this, "Harap pilih gambar untuk UMKM dan Menu", Toast.LENGTH_SHORT).show()
            return
        }
        // Pastikan gambar telah dipilih
        if (menuBase64 == null || menuBase64 == null) {
            Toast.makeText(this, "Harap pilih gambar untuk UMKM dan Menu", Toast.LENGTH_SHORT).show()
            return
        }

        // Melanjutkan hanya jika validasi berhasil
        val UMKMadd = itemUmkm(
            titleumkm = nama,
            descriptionumkm = descumkm,
            picumkm = picUmkmBase64,
            menu = menuBase64,
            id = id,
            contact = contact
        )

        database.child(id).setValue(UMKMadd).addOnSuccessListener {
            binding.dataNamaUmkm.text.clear()
            binding.dataId.text.clear()
            binding.dataDeskripsiUmkm.text.clear()
            binding.dataContact.text.clear()

            Toast.makeText(this, "Data Telah Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@UmkmCreateActivity, AdminUmkmActivity::class.java)
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
