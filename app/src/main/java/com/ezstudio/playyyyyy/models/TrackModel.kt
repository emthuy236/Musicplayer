package com.ezstudio.playyyyyy.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TrackModel(
    var path: String?,
    var title: String?,
    var artist: String?,
    var timemuisc:String?,
    var imTrack: Uri?,
    var year: Int,
    var sortMusic: String?,
    var albumid: Long = -1,
    var artistId: Long = -1,
    var size: String?,
    var album: String?,
    var type: String?,
    var duration: Long? = null,
    var yearrelease:Int
) : Parcelable

