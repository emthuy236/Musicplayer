package com.ezstudio.playyyyyy.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumModel(
    var albumid:Long = -1,
    var titlealbum: String,
    var artistalbum: String?,
    var albumImage: Uri?,
    var yearRelease:Int,
    var artistId:Long = -1
) : Parcelable
//{
//    constructor(parcel: Parcel) : this(
//        parcel.readInt(),
//        parcel.readString()!!,
//        parcel.readString(),
//        parcel.readParcelable(Uri::class.java.classLoader),
//        parcel.readInt()
//
//
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeInt(albumid)
//        parcel.writeString(titlealbum)
//        parcel.writeString(artistalbum)
//        parcel.writeParcelable(albumImage, flags)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<AlbumModel> {
//        override fun createFromParcel(parcel: Parcel): AlbumModel {
//            return AlbumModel(parcel)
//        }
//
//        override fun newArray(size: Int): Array<AlbumModel?> {
//            return arrayOfNulls(size)
//        }
//    }
//}