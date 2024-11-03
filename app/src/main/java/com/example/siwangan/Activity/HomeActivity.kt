package com.example.siwangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.siwangan.R
import androidx.recyclerview.widget.RecyclerView
import com.example.siwangan.Activity.SpotDescriptionFragment

class HomeFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorsContainer: LinearLayout
    private val sliderImages = listOf(
        R.drawable.ic_home_promotion,
        R.drawable.ic_home_prromotion2,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_background
    )
    private var indicators = mutableListOf<ImageView>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSliderAndIndicators(view)

        // Find the recommendation container
        val recommendationContainer = view.findViewById<LinearLayout>(R.id.recommendationContainer)

        // Set click listeners for each recommendation item
        for (i in 0 until recommendationContainer.childCount) {
            val recommendationItem = recommendationContainer.getChildAt(i)
            recommendationItem.setOnClickListener {
                val spotDescriptionFragment = SpotDescriptionFragment()
                val bundle = Bundle()
                bundle.putString("spot_id", "your_spot_id")
                // Add any other data you need to pass
                spotDescriptionFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, spotDescriptionFragment)
                    .addToBackStack(null)
                    .commit()
            }

        }
    }


    private fun setupSliderAndIndicators(view: View) {
        // Initialize ViewPager with the correct ID
        viewPager = view.findViewById(R.id.viewPager)
        indicatorsContainer = view.findViewById(R.id.sliderIndicators)

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
            val indicator = ImageView(requireContext()).apply {
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
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.indicator_active))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.indicator_inactive))
            }
        }
    }
}

// Image Slider Adapter (can be moved to a separate file)
private class ImageSliderAdapter(private val images: List<Int>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

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
        RecyclerView.ViewHolder(imageView)
}
