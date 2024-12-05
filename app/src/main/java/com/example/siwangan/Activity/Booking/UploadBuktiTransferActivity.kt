package com.example.siwangan.Activity.Booking

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.databinding.ActivityUploadBuktiTransferBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.InputStream

class UploadBuktiTransferActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBuktiTransferBinding
    private lateinit var firestore: FirebaseFirestore
    private var encodedImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUploadBuktiTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

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
            showConfirmationPopup()
            storeBookingData()
            finish()
        }
        binding.btnBackbro.setOnClickListener {
            finish()
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
        val totalprice = intent.getIntExtra("totalHarga", 0)
        val selectedDate = intent.getStringExtra("selectedDate")

        if (userId != null && itemTitle != null && encodedImage != null) {
            firestore.collection("bookings")
                .orderBy("id", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    var newId = 1
                    if (!documents.isEmpty) {
                        val highestId = documents.documents[0].getLong("id") ?: 0
                        newId = highestId.toInt() + 1
                    }

                    val bookingData = mapOf(
                        "id" to newId,
                        "userId" to userId,
                        "userName" to userName,
                        "userPhone" to userPhone,
                        "itemTitle" to itemTitle,
                        "bookingCode" to bookingCode,
                        "quantity" to quantity,
                        "totalPrice" to totalprice,
                        "selectedDate" to selectedDate,
                        "status" to "pending",
                        "proofImage" to encodedImage
                    )

                    firestore.collection("bookings").document(newId.toString()).set(bookingData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Booking berhasil, tunggu verifikasi admin maksimal 1x24 jam", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Error saving booking data: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error fetching highest ID: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "User or item data is missing", Toast.LENGTH_LONG).show()
        }
    }

    private fun showConfirmationPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi")
        builder.setMessage("Pesanan Tiket akan dikirimkan admin untuk diverfikasi. Tunggu dalam waktu maksimal 1x24jam")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }
}