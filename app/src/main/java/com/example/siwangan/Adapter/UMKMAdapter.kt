package com.example.siwangan.Adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.DetailUmkmActivity
import com.example.siwangan.Domain.ItemHolder
import com.example.siwangan.Helper.ImageCache
import com.example.siwangan.databinding.ViewholderUmkmBinding
import java.io.ByteArrayInputStream

class UMKMAdapter(val items: List<ItemHolder>) : RecyclerView.Adapter<UMKMAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderUmkmBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderUmkmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            titleTxt.text = item.titleumkm
            descTxt.text = item.descriptionumkm

            val bitmapUmkm = base64ToBitmap(item.picumkm)

            if (bitmapUmkm != null) {
                imageView2.setImageBitmap(bitmapUmkm)
            } else {
                Toast.makeText(holder.itemView.context, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
            }


            holder.binding.root.setOnClickListener {

                ImageCache.base64Image = item.picumkm
                ImageCache.base64Image = item.menu

                val intent = Intent(holder.itemView.context, DetailUmkmActivity::class.java)
                intent.putExtra("titleumkm", item.titleumkm)
                intent.putExtra("descriptionumkm", item.descriptionumkm)
                intent.putExtra("contact", item.contact)
                holder.itemView.context.startActivity(intent) // Memulai aktivitas
            }
        }
    }

    override fun getItemCount(): Int = items.size

    //base64ToBitmap digunakan untuk convert dari base64 ke image
    private fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedBytes)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
