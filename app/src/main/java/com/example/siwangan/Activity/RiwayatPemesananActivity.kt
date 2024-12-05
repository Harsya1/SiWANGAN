package com.example.siwangan.Activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.DataClass.BookingItem
import com.example.siwangan.Activity.Decoration.SpacingItemDecoration
import com.example.siwangan.Adapter.BookingAdapter
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityRiwayatPemesananBinding
import com.google.firebase.firestore.FirebaseFirestore

class RiwayatPemesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatPemesananBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var bookingAdapter: BookingAdapter
    private lateinit var bookingList: MutableList<BookingItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatPemesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firestore = FirebaseFirestore.getInstance()
        val profileName = intent.getStringExtra("profileName") ?: ""
        initBookingHistory(profileName)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initBookingHistory(userName: String) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewBookingHistory)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpacingItemDecoration(16)) // Add spacing of 16dp between items

        firestore.collection("bookings")
            .whereEqualTo("userName", userName)
            .whereIn("status", listOf("pending", "Selesai"))
            .get()
            .addOnSuccessListener { documents ->
                bookingList = mutableListOf()
                for (document in documents) {
                    val bookingCode = document.getString("bookingCode") ?: ""
                    val itemTitle = document.getString("itemTitle") ?: ""
                    val bookingDate = document.getString("selectedDate") ?: ""
                    val totalPrice = document.getDouble("totalPrice")?.toString() ?: ""
                    val status = document.getString("status") ?: ""
                    val proofImageBase64 = document.getString("proofImage") ?: ""
                    val proofImageBitmap = decodeBase64ToImage(proofImageBase64)
                    val quantity = document.getLong("quantity")?.toInt() ?: 0

                    bookingList.add(BookingItem(bookingCode, itemTitle, bookingDate, totalPrice, status, proofImageBitmap, userName, "", quantity, TiketQR = null, TiketQRBase64 = ""))
                }
                bookingAdapter = BookingAdapter(bookingList)
                recyclerView.adapter = bookingAdapter
            }
            .addOnFailureListener { exception ->
                Log.e("RiwayatPemesananActivity", "Error fetching booking data: ${exception.message}")
            }
    }

    private fun decodeBase64ToImage(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            Log.e("RiwayatPemesananActivity", "Error decoding Base64 string: ${e.message}")
            null
        }
    }
}