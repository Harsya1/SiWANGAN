package com.example.siwangan.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var valueNamaLengkap: TextView
    private lateinit var valueNomorTelepon: TextView
    private lateinit var valueAlamat: TextView
    private lateinit var valueJenisKelamin: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnHapusAkun: Button
    private lateinit var backButton: Button

    private lateinit var profileImageView: ImageView

    private val REQUEST_CODE_UPDATE_PROFILE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Inisialisasi Firebase Auth dan Database Reference
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        // Inisialisasi View
        valueNamaLengkap = findViewById(R.id.valueNamaLengkap)
        valueNomorTelepon = findViewById(R.id.valueNomorTelepon)
        valueAlamat = findViewById(R.id.valueAlamat)
        valueJenisKelamin = findViewById(R.id.valueJenisKelamin)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnHapusAkun = findViewById(R.id.btnHapusAkun)
        backButton = findViewById(R.id.backButton3)

        profileImageView = findViewById(R.id.profileIcon3)


        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            // Menampilkan data user dari Firebase
            fetchUserData(currentUser.uid)

            // Mengarahkan ke TambahProfileActivity saat tombol EditProfile dipencet
            btnEditProfile.setOnClickListener {
                val intent = Intent(this, TambahProfileActivity::class.java)
                intent.putExtra("USER_ID", currentUser.uid)
                startActivityForResult(intent, REQUEST_CODE_UPDATE_PROFILE)
            }

            backButton.setOnClickListener {
                finish()
            }

            // Fungsi keluar akun saat tombol hapus akun dipencet
            btnHapusAkun.setOnClickListener {
                logoutUser()
            }

        } else {
            Log.e("EditProfileActivity", "User not logged in!")
            // Redirect ke LoginActivity jika tidak ada user yang login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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
                        val alamat = dataSnapshot.child("address").getValue(String::class.java)
                        val jenisKelamin = dataSnapshot.child("gender").getValue(String::class.java)
                        val profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String::class.java)


                        // Update UI untuk nama lengkap dan nomor telepon
                        valueNamaLengkap.text = namaLengkap ?: "N/A"
                        valueNomorTelepon.text = nomorTelepon ?: "N/A"
                        valueAlamat.text = alamat ?: "Tambah Alamat"
                        valueJenisKelamin.text = jenisKelamin ?: "Tambah Jenis Kelamin"
                    } else {
                        Log.e("EditProfileActivity", "Data snapshot does not exist!")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("EditProfileActivity", "Database error: ${databaseError.message}")
                }
            })
    }

    // Fungsi untuk logout
    private fun logoutUser() {
        mAuth.signOut() // Keluar dari akun Firebase
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    // Menangani hasil dari TambahProfileActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_UPDATE_PROFILE && resultCode == RESULT_OK) {
            // Ambil data yang dikirim dari TambahProfileActivity
            val newAddress = data?.getStringExtra("NEW_ADDRESS")
            val newGender = data?.getStringExtra("NEW_GENDER")

            // Update tampilan di EditProfileActivity
            valueAlamat.text = newAddress ?: "Alamat Tidak Ditemukan"
            valueJenisKelamin.text = newGender ?: "Jenis Kelamin Tidak Ditemukan"
        }
    }
}
