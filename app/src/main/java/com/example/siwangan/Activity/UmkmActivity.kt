package com.example.siwangan.Activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.siwangan.Adapter.UMKMAdapter
import com.example.siwangan.ViewModel.UMKMViewModel
import com.example.siwangan.databinding.ActivityUmkmBinding

class UmkmActivity : Fragment() {
    private var _binding: ActivityUmkmBinding? = null
    private val binding get() = _binding!! // View Binding untuk layout fragment
    private val viewModelUmkm = UMKMViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout menggunakan View Binding
        _binding = ActivityUmkmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView
        initUmkm()

        // Setup listener untuk search field
        setupSearchField()

        binding.SearchFieldUmkm.setOnClickListener {
            val query = binding.SearchFieldUmkm.text.toString().trim()
            if (query.isNotEmpty()) {
                searchUmkm(query)
            } else {
                // Hanya menampilkan semua data UMKM jika teks kosong
                initUmkm()
            }
        }
    }

    private fun setupSearchField() {
        binding.SearchFieldUmkm.addTextChangedListener(object : TextWatcher {
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

                // Tampilkan hasil pencarian UMKM saat mengetik
                if (query.isNotEmpty()) {
                    searchUmkm(query)
                } else {
                    // Jika search field kosong, hanya tampilkan data UMKM
                    initUmkm()
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }


    private fun searchUmkm(query: String) {
        binding.apply {
            progressBarkuy.visibility = View.VISIBLE
            viewModelUmkm.load().observe(viewLifecycleOwner) { umkmList ->
                val filteredList = umkmList.filter { umkm ->
                    umkm.titleumkm.contains(query, ignoreCase = true) || // Sesuaikan field data
                            umkm.description.contains(query, ignoreCase = true)
                }
                RecyclerViewPageUmkm.adapter = UMKMAdapter(filteredList)
                progressBarkuy.visibility = View.GONE

            }
        }
    }

    private fun initUmkm() {
        binding.apply {
            progressBarkuy.visibility = View.VISIBLE
            viewModelUmkm.load().observe(viewLifecycleOwner) { UmkmList ->
                RecyclerViewPageUmkm.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                RecyclerViewPageUmkm.adapter = UMKMAdapter(UmkmList)
                progressBarkuy.visibility = View.GONE

            }
        }
    }
}