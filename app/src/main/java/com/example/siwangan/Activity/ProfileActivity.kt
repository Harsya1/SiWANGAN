package com.example.siwangan.Activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.siwangan.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ProfileActivity : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var profileName: TextView
    private lateinit var profileIcon: ImageView

    private var uri: Uri? = null

    private lateinit var lihatProfile: LinearLayout
    private lateinit var gantiPassword: LinearLayout
    private lateinit var tiketPesanan: LinearLayout
    private lateinit var riwayatPemesanan: LinearLayout
    private lateinit var tentangAplikasi: LinearLayout
    private lateinit var keluarAkun: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_profile, container, false)

        // Inisialisasi Firebase Auth dan Database Reference
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        // Inisialisasi View
        profileName = rootView.findViewById(R.id.profileName)
        profileIcon = rootView.findViewById(R.id.profileIcon)
        lihatProfile = rootView.findViewById(R.id.LihatProfile)
        gantiPassword = rootView.findViewById(R.id.GantiPassword)
        tiketPesanan = rootView.findViewById(R.id.TiketPesanan)
        riwayatPemesanan = rootView.findViewById(R.id.RiwayatPemesanan)
        tentangAplikasi = rootView.findViewById(R.id.TentangAplikasi)
        keluarAkun = rootView.findViewById(R.id.KeluarAkun)

        // Menampilkan data user dari Firebase
        fetchUserData(mAuth.currentUser?.uid)

        // Mengarahkan ke EditProfileActivity saat tombol Lihat Profile dipencet
        lihatProfile.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        gantiPassword.setOnClickListener {
            val intent = Intent(requireActivity(), GantiPasswordActivity::class.java)
            startActivity(intent)
        }


        tiketPesanan.setOnClickListener {
            val intent = Intent(requireActivity(), TiketActivity::class.java)
            intent.putExtra("profileName", profileName.text.toString())
            startActivity(intent)
        }

        riwayatPemesanan.setOnClickListener {
            val intent = Intent(requireActivity(), RiwayatPemesananActivity::class.java)
            intent.putExtra("profileName", profileName.text.toString())
            startActivity(intent)
        }

        tentangAplikasi.setOnClickListener {
            val intent = Intent(requireActivity(), TentangAkunActivity::class.java)
            startActivity(intent)
        }

        keluarAkun.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Keluar")
                .setMessage("Apakah anda yakin untuk keluar?")
                .setPositiveButton("Ya") { dialog, _ ->
                    val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                    mGoogleSignInClient.signOut()
                    editor.clear()
                    editor.apply()
                    mAuth.signOut()
                    val intent = Intent(requireContext(), SplashLoginRegisterActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    dialog.dismiss()
                }
                .setNegativeButton("Tidak", null)
                .create()
                .show()
        }

        return rootView
    }

    // Fungsi untuk mengambil data user dari Firebase
    private fun fetchUserData(userId: String?) {
        userId?.let {
            mDatabase.child("Users").child(it)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Ambil data dari database
                            val namaLengkap = dataSnapshot.child("name").getValue(String::class.java)
                            val profilePicture = dataSnapshot.child("profile_picture").getValue(String::class.java)

                            // Set profile name
                            profileName.text = namaLengkap ?: "Nama Profile"

                            // Menampilkan gambar profil jika ada
                            if (profilePicture != null) {
                                val imageBytes = Base64.decode(profilePicture, Base64.DEFAULT)
                                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                profileIcon.setImageBitmap(decodedImage)
                            }
                        } else {
                            Log.e("ProfileFragment", "Data snapshot does not exist!")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("ProfileFragment", "Database error: ${databaseError.message}")
                    }
                })
        }
    }
}