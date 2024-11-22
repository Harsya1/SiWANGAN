package com.example.siwangan.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.example.siwangan.PanduanExpandableListAdapter
import com.example.siwangan.R

class BenefitActivity : Fragment() {
    private lateinit var expandableListView: ExpandableListView
    private lateinit var adapter: PanduanExpandableListAdapter
    private lateinit var listDataHeader: MutableList<String>
    private lateinit var listDataChild: HashMap<String, List<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_benefit, container, false)

        // Inisialisasi ExpandableListView
        expandableListView = view.findViewById(R.id.ExpandableListView)

        // Persiapan data
        prepareListData()

        // Validasi data
        if (listDataHeader.isEmpty() || listDataChild.isEmpty()) {
            throw IllegalStateException("Data tidak ditemukan! Periksa prepareListData().")
        }

        // Inisialisasi adapter
        adapter = PanduanExpandableListAdapter(requireContext(), listDataHeader, listDataChild)
        expandableListView.setAdapter(adapter)

        // Listener untuk perubahan status grup
        expandableListView.setOnGroupExpandListener { groupPosition ->
            if (adapter.isGroupExpanded(groupPosition)) return@setOnGroupExpandListener
            adapter.notifyDataSetChanged()
        }

        expandableListView.setOnGroupCollapseListener { groupPosition ->
            adapter.notifyDataSetChanged()
        }

        return view
    }


    // Menyiapkan data untuk ExpandableListView
    private fun prepareListData() {
        listDataHeader = mutableListOf()
        listDataChild = HashMap()

        // Header
        listDataHeader.add("Mendaftarkan diri sebagai mitra thinkwood")
        listDataHeader.add("Mengunggah foto terkait foto KTP, Lahan, dsb")
        listDataHeader.add("Edit profile gagal tersimpan")
        listDataHeader.add("Cara menjadi mitra yang baik, bertanggung jawab")

        // Child data
        val panduan1 = listOf(
            "Setelah berhasil masuk, pilih menu registrasi di halaman beranda",
            "Kemudian ada 3 step yang perlu dilakukan, isi data formulir, unggah foto KTP dan foto lahan, lalu konfirmasi kembali data yang sudah diinputkan tadi agar tidak terjadi kesalahan dan kekeliruan",
            "Jika ada kesalahan sistem, laporkan perihal ini kepada kami"
        )

        val panduan2 = listOf(
            "Jika ingin mengunggah foto, usahakan file berukuran max. 100mb"
        )

        val panduan3 = listOf(
            "Cek Koneksi Internet, dikarenakan proses mengedit membutuhkan koneksi stabil"
        )

        val panduan4 = listOf(
            "Komitmen adalah sebuah langkah awal untuk menjalin hubungan baik dan tanggung jawab.",
            "Pastikan komunikasi dengan mitra berjalan secara terbuka dan jujur. Beri tahu informasi penting secara tepat waktu, termasuk hambatan atau masalah yang mungkin timbul.",
            "Kepercayaan adalah dasar dari setiap hubungan kemitraan yang sukses. Bersikaplah jujur, terbuka, dan berintegritas dalam setiap tindakan dan keputusan. Hindari perilaku yang dapat merusak kepercayaan.",
            "Hargai kontribusi mitra, bahkan untuk hal-hal kecil. Menunjukkan rasa terima kasih dan apresiasi atas usaha mitra membuat hubungan kemitraan semakin kuat."
        )

        listDataChild[listDataHeader[0]] = panduan1
        listDataChild[listDataHeader[1]] = panduan2
        listDataChild[listDataHeader[2]] = panduan3
        listDataChild[listDataHeader[3]] = panduan4

        // Debug log
        for (header in listDataHeader) {
            val children = listDataChild[header]
            println("Header: $header, Children: $children")
        }
    }

}