package com.example.siwangan.Activity.Admin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.DataClass.BookingItem
import com.example.siwangan.Activity.Decoration.SpacingItemDecoration
import com.example.siwangan.Adapter.BookingAdapter
import com.example.siwangan.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminRiwayatPesananTiketActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var bookingAdapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_riwayat_pesanan_tiket)
        supportActionBar?.hide()

        firestore = FirebaseFirestore.getInstance()
        initBookingHistory()
    }

    private fun initBookingHistory() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAdminBookingHistory)
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
                    bookingList.add(BookingItem(bookingCode, itemTitle, bookingDate, totalPrice, status))
                }
                bookingAdapter = BookingAdapter(bookingList)
                recyclerView.adapter = bookingAdapter
            }
            .addOnFailureListener { exception ->
                Log.e("AdminRiwayatPesananTiketActivity", "Error fetching booking data: ${exception.message}")
            }
    }
}