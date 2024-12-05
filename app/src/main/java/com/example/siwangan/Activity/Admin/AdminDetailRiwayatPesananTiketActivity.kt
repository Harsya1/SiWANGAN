package com.example.siwangan.Activity.Admin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.databinding.ActivityAdminDetailRiwayatPesananBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.io.ByteArrayOutputStream

class AdminDetailRiwayatPesananTiketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDetailRiwayatPesananBinding
    private lateinit var firestore: FirebaseFirestore
    private var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailRiwayatPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        val bookingCode = intent.getStringExtra("bookingCode") ?: ""

        fetchBookingDetails(bookingCode)

        binding.buttonSetujuiPesanan.setOnClickListener {
            showConfirmationDialog()
        }

        binding.buttonTolakPesanan.setOnClickListener {
            showDeletionConfirmationDialog()
        }
    }

    private fun fetchBookingDetails(bookingCode: String) {
        firestore.collection("bookings")
            .whereEqualTo("bookingCode", bookingCode)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    documentId = document.id
                    val itemTitle = document.getString("itemTitle") ?: ""
                    val bookingDate = document.getString("selectedDate") ?: ""
                    val totalPrice = document.getDouble("totalPrice")?.toString() ?: ""
                    val status = document.getString("status") ?: ""
                    val proofImageBase64 = document.getString("proofImage") ?: ""
                    val userName = document.getString("userName") ?: ""
                    val userPhone = document.getString("userPhone") ?: ""
                    val quantity = document.getLong("quantity")?.toInt() ?: 0

                    // Set data to views
                    binding.txtBookingCode.text = bookingCode
                    binding.txtItemTitle.text = itemTitle
                    binding.txtBookingDate.text = bookingDate
                    binding.txtTotalPrice.text = totalPrice
                    binding.txtStatus.text = status
                    binding.txtUserName.text = userName
                    binding.txtUserPhone.text = userPhone
                    binding.txtQuantity.text = quantity.toString()

                    // Decode and set the image
                    if (proofImageBase64.isNotEmpty()) {
                        val decodedBytes = Base64.decode(proofImageBase64, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        binding.imgProof.setImageBitmap(bitmap)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("AdminDetailRiwayatPesananTiketActivity", "Error fetching booking data: ${exception.message}")
            }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Persetujuan")
            .setMessage("Apakah bukti pembayaran sudah sesuai dengan pesanan ini?")
            .setPositiveButton("Sudah") { dialog, _ ->
                updateStatusToApproved()
                generateAndStoreQRCode()
                dialog.dismiss()
            }
            .setNegativeButton("Kembali") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun updateStatusToApproved() {
        documentId?.let {
            firestore.collection("bookings")
                .document(it)
                .update("status", "Disetujui")
                .addOnSuccessListener {
                    binding.txtStatus.text = "Disetujui"
                    Toast.makeText(this, "Pesanan Telah Disetujui", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e("AdminDetailRiwayatPesananTiketActivity", "Error updating status: ${exception.message}")
                }
        } ?: run {
            Log.e("AdminDetailRiwayatPesananTiketActivity", "Document ID is null")
        }
    }

    private fun generateAndStoreQRCode() {
        val qrContent = """
            Booking Code: ${binding.txtBookingCode.text}
            Item Title: ${binding.txtItemTitle.text}
            Booking Date: ${binding.txtBookingDate.text}
            Total Price: ${binding.txtTotalPrice.text}
            Status: ${binding.txtStatus.text}
            User Name: ${binding.txtUserName.text}
            User Phone: ${binding.txtUserPhone.text}
            Quantity: ${binding.txtQuantity.text}
        """.trimIndent()

        val bitMatrix: BitMatrix = MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, 200, 200)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encodedQR = Base64.encodeToString(byteArray, Base64.DEFAULT)

        documentId?.let {
            firestore.collection("bookings")
                .document(it)
                .update(mapOf(
                    "TiketQR" to encodedQR,
                    "proofImage" to null
                ))
                .addOnSuccessListener {
                    Toast.makeText(this, "Barcode untuk pengguna telah dibuat", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e("AdminDetailRiwayatPesananTiketActivity", "Error storing QR code: ${exception.message}")
                }
        } ?: run {
            Log.e("AdminDetailRiwayatPesananTiketActivity", "Document ID is null")
        }
    }

    private fun showDeletionConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Penghapusan")
            .setMessage("Ingin menghapus Pesanan ini?")
            .setPositiveButton("Ya") { dialog, _ ->
                deleteBooking()
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteBooking() {
        documentId?.let {
            firestore.collection("bookings")
                .document(it)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Pesanan Telah Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("AdminDetailRiwayatPesananTiketActivity", "Error deleting booking: ${exception.message}")
                }
        } ?: run {
            Log.e("AdminDetailRiwayatPesananTiketActivity", "Document ID is null")
        }
    }
}