package com.example.siwangan.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.siwangan.Adapter.LayananAdapter
import com.example.siwangan.Adapter.UMKMAdapter
import com.example.siwangan.R
import com.example.siwangan.ViewModel.MainViewModel
import com.example.siwangan.ViewModel.UMKMViewModel
import com.example.siwangan.databinding.ActivityHomeBinding

class HomeFragment : Fragment() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!! // View Binding untuk layout fragment
    private val viewModel = MainViewModel() // ViewModel instance
    private val viewModelUmkm = UMKMViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout menggunakan View Binding
        _binding = ActivityHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView
        initLayanan()
        initUmkm()

        // Inisialisasi Slider
        //setupSliderAndIndicators()
    }

    private fun initUmkm() {
        binding.apply {
            progressBarUmkm.visibility = View.VISIBLE
            // Mengamati perubahan data dari ViewModel
            viewModel.load().observe(viewLifecycleOwner) { UmkmList ->
                recyclerViewUmkm.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                recyclerViewUmkm.adapter = UMKMAdapter(UmkmList)
                progressBarUmkm.visibility = View.GONE
            }
        }
    }

    private fun initLayanan() {
        binding.apply {
            progressBarLayanan.visibility = View.VISIBLE
            // Mengamati perubahan data dari ViewModel
            viewModel.load().observe(viewLifecycleOwner) { layananList ->
                recyclerViewLayanan.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                recyclerViewLayanan.adapter = LayananAdapter(layananList)
                progressBarLayanan.visibility = View.GONE
            }
        }
    }
}
