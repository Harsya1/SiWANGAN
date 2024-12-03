package com.example.siwangan.Activity.BookingTicket

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.databinding.ActivityUploadBuktiTrasferBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.io.InputStream

class UploadBuktiTrasferActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBuktiTrasferBinding
    private lateinit var database: DatabaseReference
    private var encodedImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUploadBuktiTrasferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        // Retrieve data from Intent
        val bookingCode = intent.getStringExtra("bookingCode")
        val userName = intent.getStringExtra("userName")
        val userPhone = intent.getStringExtra("userPhone")
        val totalHarga = intent.getIntExtra("totalHarga", 0)

        // Display data in TextViews
        binding.txtIdBooking.text = bookingCode
        binding.txtNama.text = userName
        binding.txtNoTelepon.text = userPhone
        binding.txtJumlahBayar.text = "Rp$totalHarga"

        binding.imgBuktiTransfer.setOnClickListener {
            pickImageFromGallery()
        }

        binding.btnBayar.setOnClickListener {
            storeBookingData()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            val inputStream: InputStream? = imageUri?.let { contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.imgBuktiTransfer.setImageBitmap(bitmap)
            encodedImage = encodeImage(bitmap)
        }
    }

    private fun encodeImage(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun storeBookingData() {
        val userId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")
        val userPhone = intent.getStringExtra("userPhone")
        val itemTitle = intent.getStringExtra("itemTitle")
        val bookingCode = intent.getStringExtra("bookingCode")
        val quantity = intent.getIntExtra("quantity", 1)
        val selectedDate = intent.getStringExtra("selectedDate")

        if (userId != null && itemTitle != null && encodedImage != null) {
            val bookingData = mapOf(
                "userId" to userId,
                "userName" to userName,
                "userPhone" to userPhone,
                "itemTitle" to itemTitle,
                "bookingCode" to bookingCode,
                "quantity" to quantity,
                "selectedDate" to selectedDate,
                "status" to "pending",
                "proofImage" to encodedImage
            )
            database.child("bookings").push().setValue(bookingData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Booking successful", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error saving booking data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User or item data is missing", Toast.LENGTH_SHORT).show()
        }
    }
}