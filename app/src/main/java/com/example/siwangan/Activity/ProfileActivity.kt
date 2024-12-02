package com.example.siwangan.Activity

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
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.siwangan.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var profileName: TextView
    private lateinit var profileIcon: ImageView
//    private lateinit var buttonLihatProfile: Button
//    private lateinit var buttonGantiPassword: Button

    private var uri: Uri? = null

//    private lateinit var editPasswordLayout: LinearLayout

    private lateinit var lihatProfile: LinearLayout
    private lateinit var gantiPassword: LinearLayout
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
        tentangAplikasi = rootView.findViewById(R.id.TentangAplikasi)
        keluarAkun = rootView.findViewById(R.id.KeluarAkun)

        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            // Menampilkan data user dari Firebase
            fetchUserData(currentUser.uid)

            // Mengarahkan ke EditProfileActivity saat tombol Lihat Profile dipencet
            lihatProfile.setOnClickListener {
                val intent = Intent(requireActivity(), EditProfileActivity::class.java)
                startActivity(intent)
            }

            gantiPassword.setOnClickListener {
                // Implement your change password functionality here
            }

            tentangAplikasi.setOnClickListener {
                // Implement your about app functionality here
            }

            keluarAkun.setOnClickListener {
                logout()
            }
        } else {
            Log.e("ProfileFragment", "User not logged in!")
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return rootView
    }

    // Fungsi untuk mengambil data user dari Firebase
    private fun fetchUserData(userId: String) {
        mDatabase.child("Users").child(userId)
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

    private fun logout() {
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mGoogleSignInClient.signOut()

        val preferences = requireActivity().getSharedPreferences("user_pref", AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()

        Toast.makeText(requireActivity(), "Logout Berhasil", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireActivity(), SplashLoginRegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}