package com.example.siwangan.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.DetailRiwayatPesananTiketActivity
import com.example.siwangan.Activity.DataClass.BookingItem
import com.example.siwangan.databinding.ViewholderRiwayarPesananBinding

class BookingAdapter(private var bookingList: List<BookingItem>) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ViewholderRiwayarPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val bookingItem = bookingList[position]
        holder.bind(bookingItem)
    }

    override fun getItemCount(): Int = bookingList.size

    inner class BookingViewHolder(private val binding: ViewholderRiwayarPesananBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookingItem: BookingItem) {
            binding.txtBookingCode.text = bookingItem.bookingCode
            binding.txtItemTitle.text = bookingItem.itemTitle
            binding.txtBookingDate.text = bookingItem.bookingDate
            binding.txtQuantity.text = bookingItem.quantity.toString()
            binding.txtTotalPrice.text = bookingItem.totalPrice
            binding.txtStatus.text = bookingItem.status

            // Set the image
            bookingItem.proofImage?.let {
                binding.imgProof.setImageBitmap(it)
            }

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailRiwayatPesananTiketActivity::class.java).apply {
                    putExtra("bookingCode", bookingItem.bookingCode)
                    putExtra("itemTitle", bookingItem.itemTitle)
                    putExtra("bookingDate", bookingItem.bookingDate)
                    putExtra("quantity", bookingItem.quantity)
                    putExtra("totalPrice", bookingItem.totalPrice)
                    putExtra("status", bookingItem.status)
                }
                context.startActivity(intent)
            }
        }
    }

}