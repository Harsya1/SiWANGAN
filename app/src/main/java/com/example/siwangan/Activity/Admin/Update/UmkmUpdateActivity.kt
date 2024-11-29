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
import com.example.siwangan.Activity.Admin.AdminUmkmActivity
import com.example.siwangan.Domain.itemUmkm
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityUmkmUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.ResourceBundle.getBundle

class UmkmUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUmkmUpdateBinding
    private lateinit var database: DatabaseReference

    private var itemUmkm: itemUmkm? = null

    private var uriPicUmkm: Uri? = null
    private var uriMenu: Uri? = null

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
        val pickImageUmkm = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uriPicUmkm = it
                binding.imageViewUpdateUmkm.setImageURI(it)
            }
        }

        val pickImageMenu = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uriMenu = it
                binding.imageViewUpdateMenu.setImageURI(it)
            }
        }
        binding.btnPilihFotoUmkm.setOnClickListener { //untuk ambil image umkm
            pickImageUmkm.launch("image/*")
        }

        binding.pilihFotoMenu.setOnClickListener { // untuk ambil image menu
            pickImageMenu.launch("image/*")
        }
        binding.button.setOnClickListener {
            finish()
        }

        binding.btnUpdateData.setOnClickListener {
            updateData()
        }

    }

    private fun updateData() {
        val id = binding.textViewId.text.toString().trim()

        // Ambil data lainnya dari EditText
        val nama = binding.updateData1.text.toString().trim()
        val descumkm = binding.updateData3.text.toString().trim()
        val contact = binding.updateData4.text.toString().trim()

        // Membuat referensi database Firebase berdasarkan ID
        val database = FirebaseDatabase.getInstance().getReference("Umkm").child(id)

        // Membuat Map untuk update data
        val updates = mutableMapOf<String, Any>()

        // Update data teks jika tidak kosong
        if (nama.isNotEmpty()) {
            updates["titleumkm"] = nama
        }
        if (descumkm.isNotEmpty()) {
            updates["descriptionumkm"] = descumkm
        }
        if (contact.isNotEmpty()) {
            updates["contact"] = contact
        }

        // Ubah URI menjadi Base64 jika gambar diubah
        uriPicUmkm?.let {
            val base64ImageUmkm = uriToBase64(this, it)
            if (!base64ImageUmkm.isNullOrEmpty()) {
                updates["picumkm"] = base64ImageUmkm
            }
        }

        uriMenu?.let {
            val base64ImageMenu = uriToBase64(this, it)
            if (!base64ImageMenu.isNullOrEmpty()) {
                updates["menu"] = base64ImageMenu
            }
        }

        // Periksa apakah ada data yang akan diperbarui
        if (updates.isNotEmpty()) {
            database.updateChildren(updates).addOnSuccessListener {
                Toast.makeText(this, "Data Berhasil Diperbarui", Toast.LENGTH_SHORT).show()

                // Bersihkan input setelah update
                binding.updateData1.text.clear()
                binding.updateData3.text.clear()
                binding.updateData4.text.clear()

                // Kembali ke AdminUmkmActivity
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

    // Fungsi untuk mengambil data dari Intent
    private fun getBundle() {
        itemUmkm = intent.getParcelableExtra("itemUmkm")
    }

    private fun setupUI(item: itemUmkm) {
        binding.apply {
            textViewId.text = item.id
            txtData1.text = item.titleumkm
            txtData3.text = item.descriptionumkm
            txtData4.text = item.contact
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