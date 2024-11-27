package com.example.siwangan.Domain

import android.os.Parcel
import android.os.Parcelable

data class itemBanner(
    val idB: String = "",
    val url: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idB)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<itemBanner> {
        override fun createFromParcel(parcel: Parcel): itemBanner {
            return itemBanner(parcel)
        }

        override fun newArray(size: Int): Array<itemBanner?> {
            return arrayOfNulls(size)
        }
    }
}
