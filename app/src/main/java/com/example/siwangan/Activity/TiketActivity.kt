package com.example.siwangan.Activity

import android.content.Intent
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
import com.example.siwangan.Adapter.TiketBookingAdapter
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityTiketBinding
import com.google.firebase.firestore.FirebaseFirestore

class TiketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTiketBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var tiketBookingAdapter: TiketBookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTiketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firestore = FirebaseFirestore.getInstance()
        val profileName = intent.getStringExtra("profileName") ?: ""
        initTiketBooking(profileName)
    }

    private fun initTiketBooking(userName: String) {
        binding.recyclerViewBookingHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewBookingHistory.addItemDecoration(SpacingItemDecoration(16)) // Add spacing of 16dp between items

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
                    val tiketQRBase64 = document.getString("TiketQR") ?: ""
                    val tiketQRBitmap = decodeBase64ToImage(tiketQRBase64)
                    val quantity = document.getLong("quantity")?.toInt() ?: 0

                    bookingList.add(BookingItem(bookingCode, itemTitle, bookingDate, totalPrice, status, null, userName, "", quantity, tiketQRBitmap, tiketQRBase64))
                }
                tiketBookingAdapter = TiketBookingAdapter(bookingList) { bookingItem ->
                    val intent = Intent(this, TiketQRActivity::class.java)
                    intent.putExtra("TiketQR", bookingItem.TiketQRBase64)
                    startActivity(intent)
                }
                binding.recyclerViewBookingHistory.adapter = tiketBookingAdapter
            }
            .addOnFailureListener { exception ->
                Log.e("TiketActivity", "Error fetching booking data: ${exception.message}")
            }
    }

    private fun decodeBase64ToImage(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            Log.e("TiketActivity", "Error decoding Base64 string: ${e.message}")
            null
        }
    }
}