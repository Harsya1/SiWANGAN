package com.example.siwangan.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.siwangan.Domain.Item
import com.example.siwangan.Repository.MainRepository

class MainViewModel():ViewModel() {

    private val repository = MainRepository()

    fun load(): LiveData<MutableList<Item>> {
        return repository.load()
    }
}