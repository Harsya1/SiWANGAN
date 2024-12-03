package com.example.siwangan.Activity.BookingTicket

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityBookingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var itemTitle: String? = null

    private var qty = 1
    private val maxQty = 10
    private lateinit var KodeBooking: String
    private lateinit var selectedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setUniqueCode()
        setupListeners()
        fetchUserData(auth.currentUser?.uid ?: "")
        retrieveItemData()

        binding.btnTambahQty.setOnClickListener {
            if (qty < maxQty) {
                qty++
                binding.txtQty.text = qty.toString()
            }
        }

        binding.btnKurangQty.setOnClickListener {
            if (qty > 1) {
                qty--
                binding.txtQty.text = qty.toString()
            } else {
            }
        }


        binding.btnPesan.setOnClickListener {
            val hargaPerOrang = binding.txtHargaBookingLayanan.text.toString().replace("Rp", "").toInt()
            val totalHarga = hargaPerOrang * qty

            val intent = Intent(this, UploadBuktiTrasferActivity::class.java)
            intent.putExtra("userId", auth.currentUser?.uid)
            intent.putExtra("userName", binding.txtNamaPengunjung.text.toString())
            intent.putExtra("userPhone", binding.txtNoTelp.text.toString())
            intent.putExtra("itemTitle", itemTitle)
            intent.putExtra("bookingCode", KodeBooking)
            intent.putExtra("quantity", qty)
            intent.putExtra("selectedDate", selectedDate)
            intent.putExtra("totalHarga", totalHarga)
            startActivity(intent)
        }
    }

    private fun setUniqueCode() {
        KodeBooking = generateUniqueCode()
        binding.txtKodeBooking.text = KodeBooking
    }

    private fun setupListeners() {
        binding.imageView3.setOnClickListener {
            finish()
        }

        binding.btnPickDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.txtFieldDate.setText(selectedDate)
        }, year, month, day)

        // Set the minimum date to today
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun generateUniqueCode(): String {
        val random = java.util.Random()
        val code = StringBuilder("TKT")
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
                        android.util.Log.e("BookingActivity", "Data snapshot does not exist!")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    android.util.Log.e("BookingActivity", "Database error: ${databaseError.message}")
                }
            })
    }

    private fun retrieveItemData() {
        itemTitle = intent.getStringExtra("title")
        binding.txtTitle.text = itemTitle

        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        if (bitmap != null) {
            binding.imageView7.setImageBitmap(bitmap)
        } else {
            binding.imageView7.setImageResource(R.drawable.error_image)
        }
    }

}