package com.example.siwangan.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.MainActivity
import com.example.siwangan.R
import com.example.siwangan.databinding.ActivityMenungguVerifikasiBinding

class MenungguVerifikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenungguVerifikasiBinding
    private lateinit var buttonKembali: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menunggu_verifikasi)

        buttonKembali.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}