package com.example.siwangan

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siwangan.Adapter.LayananAdapter
import com.example.siwangan.ViewModel.MainViewModel
import com.example.siwangan.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi RecyclerView
        initLayanan()

    }

    private fun initLayanan() {
        binding.apply {
            progressBarLayanan.visibility = View.VISIBLE
            viewModel.load().observe(this@HomeActivity) { layananList ->
                recyclerViewLayanan.layoutManager = LinearLayoutManager(
                    this@HomeActivity, LinearLayoutManager.HORIZONTAL, false
                )
                recyclerViewLayanan.adapter = LayananAdapter(layananList)
                progressBarLayanan.visibility = View.GONE
            }
        }
    }
}
