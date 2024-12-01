package com.example.siwangan.Adapter

import android.content.Intent
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
import com.example.siwangan.Activity.DetailLayananActivity
import com.example.siwangan.Domain.Item
import com.example.siwangan.databinding.ViewholderLayananBinding
import java.io.ByteArrayInputStream

class LayananAdapter(val items: List<Item>) : RecyclerView.Adapter<LayananAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderLayananBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderLayananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            titleTxt.text = item.title
            priceTxt.text = "${item.price}"
            descTxt.text = item.description
            scoreTxt.text = item.score.toString()

            val bitmapUmkm = base64ToBitmap(item.picumkm)

            if (bitmapUmkm != null) {
                imageView2.setImageBitmap(bitmapUmkm)
            } else {
                Toast.makeText(holder.itemView.context, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
            }

            holder.binding.root.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailLayananActivity::class.java)
                intent.putExtra("item", items[position]) // Mengirim data item ke aktivitas berikutnya
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
