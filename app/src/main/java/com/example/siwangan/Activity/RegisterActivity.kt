package com.example.siwangan.Activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private var nameInput: EditText? = null
    private var phoneInput: EditText? = null
    private var passwordInput: EditText? = null
    private var confirmPasswordInput: EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100 // Request code untuk Google Sign-In

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        // Inisialisasi Firebase
        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        // Hubungkan komponen XML ke kode
        nameInput = findViewById(R.id.nameInput) // Nama
        phoneInput = findViewById(R.id.phoneInput) // Nomor telepon
        passwordInput = findViewById(R.id.passwordInput) // Password
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput) // Konfirmasi password
        val btnRegister = findViewById<Button>(R.id.registerButton)
        val signInWithGoogleButton: ImageButton = findViewById(R.id.googleSignupButton)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Tombol Kembali
        backButton.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, SplashLoginRegisterActivity::class.java))
            finish()
        }

        // Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Dapatkan dari Firebase Console
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Tombol Daftar dengan Google
        signInWithGoogleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // Tombol Daftar Manual
        btnRegister.setOnClickListener { v: View? ->
            val name = nameInput?.text.toString().trim { it <= ' ' }
            val phone = phoneInput?.text.toString().trim { it <= ' ' }
            val password = passwordInput?.text.toString().trim { it <= ' ' }
            val confirmPassword = confirmPasswordInput?.text.toString().trim { it <= ' ' }

            // Validasi input
            if (TextUtils.isEmpty(name)) {
                nameInput?.error = "Masukkan nama!"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(phone)) {
                phoneInput?.error = "Masukkan nomor telepon!"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                passwordInput?.error = "Masukkan password!"
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                confirmPasswordInput?.error = "Password tidak cocok!"
                return@setOnClickListener
            }

            // Registrasi pengguna
            val email = "$phone@example.com" // Pola email dari nomor telepon
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Simpan data pengguna ke Realtime Database
                        val user: FirebaseUser = mAuth!!.currentUser!!
                        val userId: String = user.uid
                        val newUser = User(name, phone, email)
                        databaseReference!!.child(userId).setValue(newUser)
                            .addOnCompleteListener { task1 ->
                                if (task1.isSuccessful) {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Registrasi berhasil!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Gagal menyimpan data pengguna: ${task1.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registrasi gagal: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In gagal: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth!!.currentUser
                    user?.let {
                        val name = it.displayName ?: "Pengguna Google"
                        val email = it.email ?: "Tidak ada email"
                        val phone = "Google User"

                        // Simpan data pengguna ke Realtime Database
                        val userId = it.uid
                        val newUser = User(name, phone, email)
                        databaseReference!!.child(userId).setValue(newUser)
                            .addOnCompleteListener { task1 ->
                                if (task1.isSuccessful) {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Daftar dengan Google berhasil!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Gagal menyimpan data Google: ${task1.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Autentikasi Google gagal: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
    // Override tombol back
    override fun onBackPressed() {
        super.onBackPressed()
        // Arahkan kembali ke SplashLoginRegisterActivity
        val intent = Intent(this, SplashLoginRegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Kelas User untuk menyimpan data pengguna
    data class User(var name: String, var phone: String, var email: String)
}
