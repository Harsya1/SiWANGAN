package com.example.siwangan.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.siwangan.Domain.ItemHolder
import com.example.siwangan.Repository.UMKMRepository

class UMKMViewModel():ViewModel() {

    private val repository = UMKMRepository()

    fun load(): LiveData<MutableList<ItemHolder>> {
        return repository.load()
    }
}