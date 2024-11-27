package com.example.siwangan.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.Admin.Update.BannerUpdateActivity
import com.example.siwangan.Domain.itemBanner
import com.example.siwangan.databinding.ViewholderBannerAdminBinding
import com.google.firebase.database.FirebaseDatabase

class BannerAdminAdapter(val items: List<itemBanner>) : RecyclerView.Adapter<BannerAdminAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderBannerAdminBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderBannerAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemBanner = items[position]
        holder.binding.apply {
            idBanner.text = itemBanner.idB
            txtDataBanner.text = itemBanner.url

            buttonUpdate.setOnClickListener {
                val intent = Intent(holder.itemView.context, BannerUpdateActivity::class.java)
                intent.putExtra("itemBanner", itemBanner) // Mengirim data item ke aktivitas UmkmUpdateActivity
                holder.itemView.context.startActivity(intent)
            }

            buttonDeleteData.setOnClickListener {
                val id = itemBanner.idB
                val database = FirebaseDatabase.getInstance().getReference("Banner").child(id)

                database.removeValue().addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()

                    // Menghapus item dari daftar dan memperbarui RecyclerView
                    (items as MutableList).removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                }.addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "Data Gagal Dihapus", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun getItemCount(): Int = items.size
}
