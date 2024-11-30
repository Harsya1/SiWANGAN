package com.example.siwangan.Adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.siwangan.Domain.Item
import com.example.siwangan.databinding.ViewholderBannerBinding
import java.io.ByteArrayInputStream

class BannerAdapter(val items: MutableList<Item>) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderBannerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {

            val bitmap = base64ToBitmap(item.url)
            if (bitmap != null) {
                imageView2.setImageBitmap(bitmap) // Menampilkan gambar di ImageView
            } else {
                Toast.makeText(holder.itemView.context, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
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
