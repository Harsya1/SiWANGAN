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
        expandableListView.setOnGroupExpandListener {
            setExpandableListViewHeight(expandableListView)
        }

        expandableListView.setOnGroupCollapseListener {
            setExpandableListViewHeight(expandableListView)
        }

        // Panggil ini setelah adapter diatur
        setExpandableListViewHeight(expandableListView)

        return view
    }

    fun setExpandableListViewHeight(listView: ExpandableListView) {
        val adapter = listView.expandableListAdapter ?: return

        var totalHeight = 0
        for (i in 0 until adapter.groupCount) {
            val groupItem = adapter.getGroupView(i, false, null, listView)
            groupItem.measure(
                View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            totalHeight += groupItem.measuredHeight

            if (listView.isGroupExpanded(i)) {
                for (j in 0 until adapter.getChildrenCount(i)) {
                    val childItem = adapter.getChildView(i, j, false, null, listView)
                    childItem.measure(
                        View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    )
                    totalHeight += childItem.measuredHeight
                }
            }
        }

        val layoutParams = listView.layoutParams
        layoutParams.height = totalHeight + (listView.dividerHeight * (adapter.groupCount - 1))
        listView.layoutParams = layoutParams
        listView.requestLayout()
    }



    // Menyiapkan data untuk ExpandableListView
    private fun prepareListData() {
        listDataHeader = mutableListOf()
        listDataChild = HashMap()

        // Header
        listDataHeader.add("Tata Tertib Yang Harus Dipahami")
        listDataHeader.add("Manfaat Fisik Dan Kesehatan")
        listDataHeader.add("Manfaat Sosial dan Rekreasi")
        listDataHeader.add("Tata Cara Booking yang Benar")
//        listDataHeader.add("Cara menjadi mitra yang baik, bertanggung jawab")

        // Child data
        val panduan1 = listOf(
            "Kedalaman Kolam Berendam ± 1 Meter.",
            "Pengunjung yang memiliki riwayat penyakit Jantung,\n Asma, Ayan atau Penyakit yang beresiko tinggi dilarang berendam.",
            "Anak-anak dan Manula harap didampingi pihak keluarga.",
            "Dilarang membawa Senjata Tajam, Senjata Api, Minuman Keras dan Narkotika.",
            "Dilarang memakai Shampoo dan Sabun dikolam rendam.",
            "Dilarang Memotret / Merekam Gambar yang dapat merugikan dan menyinggung pengunjung lain.",
            "Berpakaian sopan dengan Minimal memakai Celana Pendek.",
            "Kehilangan atau Kerusakan Barang bawaan menjadi tanggung jawab Pengunjung.",
            "Dilarang membawa makanan Basah / Berminyak dari luar.",
            "Harap Mematuhi himbauan diatas diluar itu pihak Pengelola / Bumdes Tidak Bertanggung Jawab."
        )

        val panduan2 = listOf(
            "Mengurangi Nyeri Otot dan Sendi\n" +
                    "Berendam di air panas membantu meredakan pegal-pegal di otot dan sendi. Air ini juga cocok untuk penderita rematik karena mengandung mineral alami seperti sulfur yang baik untuk tubuh.",
            "Melancarkan Peredaran Darah\n" +
                    "Air panas dapat memperlebar pembuluh darah sehingga aliran darah menjadi lebih lancar. Hasilnya, tubuh terasa lebih segar dan bertenaga.",
            "Membantu Detoksifikasi Tubuh\n" +
                    "Suhu hangat dari air memicu keringat, yang membantu membuang racun dan zat berbahaya dari dalam tubuh secara alami.",
            "Menghilangkan Stres dan Menenangkan Pikiran\n" +
                    "Berendam di pemandian air panas memberikan efek relaksasi yang luar biasa. Pikiran jadi lebih rileks, stres berkurang, dan tidur malam Anda bisa jadi lebih nyenyak.",
            "Membuat Kulit Lebih Sehat\n" +
                    "Mineral alami seperti sulfur di air panas membantu merawat kulit. Masalah kulit seperti jerawat atau eksim dapat berkurang setelah rutin berendam di sini."
        )

        val panduan3 = listOf(
            "Tempat Seru untuk Keluarga\n" +
                    "Pemandian ini cocok untuk kumpul keluarga. Anda bisa menikmati waktu berkualitas bersama sambil relaksasi.",
            "Terapi Alami Tanpa Biaya Mahal\n" +
                    "Tidak perlu pergi ke spa mahal, karena di sini Anda sudah bisa mendapatkan terapi alami untuk tubuh Anda."
        )

        val panduan4 = listOf(
            "Lengkapi data yang sebenarnya dan valid di profile anda.",
            "Pilih Layanan yang ingin anda pesan",
            "Pilih tanggal kunjungan anda dan seberapa banyak tiket yang anda butuhkan, Max 10 tiket",
            "Klik tombol Pesan untuk diarahkan ke Form Upload Bukti Transfer",
            "Kirim transfer"
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