package com.example.siwangan.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class EditProfileActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private var valueNamaLengkap: TextView? = null
    private var valueNomorTelepon: TextView? = null
    private var valueAlamat: TextView? = null
    private var valueJenisKelamin: TextView? = null
    private var btnEditProfile: Button? = null
    private var btnHapusAkun: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        valueNamaLengkap = findViewById<TextView>(R.id.valueNamaLengkap)
        valueNomorTelepon = findViewById<TextView>(R.id.valueNomorTelepon)
        valueAlamat = findViewById<TextView>(R.id.valueAlamat)
        valueJenisKelamin = findViewById<TextView>(R.id.valueJenisKelamin)

        btnEditProfile = findViewById<Button>(R.id.btnEditProfile)
        btnHapusAkun = findViewById<Button>(R.id.btnHapusAkun)

        // Menampilkan data user dari Firebase
        val userId = mAuth!!.currentUser!!.uid
        mDatabase!!.child("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val namaLengkap = dataSnapshot.child("namaLengkap").getValue(
                            String::class.java
                        )
                        val nomorTelepon = dataSnapshot.child("nomorTelepon").getValue(
                            String::class.java
                        )
                        val alamat = dataSnapshot.child("alamat").getValue(
                            String::class.java
                        )
                        val jenisKelamin = dataSnapshot.child("jenisKelamin").getValue(
                            String::class.java
                        )

                        valueNamaLengkap?.setText(namaLengkap)
                        valueNomorTelepon?.setText(nomorTelepon)
                        valueAlamat?.setText(alamat)
                        valueJenisKelamin?.setText(jenisKelamin)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors
                }
            })

        // Mengarahkan ke TambahProfileActivity saat tombol EditProfile dipencet
        btnEditProfile?.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                this@EditProfileActivity,
                TambahProfileActivity::class.java
            )
            startActivity(intent)
        })

        // Fungsi keluar akun saat tombol hapus akun dipencet
        btnHapusAkun?.setOnClickListener(View.OnClickListener {
            mAuth!!.signOut() // Keluar dari akun Firebase
            val intent = Intent(
                this@EditProfileActivity,
                LoginActivity::class.java
            )
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Bersihkan stack activity
            startActivity(intent)
            finish()
        })
    }
}
