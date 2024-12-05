package com.example.siwangan.Domain

import android.os.Parcel
import android.os.Parcelable

data class itemUser(
    val email: String = "",
    val name: String = "",
    val phone: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeString(phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<itemUser> {
        override fun createFromParcel(parcel: Parcel): itemUser {
            return itemUser(parcel)
        }

        override fun newArray(size: Int): Array<itemUser?> {
            return arrayOfNulls(size)
        }
    }
}
