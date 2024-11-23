package com.example.siwangan.Domain


import android.os.Parcel
import android.os.Parcelable

data class Item(
    val url: String = "",
    val description: String = "",
    val pic: String = "",
    val price: String = "",
    val score: Double = 0.0,
    val title: String = "",
    val descriptionumkm: String = "",
    val picumkm: String = "",
    val menu: String = "",
    val titleumkm: String = "",
    val contact: String = "",
    val id: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString() ,
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(description)
        parcel.writeString(pic)
        parcel.writeString(price)
        parcel.writeDouble(score)
        parcel.writeString(title)
        parcel.writeString(descriptionumkm)
        parcel.writeString(picumkm)
        parcel.writeString(menu)
        parcel.writeString(titleumkm)
        parcel.writeString(contact)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}