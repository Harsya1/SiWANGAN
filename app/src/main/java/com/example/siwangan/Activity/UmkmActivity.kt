package com.example.siwangan.Activity

import android.os.Bundle
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
    ): View? {
        // Inflate layout menggunakan View Binding
        _binding = ActivityUmkmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView
        initUmkm()

        // Inisialisasi Slider
        //setupSliderAndIndicators()
    }

    private fun initUmkm() {
        binding.apply {
            viewModelUmkm.load().observe(viewLifecycleOwner) { UmkmList ->
                RecyclerViewPageUmkm.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
                RecyclerViewPageUmkm.adapter = UMKMAdapter(UmkmList)
            }
        }
    }
}