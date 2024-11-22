package com.example.siwangan

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.siwangan.Activity.BenefitActivity
import com.example.siwangan.Activity.HomeFragment
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

        initChipNavigationBar()

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
                        .replace(R.id.main_fragment, UmkmFragment())
                        .commitAllowingStateLoss()
                }
                R.id.BenefitFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, BenefitActivity())
                        .commitAllowingStateLoss()
                }
                R.id.ProfileFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, ProfileActivity())
                        .commitAllowingStateLoss()
                }
            }
        }
    }
//
//    private fun initBottomNavigation() {
//        // Initial fragment
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main_fragment, HomeFragment())
//            .commitAllowingStateLoss()
//
//        // Set initial selection
//        binding.mainBnv.selectedItemId = R.id.homeFragment
//
//        // Handle navigation item selection
//        binding.mainBnv.setOnItemSelectedListener { menuItem ->
//            // Add animation to the menu item
//            menuItem.icon?.let { drawable ->
//                drawable.setVisible(false, true)
//                drawable.setVisible(true, true)
//                val animation = AnimationUtils.loadAnimation(this, R.anim.fade_transition)
//                binding.mainBnv.findViewById<View>(menuItem.itemId)?.startAnimation(animation)
//            }
//            when (menuItem.itemId) {
//                R.id.homeFragment -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_fragment, HomeFragment())
//                        .commitAllowingStateLoss()
//                    true
//                }
//                R.id.UmkmFragment -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_fragment, UmkmFragment())
//                        .commitAllowingStateLoss()
//                    true
//                }
//                R.id.BenefitFragment -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_fragment, BenefitActivity())
//                        .commitAllowingStateLoss()
//                    true
//                }
//                R.id.ProfileFragment -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.main_fragment, ProfileActivity())
//                        .commitAllowingStateLoss()
//                    true
//                }
//                else -> false
//            }
//        }
//        // Disable default icon tinting
//        binding.mainBnv.itemIconTintList = null
//    }
}


