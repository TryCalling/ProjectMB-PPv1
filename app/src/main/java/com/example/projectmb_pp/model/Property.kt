package com.example.projectmb_pp.model

import android.os.Parcel
import android.os.Parcelable

data class Property(
    val id: Int,
    val title: String,
    val release_date: String,
    val duration: String,
    val synopsis: String,
    val genre_id: Int,
    val poster_url: String,
    val created_at: String,
    val updated_at: String,
    val movie_url: String,
    val genre: Genre,
    var likeCount: Int = 0,
    var isLiked: Boolean = false,
    var isLoading: Boolean = false

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString()?:"",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Genre::class.java.classLoader) ?: Genre(0, ""),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(release_date)
        dest.writeString(duration)
        dest.writeString(synopsis)
        dest.writeInt(genre_id)
        dest.writeString(poster_url)
        dest.writeString(created_at)
        dest.writeString(updated_at)
        dest.writeString(movie_url)
        dest.writeInt(likeCount)
        dest.writeByte(if (isLiked) 1 else 0)
        dest.writeByte(if (isLoading) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<Property> {
        override fun createFromParcel(parcel: Parcel): Property {
            return Property(parcel)
        }

        override fun newArray(size: Int): Array<Property?> {
            return arrayOfNulls(size)
        }
    }
}
data class Genre(
    val id: Int,
    val name: String

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
    }

    companion object CREATOR : Parcelable.Creator<Genre> {
        override fun createFromParcel(parcel: Parcel): Genre {
            return Genre(parcel)
        }

        override fun newArray(size: Int): Array<Genre?> {
            return arrayOfNulls(size)
        }
    }

}

