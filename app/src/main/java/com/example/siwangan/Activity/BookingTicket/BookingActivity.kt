package com.example.siwangan.Activity.BookingTicket

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.siwangan.Domain.Item
import com.example.siwangan.Domain.User
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityBookingBinding
import java.io.ByteArrayInputStream
import java.util.ResourceBundle.getBundle

class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private lateinit var item: Item
    private lateinit var user: User

    private var qty = 1
    private val maxQty = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtQty.text = qty.toString()

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getBundle()

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