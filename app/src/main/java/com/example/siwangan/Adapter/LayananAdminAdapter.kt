package com.example.siwangan.Adapter

import android.app.Activity
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
import com.example.siwangan.Activity.Admin.Update.BannerUpdateActivity
import com.example.siwangan.Activity.Admin.Update.LayananUpdateActivity
import com.example.siwangan.Activity.Admin.Update.UmkmUpdateActivity
import com.example.siwangan.Domain.itemLayanan
import com.example.siwangan.databinding.ViewholderLayananAdminBinding
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayInputStream

class LayananAdminAdapter(val items: List<itemLayanan>) : RecyclerView.Adapter<LayananAdminAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderLayananAdminBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderLayananAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemLayanan = items[position]
        holder.binding.apply {
            txtJudulLayanan.text = itemLayanan.title
            txtHargaLayanan.text = "${itemLayanan.price}"
            txtDescLayananAdmin.text = itemLayanan.description
            txtScoreLayanan.text = itemLayanan.score.toString()

            val bitmap = base64ToBitmap(itemLayanan.pic)
            if (bitmap != null) {
                imageViewHolderLayanan.setImageBitmap(bitmap)
            } else {
                Toast.makeText(holder.itemView.context, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
            }

            buttonUpdateLayanan.setOnClickListener {
                val intent = Intent(holder.itemView.context, LayananUpdateActivity::class.java)
                intent.putExtra("title", itemLayanan.title)
                intent.putExtra("description", itemLayanan.description)
                intent.putExtra("price", itemLayanan.price)
                intent.putExtra("score", itemLayanan.score)
                holder.itemView.context.startActivity(intent)
            }


            buttonDeleteData.setOnClickListener {
                // Mendapatkan ID dari itemUmkm
                val id = itemLayanan.title

                // Membuat referensi database Firebase berdasarkan ID
                val database = FirebaseDatabase.getInstance().getReference("Layanan").child(id)

                // Menghapus data berdasarkan ID
                database.removeValue().addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()

                    // Mengupdate RecyclerView setelah penghapusan
                    val context = holder.itemView.context
                    if (context is Activity) {
                        context.recreate() // Mengulang aktivitas untuk memperbarui data di RecyclerView
                    }
                }.addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "Data Gagal Dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
    // Fungsi untuk mengonversi Base64 menjadi Bitmap
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
