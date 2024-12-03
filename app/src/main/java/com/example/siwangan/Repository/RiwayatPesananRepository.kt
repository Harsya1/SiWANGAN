package com.example.siwangan.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.siwangan.Domain.RiwayatPemesanan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RiwayatPesananRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun load(userName: String?): LiveData<MutableList<RiwayatPemesanan>> {
        val listData = MutableLiveData<MutableList<RiwayatPemesanan>>()
        val ref = firebaseDatabase.getReference("Bookings")

        ref.orderByChild("userName").equalTo(userName).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<RiwayatPemesanan>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(RiwayatPemesanan::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error if needed
            }
        })
        return listData
    }
}