package com.example.siwangan.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.siwangan.Domain.ItemHolder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BannerRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun load():LiveData<MutableList<ItemHolder>>{
        val listData = MutableLiveData<MutableList<ItemHolder>>()
        val  ref = firebaseDatabase.getReference("Banner")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ItemHolder>()
                for (childSnapshot in snapshot.children){
                    val item = childSnapshot.getValue(ItemHolder::class.java)
                    item?.let { list.add(it) }
                }
                listData.value=list
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return listData
    }

}