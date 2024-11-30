import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.Admin.Update.BannerUpdateActivity
import com.example.siwangan.Domain.itemBanner
import com.example.siwangan.databinding.ViewholderBannerAdminBinding
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayInputStream

class BannerAdminAdapter(val items: MutableList<itemBanner>) : RecyclerView.Adapter<BannerAdminAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderBannerAdminBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderBannerAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemBanner = items[position]
        holder.binding.apply {
            idBanner.text = itemBanner.idB

            // Mengonversi Base64 menjadi Bitmap
            val bitmap = base64ToBitmap(itemBanner.url)
            if (bitmap != null) {
                imageBanner.setImageBitmap(bitmap) // Menampilkan gambar di ImageView
            } else {
                Toast.makeText(holder.itemView.context, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
            }

            buttonUpdate.setOnClickListener {
                val intent = Intent(holder.itemView.context, BannerUpdateActivity::class.java)
                intent.putExtra("itemBanner", itemBanner) // Mengirim data item ke aktivitas Update
                holder.itemView.context.startActivity(intent)
            }

            buttonDeleteData.setOnClickListener {
                val id = itemBanner.idB
                val database = FirebaseDatabase.getInstance().getReference("Banner").child(id)

                database.removeValue().addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()

                    // Menghapus item dari daftar dan memperbarui RecyclerView
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
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
