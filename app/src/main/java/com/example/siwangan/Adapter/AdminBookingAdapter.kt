package com.example.siwangan.Adapter

import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.Admin.AdminDetailRiwayatPesananTiketActivity
import com.example.siwangan.Activity.DataClass.BookingItem
import com.example.siwangan.databinding.ViewholderAdminRiwayatPesananBinding
import java.io.ByteArrayOutputStream

class AdminBookingAdapter(private val bookingList: List<BookingItem>) : RecyclerView.Adapter<AdminBookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ViewholderAdminRiwayatPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val bookingItem = bookingList[position]
        holder.bind(bookingItem)
    }

    override fun getItemCount(): Int = bookingList.size

    inner class BookingViewHolder(private val binding: ViewholderAdminRiwayatPesananBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookingItem: BookingItem) {
            binding.txtBookingCode.text = bookingItem.bookingCode
            binding.txtItemTitle.text = bookingItem.itemTitle
            binding.txtBookingDate.text = bookingItem.bookingDate
            binding.txtTotalPrice.text = bookingItem.totalPrice
            binding.txtStatus.text = bookingItem.status
            binding.txtUserName.text = bookingItem.userName
            binding.txtUserPhone.text = bookingItem.userPhone
            binding.txtQuantity.text = bookingItem.quantity.toString()

            // Set the image if available
            bookingItem.proofImage?.let {
                binding.imgProof.setImageBitmap(it)
            }

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, AdminDetailRiwayatPesananTiketActivity::class.java).apply {
                    putExtra("bookingCode", bookingItem.bookingCode)
                }
                context.startActivity(intent)
            }
        }
    }
}