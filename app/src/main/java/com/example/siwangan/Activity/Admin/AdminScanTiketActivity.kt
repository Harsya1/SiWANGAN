package com.example.siwangan.Activity.Admin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.databinding.ActivityAdminScanTiketBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class AdminScanTiketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminScanTiketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminScanTiketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonScanQR.setOnClickListener {
            scanQRCode()
        }
    }

    private fun scanQRCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startQRCodeScanner()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startQRCodeScanner()
        } else {
            Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startQRCodeScanner() {
        val options = ScanOptions()
        options.setPrompt("Scan a QR code")
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(true) // Lock orientation to portrait
        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            Toast.makeText(this, "Scan Tiket Berhasil", Toast.LENGTH_LONG).show()
            displayScanResult(result.contents)
        } else {
            Toast.makeText(this, "Scan Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayScanResult(scanResult: String) {
        binding.tvIDTicket.text = "$scanResult"

        // Update other TextViews as needed
    }
}