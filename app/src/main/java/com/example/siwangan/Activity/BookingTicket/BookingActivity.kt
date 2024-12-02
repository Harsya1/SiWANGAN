package com.example.siwangan.Activity.BookingTicket

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.Domain.Item
import com.example.siwangan.Domain.User
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityBookingBinding
import java.io.ByteArrayInputStream
import java.util.ResourceBundle.getBundle
import java.text.SimpleDateFormat
import java.util.*

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding
    private lateinit var item: Item
    private lateinit var user: User

    private var qty = 1
    private val maxQty = 10
    private lateinit var KodeBooking: String
    private var quantity: Int = 0
    private lateinit var selectedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUniqueCode()
        setupListeners()

//
//        val calendar = Calendar.getInstance()
//        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
//            calendar.set(Calendar.YEAR, year)
//            calendar.set(Calendar.MONTH, month)
//            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//            binding.etTanggal.setText(dateFormat.format(calendar.time))
//        }
//
//        val datePickerDialog = DatePickerDialog(
//            this,
//            dateSetListener,
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//
//        binding.txtFieldDat.setOnClickListener {
//            datePickerDialog.show()
//        }
        getBundle()
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
        // Listener untuk button tambah
        binding.btnTambahQty.setOnClickListener {
            if (qty < maxQty) {
                qty++
                binding.txtQty.text = qty.toString()
            }
        }

        binding.btnKurangQty.setOnClickListener {
            if (quantity > 0) {
                quantity--
                binding.txtQty.text = quantity.toString()
            } else {
                Toast.makeText(this, "Jumlah minimal adalah 1", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnTambahQty.setOnClickListener {
            if (quantity < 10) {
                quantity++
                binding.txtQty.text = quantity.toString()
            } else {
                Toast.makeText(this, "Jumlah Maksimal adalah 10", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.txtFieldDate.setText(selectedDate)
        }, year, month, day)
        datePickerDialog.show()
    }

    fun generateUniqueCode(): String {
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


    private fun getBundle() {
        item = intent.getParcelableExtra("item")!!
        user = intent.getParcelableExtra("user")!!

        binding.apply {
            txtTitle.text = item.title
            txtHargaBookingLayanan.text = item.price
            txtKodeBooking.text = generateUniqueCode()
            txtNamaPengunjung.text = user.name
            txtNoTelp.text = user.phone

            val bitmap = base64ToBitmap(item.pic) // Assuming `item.pic` contains the Base64 string
            if (bitmap != null) {
                imageView7.setImageBitmap(bitmap)
            } else {
                imageView7.setImageResource(R.drawable.error_image) // Placeholder image if decoding fails
            }
        }
    }

    private fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedBytes)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}