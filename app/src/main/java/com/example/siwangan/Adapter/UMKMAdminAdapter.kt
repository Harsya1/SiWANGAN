package com.example.siwangan.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.Admin.Update.UmkmUpdateActivity
import com.example.siwangan.databinding.ViewholderUmkmAdminBinding
import com.example.siwangan.Domain.itemUmkm
import com.google.firebase.database.FirebaseDatabase

class UMKMAdminAdapter(val itemumkm: List<itemUmkm>) : RecyclerView.Adapter<UMKMAdminAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderUmkmAdminBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderUmkmAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemUmkm = itemumkm[position]
        holder.binding.apply {
            txtId.text = itemUmkm.id
            txtTitleUmkmAdmin.text = itemUmkm.titleumkm
            txtDescUmkmAdmin.text = itemUmkm.descriptionumkm
            txtContactUmkmAdmin.text = itemUmkm.contact
            txtUrlPicUmkm.text = itemUmkm.picumkm
            txtUrlMenuUmkm.text = itemUmkm.menu

            // Intent ke UmkmUpdateActivity (Untuk buttonUpdate)
            buttonUpdate.setOnClickListener {
                val intent = Intent(holder.itemView.context, UmkmUpdateActivity::class.java)
                intent.putExtra("itemUmkm", itemUmkm) // Mengirim data item ke aktivitas UmkmUpdateActivity
                holder.itemView.context.startActivity(intent)
            }

            buttonDeleteData.setOnClickListener {
                // Mendapatkan ID dari itemUmkm
                val id = itemUmkm.id

                // Membuat referensi database Firebase berdasarkan ID
                val database = FirebaseDatabase.getInstance().getReference("Umkm").child(id)

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

    override fun getItemCount(): Int = itemumkm.size
}
