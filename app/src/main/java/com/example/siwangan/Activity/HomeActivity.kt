package com.example.siwangan.Activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siwangan.Adapter.BannerAdapter
import com.example.siwangan.Adapter.LayananAdapter
import com.example.siwangan.Adapter.UMKMAdapter
import com.example.siwangan.ViewModel.BannerViewModel
import com.example.siwangan.ViewModel.MainViewModel
import com.example.siwangan.ViewModel.UMKMViewModel
import com.example.siwangan.databinding.ActivityHomeBinding

class HomeFragment : Fragment() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!! // View Binding untuk layout fragment
    private val viewModel = MainViewModel() // ViewModel instance
    private val viewModelUmkm = UMKMViewModel()
    private val viewBanner = BannerViewModel()

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
        initBanner()

        // Setup listener untuk search field
        setupSearchField()

        // Button Search untuk memicu pencarian dan reset data jika kosong
        binding.btnSearch.setOnClickListener {
            val query = binding.SearchField.text.toString().trim()
            if (query.isNotEmpty()) {
                searchUmkm(query)
                searchLayanan(query)
            } else {
                // Menampilkan semua data jika teks kosong
                initUmkm()
                initLayanan()
                initBanner()
            }
        }
        // Inisialisasi Slider
        //setupSliderAndIndicators()
    }

    private fun setupSearchField() {
        binding.SearchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val query = charSequence.toString().trim()

                // Tampilkan hasil pencarian per kata saat mengetik
                if (query.isNotEmpty()) {
                    searchUmkm(query)
                    searchLayanan(query)
                } else {
                    // Jika search field kosong, tampilkan semua data
                    initUmkm()
                    initLayanan()
                    initBanner()
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    private fun searchUmkm(query: String) {
        binding.apply {
            progressBarUmkm.visibility = View.VISIBLE
            viewModelUmkm.load().observe(viewLifecycleOwner) { umkmList ->
                val filteredList = umkmList.filter { umkm ->
                    umkm.titleumkm.contains(query, ignoreCase = true) || // Sesuaikan field data
                            umkm.description.contains(query, ignoreCase = true)
                }
                recyclerViewUmkm.adapter = UMKMAdapter(filteredList)
                progressBarUmkm.visibility = View.GONE
            }
        }
    }

    private fun searchLayanan(query: String) {
        binding.apply {
            progressBarLayanan.visibility = View.VISIBLE
            viewModel.load().observe(viewLifecycleOwner) { layananList ->
                val filteredList = layananList.filter { layanan ->
                    layanan.title.contains(query, ignoreCase = true) || // Sesuaikan field data
                            layanan.description.contains(query, ignoreCase = true)
                }
                recyclerViewLayanan.adapter = LayananAdapter(filteredList)
                progressBarLayanan.visibility = View.GONE
            }
        }
    }

    private fun initBanner() {
        binding.apply {
            progressBarBanner.visibility = View.VISIBLE
            // Mengamati perubahan data dari ViewModel
            viewBanner.load().observe(viewLifecycleOwner) { BannerList ->
                viewPager.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                viewPager.adapter = BannerAdapter(BannerList)
                progressBarBanner.visibility = View.GONE
            }
        }
    }

    private fun initUmkm() {
        binding.apply {
            progressBarUmkm.visibility = View.VISIBLE
            // Mengamati perubahan data dari ViewModel
            viewModelUmkm.load().observe(viewLifecycleOwner) { UmkmList ->
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
