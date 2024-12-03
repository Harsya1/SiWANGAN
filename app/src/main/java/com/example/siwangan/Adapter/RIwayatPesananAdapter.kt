package com.example.siwangan.Adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.siwangan.Domain.RiwayatPemesanan
//import com.example.siwangan.databinding.ViewholderRiwayatPesananBinding
//
//class RiwayatPesananAdapter(private val items: List<RiwayatPemesanan>) : RecyclerView.Adapter<RiwayatPesananAdapter.ViewHolder>() {
//
//    class ViewHolder(val binding: ViewholderRiwayatPesananBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ViewholderRiwayatPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = items[position]
//        holder.binding.apply {
//            txtIDBooking.text = item.bookingId
//            txtNamaPengunjung.text = item.userName
//            txtHargaLayanan.text = item.totalPrice
//            txtJumlahPengunjung.text = item.visitorCount
//            txtStatusBooking.text = item.status
//        }
//    }
//
//    override fun getItemCount(): Int = items.size
//}