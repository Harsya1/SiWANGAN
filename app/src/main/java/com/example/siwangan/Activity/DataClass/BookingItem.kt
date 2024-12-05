package com.example.siwangan.Activity.DataClass

import android.graphics.Bitmap

data class BookingItem(
    val bookingCode: String,
    val itemTitle: String,
    val bookingDate: String,
    val totalPrice: String,
    val status: String,
    val proofImage: Bitmap?, // Include Bitmap for the image
    val userName: String,
    val userPhone: String,
    val quantity: Int,
    val TiketQR: Bitmap?, // Include Bitmap for the QR code
    val TiketQRBase64: String // Include Base64 string for the QR code
)