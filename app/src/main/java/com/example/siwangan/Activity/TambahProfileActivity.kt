package com.example.siwangan.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TambahProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var editNamaLengkap: EditText
    private lateinit var editNomorTelepon: EditText
    private lateinit var editAlamat: EditText
    private lateinit var editJenisKelamin: EditText
    private lateinit var btnSimpanData: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_profile)
        supportActionBar?.hide()

        // Mengambil userId yang dikirim dari EditProfileActivity
        val userId = intent.getStringExtra("USER_ID")

        if (userId != null) {
            println("User ID yang diterima: $userId")
        } else {
            Log.e("TambahProfileActivity", "User ID is null!")
        }

        // Inisialisasi Firebase Auth dan Database Reference
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        // Inisialisasi View
        editNamaLengkap = findViewById(R.id.editNamaLengkap)
        editNomorTelepon = findViewById(R.id.editNomorTelepon)
        editAlamat = findViewById(R.id.editAlamat)
        editJenisKelamin = findViewById(R.id.editJenisKelamin)
        btnSimpanData = findViewById(R.id.registerButton)
        backButton = findViewById(R.id.backButton)

        // Cek apakah user sedang login
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            // Menampilkan data user dari Firebase
            fetchUserData(currentUser.uid)
        } else {
            Log.e("TambahProfileActivity", "User not logged in!")
            // Redirect ke LoginActivity jika tidak ada user yang login
            finish()
        }

        backButton.setOnClickListener {
            finish()
        }
        // Tombol Simpan Data
        btnSimpanData.setOnClickListener {
            val namaLengkap = editNamaLengkap.text.toString()
            val nomorTelepon = editNomorTelepon.text.toString()
            val alamat = editAlamat.text.toString()
            val jenisKelamin = editJenisKelamin.text.toString()

            if (namaLengkap.isNotEmpty() && nomorTelepon.isNotEmpty() && alamat.isNotEmpty() && jenisKelamin.isNotEmpty()) {
                saveUserData(namaLengkap, nomorTelepon, alamat, jenisKelamin)
            } else {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    // Fungsi untuk mengambil data user dari Firebase
    private fun fetchUserData(userId: String) {
        mDatabase.child("Users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Ambil data dari database
                        val namaLengkap = dataSnapshot.child("name").getValue(String::class.java)
                        val nomorTelepon = dataSnapshot.child("phone").getValue(String::class.java)

                        // Update UI dengan data dari Firebase
                        editNamaLengkap.setText(namaLengkap)
                        editNomorTelepon.setText(nomorTelepon)

                    } else {
                        Log.e("TambahProfileActivity", "Data snapshot does not exist!")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("TambahProfileActivity", "Database error: ${databaseError.message}")
                }
            })
    }

    // Fungsi untuk menyimpan data yang telah diubah
    private fun saveUserData(namaLengkap: String, nomorTelepon: String, alamat: String, jenisKelamin: String) {
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            // Membuat objek untuk data baru
            val userMap = hashMapOf(
                "name" to namaLengkap,
                "phone" to nomorTelepon,
                "address" to alamat,
                "gender" to jenisKelamin
            )

            // Menyimpan data di Firebase
            mDatabase.child("Users").child(currentUser.uid).updateChildren(userMap as Map<String, Any>)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Kirim data yang telah diperbarui kembali ke EditProfileActivity
                        val resultIntent = Intent()
                        resultIntent.putExtra("NEW_ADDRESS", alamat)
                        resultIntent.putExtra("NEW_GENDER", jenisKelamin)

                        setResult(RESULT_OK, resultIntent)

                        Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                        finish()  // Menutup activity setelah data disimpan
                    } else {
                        Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Log.e("TambahProfileActivity", "User not logged in!")
        }
    }
}
