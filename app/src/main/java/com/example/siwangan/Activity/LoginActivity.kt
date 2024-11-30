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
import com.example.siwangan.HomeActivity
import com.example.siwangan.MainActivity
import com.example.siwangan.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var phoneInput2: EditText
    private lateinit var pwInput: EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100 // Request code untuk Google Sign-In
    private val adminEmail = "siwangan324@gmail.com"
    private val adminPassword = "airpanaskepulungan1101"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        // Cek apakah pengguna sudah login
        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Pengguna sudah login, arahkan ke HomeActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        // Hubungkan komponen XML ke kode
        phoneInput2 = findViewById(R.id.phoneInput2)
        pwInput = findViewById(R.id.pwInput)
        val btnLogin = findViewById<Button>(R.id.loginButton)
        val signInWithGoogleButton = findViewById<ImageButton>(R.id.googleLoginButton)
        val backButton = findViewById<ImageButton>(R.id.backButton2)

        // Tombol Kembali
        backButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SplashLoginRegisterActivity::class.java))
            finish()
        }

        // Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Tombol Login Manual
        btnLogin.setOnClickListener { v: View? ->
            val phone = phoneInput2.text.toString().trim { it <= ' ' }
            val password = pwInput.text.toString().trim { it <= ' ' }

            // Validasi input
            if (TextUtils.isEmpty(phone)) {
                phoneInput2.error = "Masukkan nomor telepon!"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                pwInput.error = "Masukkan password!"
                return@setOnClickListener
            }

            // Login dengan Firebase
            val email = "$phone@example.com" // Gunakan pola email dari nomor telepon
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        // Jika login berhasil
                        Toast.makeText(
                            this@LoginActivity,
                            "Login berhasil!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Simpan status login ke SharedPreferences
                        val editor = sharedPref.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()

                        // Arahkan ke halaman utama
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        // Jika login gagal
                        Toast.makeText(
                            this@LoginActivity,
                            "Login gagal: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        // Tombol Login dengan Google
        signInWithGoogleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
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
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    // Login berhasil dengan Google
                    Toast.makeText(this, "Login dengan Google berhasil!", Toast.LENGTH_SHORT).show()

                    // Simpan status login ke SharedPreferences
                    val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    // Arahkan ke halaman utama
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    // Jika login gagal dengan Google
                    Toast.makeText(this, "Login dengan Google gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Fungsi logout jika diperlukan
    private fun logout() {
        // Mengubah status login menjadi false di SharedPreferences
        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Logout dari Firebase
        mAuth.signOut()

        // Arahkan ke halaman login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
