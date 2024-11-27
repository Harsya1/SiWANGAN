package com.example.siwangan.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.siwangan.Domain.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UMKMRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun load():LiveData<MutableList<Item>>{
        val listData = MutableLiveData<MutableList<Item>>()
        val  ref = firebaseDatabase.getReference("Umkm")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Item>()
                for (childSnapshot in snapshot.children){
                    val item = childSnapshot.getValue(Item::class.java)
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