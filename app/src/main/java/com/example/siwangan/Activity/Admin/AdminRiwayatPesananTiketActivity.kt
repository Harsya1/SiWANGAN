package com.example.siwangan.Activity.Admin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siwangan.Activity.DataClass.BookingItem
import com.example.siwangan.Activity.Decoration.SpacingItemDecoration
import com.example.siwangan.Adapter.AdminBookingAdapter
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityAdminRiwayatPesananTiketBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminRiwayatPesananTiketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminRiwayatPesananTiketBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var bookingAdapter: AdminBookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRiwayatPesananTiketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firestore = FirebaseFirestore.getInstance()
        initBookingHistory()
    }

    private fun initBookingHistory() {
        val recyclerView = binding.recyclerViewAdminBookingHistory
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpacingItemDecoration(16)) // Add spacing of 16dp between items

        firestore.collection("bookings")
            .get()
            .addOnSuccessListener { documents ->
                val bookingList = mutableListOf<BookingItem>()
                for (document in documents) {
                    val bookingCode = document.getString("bookingCode") ?: ""
                    val itemTitle = document.getString("itemTitle") ?: ""
                    val bookingDate = document.getString("selectedDate") ?: ""
                    val totalPrice = document.getDouble("totalPrice")?.toString() ?: ""
                    val status = document.getString("status") ?: ""
                    val proofImageBase64 = document.getString("proofImage") ?: ""
                    val proofImageBitmap = decodeBase64ToImage(proofImageBase64)
                    val userName = document.getString("userName") ?: ""
                    val userPhone = document.getString("userPhone") ?: ""
                    val quantity = document.getLong("quantity")?.toInt() ?: 0

                    bookingList.add(BookingItem(bookingCode, itemTitle, bookingDate, totalPrice, status, proofImageBitmap, userName, userPhone, quantity, null, ""))
                }
                bookingAdapter = AdminBookingAdapter(bookingList)
                recyclerView.adapter = bookingAdapter
            }
            .addOnFailureListener { exception ->
                Log.e("AdminRiwayatPesananTiketActivity", "Error fetching booking data: ${exception.message}")
            }
    }

    private fun decodeBase64ToImage(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            Log.e("AdminRiwayatPesananTiketActivity", "Error decoding base64 image: ${e.message}")
            null
        }
    }
}