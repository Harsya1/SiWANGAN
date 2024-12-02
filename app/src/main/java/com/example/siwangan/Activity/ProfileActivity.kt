package com.example.siwangan.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.siwangan.R

class ProfileFragment : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileIcon: ImageView
    private lateinit var buttonLihatProfile: Button
    private lateinit var buttonGantiPassword: Button

    private var uri: Uri? = null

//    private lateinit var editPasswordLayout: LinearLayout


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
        buttonGantiPassword = rootView.findViewById(R.id.buttonGantiPassword)
//        editPasswordLayout = rootView.findViewById(R.id.editPassword)


        // Menambahkan event klik pada button
        buttonLihatProfile.setOnClickListener {
            // Membuka EditProfileActivity
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        // Menambahkan event klik pada button
        buttonGantiPassword.setOnClickListener {
            // Membuka GantiPasswordActivity
            val intent = Intent(requireActivity(), GantiPasswordActivity::class.java)
            startActivity(intent)
        }


        return rootView
    }
}
