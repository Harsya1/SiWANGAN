package com.example.siwangan.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.siwangan.Domain.ItemHolder
import com.example.siwangan.Repository.BannerRepository

class BannerViewModel():ViewModel() {

    private val repository = BannerRepository()

    fun load(): LiveData<MutableList<ItemHolder>> {
        return repository.load()
    }
}