package com.example.siwangan.Activity.Booking

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityBookingUmkmBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.*

class BookingUmkmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingUmkmBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookingUmkmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Retrieve data from intent
        val bookingCode = intent.getStringExtra("bookingCode") ?: generateUniqueCode()
        val title = intent.getStringExtra("title")
        val imageUri = intent.getStringExtra("imageUri")

        // Fill in the fields
        binding.txtKodeBooking.text = bookingCode
        binding.txtTitle.text = title

        // Load image from URI
        if (imageUri != null) {
            binding.imageView7.setImageURI(Uri.parse(imageUri))
        }

        fetchUserData(auth.currentUser?.uid ?: "")

        binding.btnPesan.setOnClickListener {
            val visitorName = binding.txtNamaPengunjung.text.toString()
            val noTelp = binding.txtNoTelp.text.toString()
            val pesananDetail = binding.txtPesananDetail.text.toString()
            storeBookingData(bookingCode, title, visitorName, noTelp, pesananDetail)
            finish()
        }
        binding.imageView3.setOnClickListener {
            finish()
        }
    }

    private fun fetchUserData(userId: String) {
        database.child("Users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val namaLengkap = dataSnapshot.child("name").getValue(String::class.java)
                        val nomorTelepon = dataSnapshot.child("phone").getValue(String::class.java)

                        binding.txtNamaPengunjung.text = namaLengkap ?: "N/A"
                        binding.txtNoTelp.text = nomorTelepon ?: "N/A"
                    } else {
                        android.util.Log.e("BookingUmkmActivity", "Data snapshot does not exist!")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    android.util.Log.e("BookingUmkmActivity", "Database error: ${databaseError.message}")
                }
            })
    }

    private fun generateUniqueCode(): String {
        val random = java.util.Random()
        val code = StringBuilder("UMKM")
        for (i in 0 until 6) {
            val digit = random.nextInt(36)
            if (digit < 10) {
                code.append(digit)
            } else {
                code.append('A' + (digit - 10))
            }
        }
        return code.toString()
    }

    private fun storeBookingData(bookingCode: String?, title: String?, visitorName: String?, noTelp: String?, pesananDetail: String?) {
        if (bookingCode == null || title == null || visitorName == null || noTelp == null || pesananDetail == null) {
            Toast.makeText(this, "Data is missing", Toast.LENGTH_SHORT).show()
            return
        }

        val bookingData = hashMapOf(
            "bookingCode" to bookingCode,
            "title" to title,
            "visitorName" to visitorName,
            "noTelp" to noTelp,
            "pesananDetail" to pesananDetail
        )

        firestore.collection("UMKM Bookings")
            .add(bookingData)
            .addOnSuccessListener {
                Toast.makeText(this, "Pesanan Sudah Dibuat, tunggu pesanan dalam waktu 15 menit", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error storing booking: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}