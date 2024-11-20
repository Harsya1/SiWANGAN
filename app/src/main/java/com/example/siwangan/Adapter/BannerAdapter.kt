package com.example.siwangan.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.siwangan.Domain.Item
import com.example.siwangan.databinding.ViewholderBannerBinding

class BannerAdapter(val items: MutableList<Item>) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderBannerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {

            Glide.with(holder.itemView.context)
                .load(item.url)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(imageView2)
        }
    }

    override fun getItemCount(): Int = items.size
}
