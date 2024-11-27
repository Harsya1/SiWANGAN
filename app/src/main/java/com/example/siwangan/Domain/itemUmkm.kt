package com.example.siwangan.Domain

import android.os.Parcel
import android.os.Parcelable

data class itemUmkm(
    val titleumkm: String = "",
    val descriptionumkm: String = "",
    val picumkm: String = "",
    val menu: String = "",
    val contact: String = "",
    val id: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(titleumkm)
        parcel.writeString(descriptionumkm)
        parcel.writeString(picumkm)
        parcel.writeString(menu)
        parcel.writeString(contact)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<itemUmkm> {
        override fun createFromParcel(parcel: Parcel): itemUmkm {
            return itemUmkm(parcel)
        }

        override fun newArray(size: Int): Array<itemUmkm?> {
            return arrayOfNulls(size)
        }
    }
}