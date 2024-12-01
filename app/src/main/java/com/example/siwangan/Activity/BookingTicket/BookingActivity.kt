package com.example.siwangan.Activity.BookingTicket

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityBookingBinding

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding

    private var qty = 1
    private val maxQty = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtQty.text = qty.toString()

        enableEdgeToEdge()
        setContentView(R.layout.activity_booking)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Listener untuk button tambah
        binding.btnTambahQty.setOnClickListener {
            if (qty < maxQty) {
                qty++
                binding.txtQty.text = qty.toString()
            } else {
                Toast.makeText(this, "Jumlah maksimal adalah $maxQty", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener untuk button kurang
        binding.btnKurangQty.setOnClickListener {
            if (qty > 1) {
                qty--
                binding.txtQty.text = qty.toString()
            } else {
                Toast.makeText(this, "Jumlah minimal adalah 1", Toast.LENGTH_SHORT).show()
            }
        }

    }
}