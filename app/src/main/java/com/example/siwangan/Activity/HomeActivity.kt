package com.example.siwangan.Activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.siwangan.R
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback


class HomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorsContainer: LinearLayout
    private val sliderImages = listOf(
        R.drawable.ic_launcher_background, // Replace with your actual image resources
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background
    )
    private var indicators = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // Initialize ViewPager and Indicators
        setupSliderAndIndicators()

        // Add null check for the view
        findViewById<View>(R.id.main)?.let { view ->
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }

    private fun setupSliderAndIndicators() {
        // Initialize ViewPager with the correct ID
        viewPager = findViewById(R.id.viewPager)  // Changed from sliderContainer_card to viewPager
        indicatorsContainer = findViewById(R.id.sliderIndicators)

        // Set up ViewPager adapter
        val adapter = ImageSliderAdapter(sliderImages)
        viewPager.adapter = adapter

        // Set up indicators
        setupIndicators()
        setCurrentIndicator(0)

        // Set up ViewPager change listener
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentIndicator(position)
            }
        })
    }


    private fun setupIndicators() {
        indicators.clear()
        indicatorsContainer.removeAllViews()

        for (i in sliderImages.indices) {
            val indicator = ImageView(this).apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.indicator_inactive))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0)
                }
            }
            indicatorsContainer.addView(indicator)
            indicators.add(indicator)
        }
    }

    private fun setCurrentIndicator(position: Int) {
        indicators.forEachIndexed { index, imageView ->
            if (index == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_active))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicator_inactive))
            }
        }
    }
}

// Add this adapter class at the bottom of the file or in a separate file
private class ImageSliderAdapter(private val images: List<Int>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount() = images.size

    class ImageViewHolder(val imageView: ImageView) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(imageView)
}
