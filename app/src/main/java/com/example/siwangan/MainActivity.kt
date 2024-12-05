package com.example.siwangan

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.siwangan.Activity.BenefitActivity
import com.example.siwangan.Activity.HomeFragment
import com.example.siwangan.Activity.ProfileFragment
import com.example.siwangan.Activity.UmkmActivity
import com.example.siwangan.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initChipNavigationBar()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle back press
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = supportFragmentManager.findFragmentById(R.id.main_fragment)
                if (fragment is HomeFragment) {
                    finish()
                } else {
                    binding.mainBnv.setItemSelected(R.id.homeFragment, true)
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }


    private fun initChipNavigationBar() {
        // Set initial fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment, HomeFragment())
            .commitAllowingStateLoss()

        // Set initial selection
        binding.mainBnv.setItemSelected(R.id.homeFragment, true)

        // Handle navigation item selection
        binding.mainBnv.setOnItemSelectedListener { itemId ->
            when (itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, HomeFragment())
                        .commitAllowingStateLoss()
                }
                R.id.UmkmFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, UmkmActivity())
                        .commitAllowingStateLoss()
                }
                R.id.BenefitFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, BenefitActivity())
                        .commitAllowingStateLoss()
                }
                R.id.ProfileFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, ProfileFragment())
                        .commitAllowingStateLoss()
                }
            }
        }
    }
}


