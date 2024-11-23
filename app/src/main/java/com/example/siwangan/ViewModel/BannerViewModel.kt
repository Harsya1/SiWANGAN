package com.example.siwangan.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.siwangan.Domain.Item
import com.example.siwangan.Repository.BannerRepository
import com.example.siwangan.Repository.MainRepository
import com.example.siwangan.Repository.UMKMRepository

class BannerViewModel():ViewModel() {

    private val repository = BannerRepository()

    fun load(): LiveData<MutableList<Item>> {
        return repository.load()
    }
}