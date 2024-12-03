// RiwayatPemesananActivity.kt
package com.example.siwangan.Activity

import android.os.Bundle
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
    }

    private fun initBookingHistory(userName: String) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewBookingHistory)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(SpacingItemDecoration(16)) // Add spacing of 16dp between items

        firestore.collection("bookings")
            .whereEqualTo("userName", userName)
            .get()
            .addOnSuccessListener { documents ->
                val bookingList = mutableListOf<BookingItem>()
                for (document in documents) {
                    val bookingCode = document.getString("bookingCode") ?: ""
                    val itemTitle = document.getString("itemTitle") ?: ""
                    val bookingDate = document.getString("selectedDate") ?: ""
                    val totalPrice = document.getDouble("totalPrice")?.toString() ?: ""
                    val status = document.getString("status") ?: ""
                    bookingList.add(BookingItem(bookingCode, itemTitle, bookingDate, totalPrice, status))
                }
                bookingAdapter = BookingAdapter(bookingList)
                recyclerView.adapter = bookingAdapter
            }
            .addOnFailureListener { exception ->
                Log.e("RiwayatPemesananActivity", "Error fetching booking data: ${exception.message}")
            }
    }

    }
