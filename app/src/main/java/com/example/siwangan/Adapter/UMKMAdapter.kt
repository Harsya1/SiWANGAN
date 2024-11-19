package com.example.siwangan.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.siwangan.Domain.Item
import com.example.siwangan.databinding.ViewholderUmkmBinding

class UMKMAdapter(val items: MutableList<Item>) : RecyclerView.Adapter<UMKMAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderUmkmBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderUmkmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            titleTxt.text = item.titleumkm
            descTxt.text = item.description

            Glide.with(holder.itemView.context)
                .load(item.picumkm)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(imageView2)
        }
    }

    override fun getItemCount(): Int = items.size
}
