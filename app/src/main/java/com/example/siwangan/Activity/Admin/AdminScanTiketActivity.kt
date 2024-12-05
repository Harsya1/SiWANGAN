package com.example.siwangan.Activity.Admin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.databinding.ActivityAdminScanTiketBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class AdminScanTiketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminScanTiketBinding
    private lateinit var firestore: FirebaseFirestore

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

        firestore = FirebaseFirestore.getInstance()

        binding.buttonScanQR.setOnClickListener {
            scanQRCode()
        }
        binding.buttonTandaiSelesai.setOnClickListener {
            if (isScanResultAvailable()) {
                showCompletionConfirmationDialog()
            } else {
                Toast.makeText(this, "Scan Tiket Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            }
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
            Toast.makeText(this, "izin Kamera Diperlukan Untuk Keperluan Scan Tiket", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startQRCodeScanner() {
        val options = ScanOptions()
        options.setPrompt("Scan Tiket Pengguna")
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
        binding.tvIDTicket.text = scanResult
    }

    private fun isScanResultAvailable(): Boolean {
        return binding.tvIDTicket.text.isNotEmpty()
    }

    private fun showCompletionConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Penyelesaian")
            .setMessage("Apakah Anda yakin ingin menandai pesanan ini sebagai selesai?")
            .setPositiveButton("Ya") { dialog, _ ->
                val bookingCode = binding.IDBooking.text.toString()
                updateBookingStatusToSelesai(bookingCode)
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun updateBookingStatusToSelesai(bookingCode: String) {
        firestore.collection("bookings")
            .whereEqualTo("bookingCode", bookingCode)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val documentId = document.id
                        firestore.collection("bookings")
                            .document(documentId)
                            .update("status", "Selesai")
                            .addOnSuccessListener {
                                deleteTicketQR(documentId)
                                Toast.makeText(this, "Pesanan Berhasil Ditandai Selesai", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "Error updating status: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Isi Kode Booking Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching booking data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteTicketQR(documentId: String) {
        firestore.collection("bookings")
            .document(documentId)
            .update("TiketQR", "")
            .addOnSuccessListener {
                Toast.makeText(this, "Barcode Berhasil Digunakan ", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error deleting QR code: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}