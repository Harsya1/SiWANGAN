package com.example.siwangan.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.siwangan.Activity.DetailUmkmActivity
import com.example.siwangan.Domain.Item
import com.example.siwangan.databinding.ViewholderUmkmBinding

class UMKMAdapter(val items: List<Item>) : RecyclerView.Adapter<UMKMAdapter.ViewHolder>() {

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

            Glide.with(holder.itemView.context)
                .load(item.picumkm)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(imageView2)

            holder.binding.root.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailUmkmActivity::class.java)
                intent.putExtra("item", items[position]) // Mengirim data item ke aktivitas berikutnya
                holder.itemView.context.startActivity(intent) // Memulai aktivitas
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
