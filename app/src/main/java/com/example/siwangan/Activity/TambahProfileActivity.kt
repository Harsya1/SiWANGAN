package com.example.siwangan.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.siwangan.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.ByteArrayOutputStream

class TambahProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var profileIcon: ImageView
    private lateinit var profileName: TextView
    private lateinit var editNamaLengkap: EditText
    private lateinit var editNomorTelepon: EditText
    private lateinit var editAlamat: EditText
    private lateinit var editJenisKelamin: EditText
    private lateinit var btnSimpanData: Button
    private lateinit var backButton: Button
    private val PICK_IMAGE_REQUEST = 71
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_profile)
        supportActionBar?.hide()

        // Inisialisasi
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        profileIcon = findViewById(R.id.profileIcon)
        profileName = findViewById(R.id.profileName)
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
            finish()
        }

        // Mengatur listener untuk back button
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
                if (imageUri != null) {
                    // Jika ada gambar yang dipilih, encode gambar dan simpan data
                    encodeImageAndSaveData(namaLengkap, nomorTelepon, alamat, jenisKelamin)
                } else {
                    // Jika tidak ada gambar, simpan data tanpa gambar
                    saveUserData(namaLengkap, nomorTelepon, alamat, jenisKelamin, null)
                }
            } else {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener untuk memilih gambar
        profileIcon.setOnClickListener {
            openImageChooser()
        }
    }

    // Fungsi untuk memilih gambar
    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Fungsi untuk menangani hasil memilih gambar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data

            // Menampilkan gambar pada ImageView
            Glide.with(this)
                .load(imageUri)
                .into(profileIcon)
        }
    }

    // Fungsi untuk encode gambar ke Base64 dan simpan data
    private fun encodeImageAndSaveData(namaLengkap: String, nomorTelepon: String, alamat: String, jenisKelamin: String) {
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        // Simpan data pengguna termasuk gambar yang di-encode ke Firebase Database
        saveUserData(namaLengkap, nomorTelepon, alamat, jenisKelamin, encodedImage)
    }

    // Fungsi untuk menyimpan data pengguna (termasuk foto profil jika ada)
    private fun saveUserData(
        namaLengkap: String,
        nomorTelepon: String,
        alamat: String,
        jenisKelamin: String,
        encodedImage: String?
    ) {
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            // Membuat objek untuk data baru
            val userMap = hashMapOf(
                "name" to namaLengkap,
                "phone" to nomorTelepon,
                "address" to alamat,
                "gender" to jenisKelamin
            )

            // Jika ada gambar yang di-encode, tambahkan ke data yang akan disimpan
            if (encodedImage != null) {
                userMap["profile_picture"] = encodedImage
            }

            // Menyimpan data di Firebase
            mDatabase.child("Users").child(currentUser.uid)
                .updateChildren(userMap as Map<String, Any>)
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

    // Fungsi untuk mengambil data user dari Firebase
    private fun fetchUserData(userId: String) {
        mDatabase.child("Users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Ambil data dari database
                        val namaLengkap = dataSnapshot.child("name").getValue(String::class.java)
                        val nomorTelepon = dataSnapshot.child("phone").getValue(String::class.java)
                        val alamat = dataSnapshot.child("address").getValue(String::class.java)
                        val jenisKelamin = dataSnapshot.child("gender").getValue(String::class.java)
                        val profilePicture = dataSnapshot.child("profile_picture").getValue(String::class.java)

                        // Update UI dengan data dari Firebase
                        editNamaLengkap.setText(namaLengkap)
                        editNomorTelepon.setText(nomorTelepon)
                        editAlamat.setText(alamat)
                        editJenisKelamin.setText(jenisKelamin)

                        // Menampilkan gambar profil jika ada
                        if (profilePicture != null) {
                            val imageBytes = Base64.decode(profilePicture, Base64.DEFAULT)
                            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            profileIcon.setImageBitmap(decodedImage)
                        }
                    } else {
                        Log.e("TambahProfileActivity", "Data snapshot does not exist!")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("TambahProfileActivity", "Database error: ${databaseError.message}")
                }
            })
    }
}