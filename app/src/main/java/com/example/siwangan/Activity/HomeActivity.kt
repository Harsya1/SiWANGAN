package com.example.siwangan.Activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Adapter.BannerAdapter
import com.example.siwangan.Adapter.LayananAdapter
import com.example.siwangan.Adapter.UMKMAdapter
import com.example.siwangan.R
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
        binding.selengkapnyaUmkm.setOnClickListener{
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_fragment, UmkmActivity()) // Ganti fragment_container dengan ID container Anda
            transaction.addToBackStack(null) // Tambahkan ke back stack (opsional, untuk navigasi back)
            transaction.commit()
        }
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

            viewBanner.load().observe(viewLifecycleOwner) { BannerList ->
                val bannerAdapter = BannerAdapter(BannerList)
                viewPager.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                viewPager.adapter = bannerAdapter

                // Setup indikator
                setupIndicators(BannerList.size)
                updateIndicator(0)

                // Auto-scroll dengan handler
                val handler = android.os.Handler()
                val runnable = object : Runnable {
                    var currentPosition = 0
                    override fun run() {
                        if (bannerAdapter.itemCount > 0) {
                            currentPosition = (currentPosition + 1) % bannerAdapter.itemCount
                            viewPager.smoothScrollToPosition(currentPosition)
                            updateIndicator(currentPosition)
                        }
                        handler.postDelayed(this, 5000)
                    }
                }
                handler.postDelayed(runnable, 5000)

                // Bersihkan handler saat fragment dihancurkan
                viewLifecycleOwner.lifecycle.addObserver(
                    object : androidx.lifecycle.DefaultLifecycleObserver {
                        override fun onDestroy(owner: androidx.lifecycle.LifecycleOwner) {
                            handler.removeCallbacks(runnable)
                        }
                    }
                )

                progressBarBanner.visibility = View.GONE
            }
        }
    }


    private fun setupIndicators(count: Int) {
        binding.sliderIndicators.removeAllViews() // Hapus indikator lama

        // Tambahkan indikator ke dalam LinearLayout
        for (i in 0 until count) {
            val indicator = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    8.dpToPx(), // Lebar default 8dp
                    10.dpToPx() // Tinggi default 10dp
                ).apply {
                    marginStart = 20.dpToPx()
                    marginEnd = 20.dpToPx()
                }
                background = requireContext().getDrawable(R.drawable.indicator_inactive)
            }
            binding.sliderIndicators.addView(indicator)
        }

        // Set indikator awal sebagai aktif
        updateIndicator(0) // Indikator awal
    }

    private fun updateIndicator(activePosition: Int) {
        val indicatorCount = binding.sliderIndicators.childCount

        for (i in 0 until indicatorCount) {
            val indicator = binding.sliderIndicators.getChildAt(i)

            // Perbarui ukuran dan drawable untuk indikator aktif dan tidak aktif
            val layoutParams = indicator.layoutParams as LinearLayout.LayoutParams
            if (i == activePosition) {
                layoutParams.width = 70.dpToPx() // Lebar 50dp untuk aktif
                indicator.background = requireContext().getDrawable(R.drawable.indicator_active)
            } else {
                layoutParams.width = 8.dpToPx() // Lebar 8dp untuk tidak aktif
                indicator.background = requireContext().getDrawable(R.drawable.indicator_inactive)
            }
            indicator.layoutParams = layoutParams
        }
    }

    // Extension function to convert dp to px
    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()


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