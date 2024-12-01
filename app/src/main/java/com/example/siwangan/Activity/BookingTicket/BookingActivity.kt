package com.example.siwangan.Activity.BookingTicket

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.databinding.ActivityBookingBinding
import java.text.SimpleDateFormat
import java.util.*

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding
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
    }

    private fun generateUniqueCode(): String {
        val random = Random()
        val code = StringBuilder()
        for (i in 0 until 6) {
            val digit = random.nextInt(36)
            if (digit < 10) {
                code.append(digit)
            } else {
                code.append((digit - 10 + 'A'.code).toChar())
            }
        }
        return code.toString()
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
}