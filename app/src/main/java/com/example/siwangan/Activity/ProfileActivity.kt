package com.example.siwangan.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.siwangan.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileIcon: ImageView
    private lateinit var buttonLihatProfile: Button
    private lateinit var buttonGantiPw: Button
    private lateinit var buttonTentangAplikasi: Button
    private lateinit var buttonKeluarAkun: Button // Tombol logout baru

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan layout fragment_profile dengan fragment ini
        val rootView = inflater.inflate(R.layout.activity_profile, container, false)

        // Menghubungkan elemen-elemen UI dengan ID yang ada di layout
        profileName = rootView.findViewById(R.id.profileName)
        profileIcon = rootView.findViewById(R.id.profileIcon)
        buttonLihatProfile = rootView.findViewById(R.id.buttonLihatProfile)
        buttonGantiPw = rootView.findViewById(R.id.buttonGantiPw)
        buttonTentangAplikasi = rootView.findViewById(R.id.buttonTentangAplikasi)
        buttonKeluarAkun =
            rootView.findViewById(R.id.buttonKeluarAkun)  // Menambahkan tombol logout

        // Menambahkan event klik pada button lihat profile
        buttonLihatProfile.setOnClickListener {
            // Membuka EditProfileActivity
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Menambahkan event klik pada button ganti password
        buttonGantiPw.setOnClickListener {
            // Membuka GantiPasswordActivity
            val intent = Intent(requireActivity(), GantiPasswordActivity::class.java)
            startActivity(intent)
        }
        // Menambahkan event klik pada button tentang akun
        buttonTentangAplikasi.setOnClickListener {
            // Membuka EditProfileActivity
            val intent = Intent(requireActivity(), TentangAkunActivity::class.java)
            startActivity(intent)
        }

        // Menambahkan event klik pada button logout
        buttonKeluarAkun.setOnClickListener {
            logout() // Memanggil metode logout saat tombol dipencet
        }

        return rootView
    }

    private fun logout() {
        // Keluar dari Firebase Authentication
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()

        // Jika menggunakan Google Sign-In, keluar juga dari Google (Opsional)
        val mGoogleSignInClient =
            GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN)
        mGoogleSignInClient.signOut()

        // Menghapus data pengguna dari SharedPreferences
        val preferences: SharedPreferences =
            requireActivity().getSharedPreferences("user_pref", AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()  // Menghapus semua data yang disimpan
        editor.apply()  // Menyimpan perubahan

        // Menampilkan Toast sebagai konfirmasi bahwa logout berhasil
        Toast.makeText(requireActivity(), "Logout Berhasil", Toast.LENGTH_SHORT).show()

        // Mengarahkan pengguna ke SplashLoginRegisterActivity (halaman login)
        val intent = Intent(requireActivity(), SplashLoginRegisterActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK  // Menghapus aktivitas sebelumnya dari back stack
        startActivity(intent)

        // Menutup aktivitas saat ini agar pengguna tidak bisa kembali ke halaman profile setelah logout
        requireActivity().finish()
    }
}
