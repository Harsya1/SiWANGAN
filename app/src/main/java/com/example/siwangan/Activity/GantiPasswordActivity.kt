package com.example.siwangan.Activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GantiPasswordActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var pwSaatIni: EditText
    private lateinit var pwBaru: EditText
    private lateinit var ulangiPwBaru: EditText
    private lateinit var btnGantiPw: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganti_password)

        // Inisialisasi Firebase Auth dan Database Reference
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        // Inisialisasi Views
        pwSaatIni = findViewById(R.id.pwSaatIni)
        pwBaru = findViewById(R.id.pwBaru)
        ulangiPwBaru = findViewById(R.id.ulangiPwBaru)
        btnGantiPw = findViewById(R.id.btnGantiPw)
        backButton = findViewById(R.id.backButton2)

        // Tombol Ganti Password
        btnGantiPw.setOnClickListener {
            val passwordSaatIni = pwSaatIni.text.toString()
            val passwordBaru = pwBaru.text.toString()
            val ulangiPasswordBaru = ulangiPwBaru.text.toString()

            if (validateInput(passwordSaatIni, passwordBaru, ulangiPasswordBaru)) {
                changePassword(passwordSaatIni, passwordBaru)
            }
        }

        // Tombol Kembali
        backButton.setOnClickListener {
            finish() // Menutup activity dan kembali ke activity sebelumnya
        }
    }

    // Fungsi untuk memvalidasi input
    private fun validateInput(passwordSaatIni: String, passwordBaru: String, ulangiPasswordBaru: String): Boolean {
        if (TextUtils.isEmpty(passwordSaatIni)) {
            Toast.makeText(this, "Password saat ini harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(passwordBaru)) {
            Toast.makeText(this, "Password baru harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (passwordBaru.length > 12) {
            Toast.makeText(this, "Password baru tidak boleh lebih dari 12 karakter", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!passwordBaru.matches(Regex(".*[A-Z].*"))) {
            Toast.makeText(this, "Password baru harus mengandung huruf besar", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!passwordBaru.matches(Regex(".*[a-z].*"))) {
            Toast.makeText(this, "Password baru harus mengandung huruf kecil", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!passwordBaru.matches(Regex(".*\\d.*"))) {
            Toast.makeText(this, "Password baru harus mengandung angka", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!passwordBaru.matches(Regex(".*[!@#\$%^&*(),.?\":{}|<>].*"))) {
            Toast.makeText(this, "Password baru harus mengandung simbol", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(ulangiPasswordBaru)) {
            Toast.makeText(this, "Ulangi password baru harus diisi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (passwordBaru != ulangiPasswordBaru) {
            Toast.makeText(this, "Password baru dan konfirmasi password baru tidak cocok", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    // Fungsi untuk mengganti password di Firebase Authentication dan Database
    private fun changePassword(currentPassword: String, newPassword: String) {
        val user = mAuth.currentUser
        if (user != null) {
            // Proses untuk mengganti password
            user.reauthenticate(EmailAuthProvider.getCredential(user.email!!, currentPassword))
                .addOnCompleteListener { reAuthTask ->
                    if (reAuthTask.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    // Menyimpan password baru ke Firebase Database
                                    updatePasswordInDatabase(newPassword)
                                } else {
                                    Toast.makeText(this, "Gagal mengubah password", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Password saat ini salah", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    // Fungsi untuk memperbarui password di Firebase Database
    private fun updatePasswordInDatabase(newPassword: String) {
        val userId = mAuth.currentUser?.uid
        if (userId != null) {
            val userMap = hashMapOf<String, Any>(
                "password" to newPassword
            )

            mDatabase.child("Users").child(userId).updateChildren(userMap)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                        finish() // Menutup activity dan kembali ke halaman sebelumnya
                    } else {
                        Toast.makeText(this, "Gagal memperbarui password di database", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
