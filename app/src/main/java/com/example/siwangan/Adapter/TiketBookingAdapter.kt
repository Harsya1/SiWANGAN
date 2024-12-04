package com.example.siwangan.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.DataClass.BookingItem
import com.example.siwangan.databinding.ViewholderTiketBinding

class TiketBookingAdapter(
    private val bookingList: List<BookingItem>,
    private val onItemClick: (BookingItem) -> Unit
) : RecyclerView.Adapter<TiketBookingAdapter.TiketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiketViewHolder {
        val binding = ViewholderTiketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TiketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TiketViewHolder, position: Int) {
        val bookingItem = bookingList[position]
        holder.bind(bookingItem)
        holder.itemView.setOnClickListener { onItemClick(bookingItem) }
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    class TiketViewHolder(private val binding: ViewholderTiketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookingItem: BookingItem) {
            binding.txtBookingCode.text = bookingItem.bookingCode
            binding.txtItemTitle.text = bookingItem.itemTitle
            binding.txtBookingDate.text = bookingItem.bookingDate
            binding.txtQuantity.text = bookingItem.quantity.toString()
            binding.txtTotalPrice.text = bookingItem.totalPrice
            binding.txtStatus.text = bookingItem.status
            binding.TiketQR.setImageBitmap(bookingItem.TiketQR)
        }
    }
}