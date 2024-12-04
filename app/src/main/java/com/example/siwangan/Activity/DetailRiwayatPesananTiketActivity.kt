package com.example.siwangan.Activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.databinding.ActivityDetailRiwayatPesananTiketBinding
import com.google.firebase.firestore.FirebaseFirestore

class DetailRiwayatPesananTiketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRiwayatPesananTiketBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRiwayatPesananTiketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        val bookingCode = intent.getStringExtra("bookingCode") ?: ""

        fetchBookingDetails(bookingCode)
    }

    private fun fetchBookingDetails(bookingCode: String) {
        firestore.collection("bookings")
            .whereEqualTo("bookingCode", bookingCode)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val itemTitle = document.getString("itemTitle") ?: ""
                    val bookingDate = document.getString("selectedDate") ?: ""
                    val totalPrice = document.getDouble("totalPrice")?.toString() ?: ""
                    val status = document.getString("status") ?: ""
                    val proofImageBase64 = document.getString("proofImage") ?: ""
                    val quantity = document.getLong("quantity")?.toInt() ?: 0

                    // Set data to views
                    binding.txtBookingCode.text = bookingCode
                    binding.txtItemTitle.text = itemTitle
                    binding.txtBookingDate.text = bookingDate
                    binding.txtTotalPrice.text = totalPrice
                    binding.txtStatus.text = status
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
                Log.e("DetailRiwayatPesananTiketActivity", "Error fetching booking data: ${exception.message}")
            }
    }
}