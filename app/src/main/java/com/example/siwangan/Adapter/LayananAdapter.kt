package com.example.siwangan.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.siwangan.Domain.Item
import com.example.siwangan.databinding.ViewholderLayananBinding

class LayananAdapter(val items: MutableList<Item>) : RecyclerView.Adapter<LayananAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderLayananBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderLayananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            titleTxt.text = item.title
            priceTxt.text = "$${item.price}"
            descTxt.text = item.description
            scoreTxt.text = item.score.toString()

            Glide.with(holder.itemView.context)
                .load(item.pic)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(imageView2)
        }
    }

    override fun getItemCount(): Int = items.size
}
