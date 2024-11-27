package com.example.siwangan.Domain

import android.os.Parcel
import android.os.Parcelable

data class itemLayanan(
    val description: String = "",
    val pic: String = "",
    val price: String = "",
    val score: Double = 0.0,
    val title: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(pic)
        parcel.writeString(price)
        parcel.writeDouble(score)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<itemLayanan> {
        override fun createFromParcel(parcel: Parcel): itemLayanan {
            return itemLayanan(parcel)
        }

        override fun newArray(size: Int): Array<itemLayanan?> {
            return arrayOfNulls(size)
        }
    }
}