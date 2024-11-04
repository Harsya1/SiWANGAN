package com.example.siwangan.Domain


import java.io.Serializable

data class ItemDomain(
    val title: String = "",
    val address: String = "",
    val description: String = "",
    val pic: String = "",
    val duration: String = "",
    val timeTour: String = "",
    val dateTour: String = "",
    val tourGuideName: String = "",
    val tourGuidePhone: String = "",
    val tourGuidePic: String = "",
    val price: Int = 0,
    val bed: Int = 0,
    val distance: String = "",
    val score: Double = 0.0
) : Serializable