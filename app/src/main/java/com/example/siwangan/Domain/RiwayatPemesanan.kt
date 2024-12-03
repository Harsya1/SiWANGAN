package com.example.siwangan.Domain

import android.os.Parcel
import android.os.Parcelable

data class RiwayatPemesanan(
    val bookingId: String = "",
    val userName: String = "",
    val totalPrice: String = "",
    val visitorCount: String = "",
    val status : String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bookingId)
        parcel.writeString(userName)
        parcel.writeString(totalPrice)
        parcel.writeString(visitorCount)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RiwayatPemesanan> {
        override fun createFromParcel(parcel: Parcel): RiwayatPemesanan {
            return RiwayatPemesanan(parcel)
        }

        override fun newArray(size: Int): Array<RiwayatPemesanan?> {
            return arrayOfNulls(size)
        }
    }
}