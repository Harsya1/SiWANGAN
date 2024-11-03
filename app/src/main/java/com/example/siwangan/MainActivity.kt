package com.example.siwangan

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.Activity.BenefitActivity
import com.example.siwangan.Activity.ProfileActivity
import com.example.siwangan.Activity.UmkmFragment
import com.example.siwangan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initBottomNavigation()

        // Handle back press
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = supportFragmentManager.findFragmentById(R.id.main_fragment)
                if (fragment is HomeFragment) {
                    finish()
                } else {
                    binding.mainBnv.selectedItemId = R.id.homeFragment
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initBottomNavigation() {
        // Initial fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment, HomeFragment())
            .commitAllowingStateLoss()

        // Set initial selection
        binding.mainBnv.selectedItemId = R.id.homeFragment

        // Handle navigation item selection
        binding.mainBnv.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, HomeFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.UmkmFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, UmkmFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.BenefitFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, BenefitActivity())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.ProfileFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, ProfileActivity())
                        .commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }
        // Disable default icon tinting
        binding.mainBnv.itemIconTintList = null
    }
}


