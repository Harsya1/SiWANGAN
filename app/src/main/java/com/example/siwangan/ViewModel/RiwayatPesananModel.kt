package com.example.siwangan.ViewModel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import com.example.siwangan.Domain.RiwayatPemesanan
//import com.example.siwangan.Repository.RiwayatPesananRepository
//
//class RiwayatPesananModel : ViewModel() {
//
//    private val repository = RiwayatPesananRepository()
//
//    fun load(userName: String?): LiveData<MutableList<RiwayatPemesanan>> {
//        return repository.load(
//            userName
//        )
//    }
//}