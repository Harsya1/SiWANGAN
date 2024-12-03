package com.example.siwangan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.DataClass.BookingItem
import com.example.siwangan.R

class BookingAdapter(private val bookingList: List<BookingItem>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtBookingCode: TextView = itemView.findViewById(R.id.txtBookingCode)
        val txtItemTitle: TextView = itemView.findViewById(R.id.txtItemTitle)
        val txtBookingDate: TextView = itemView.findViewById(R.id.txtBookingDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_riwayat_pesanan, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val bookingItem = bookingList[position]
        holder.txtBookingCode.text = bookingItem.bookingCode
        holder.txtItemTitle.text = bookingItem.itemTitle
        holder.txtBookingDate.text = bookingItem.bookingDate
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }
}