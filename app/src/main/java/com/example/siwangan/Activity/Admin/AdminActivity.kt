package com.example.siwangan.Activity.Admin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.Activity.SplashLoginRegisterActivity
import com.example.siwangan.databinding.ActivityAdminBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // Inisialisasi View Binding
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Padding untuk SystemBars
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve admin-specific data from SharedPreferences
        val sharedPref = getSharedPreferences("admin_prefs", Context.MODE_PRIVATE)
        val adminName = sharedPref.getString("admin_name", "Admin")
        Toast.makeText(this, "Welcome, $adminName", Toast.LENGTH_SHORT).show()

        // Intent untuk adminBannerActivity
        binding.adminBannerActivity.setOnClickListener {
            val intent = Intent(this, AdminBannerActivity::class.java)
            startActivity(intent)
        }

        // Intent untuk adminLayananActivity
        binding.adminLayananActivity.setOnClickListener {
            val intent = Intent(this, AdminLayananActivity::class.java)
            startActivity(intent)
        }

        // Intent untuk adminUmkmActivity
        binding.adminUmkmActivity.setOnClickListener {
            val intent = Intent(this, AdminUmkmActivity::class.java)
            startActivity(intent)
        }
//        // Intent untuk adminScanQrActivity
        binding.adminScanQrActivity.setOnClickListener {
            val intent = Intent(this, AdminScanTiketActivity::class.java)
            startActivity(intent)
        }
        // Intent untuk adminRiwayatPesananTiketActivity
        binding.adminRiwayatPesananTiket.setOnClickListener {
            val intent = Intent(this, AdminRiwayatPesananTiketActivity::class.java)
            startActivity(intent)
        }

        // Log-Out button
        binding.btnAdminLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Keluar")
                .setMessage("Apakah anda yakin untuk keluar?")
                .setPositiveButton("Ya") { dialog, _ ->
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                    mGoogleSignInClient.signOut()
                    mAuth.signOut()
                    val intent = Intent(this, SplashLoginRegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                    dialog.dismiss()
                }
                .setNegativeButton("Tidak", null)
                .create()
                .show()
        }
    }
}