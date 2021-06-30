package com.ezstudio.playyyyyy.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
@Parcelize
data class AristModel(
    var idArtist:Long,
    var imArtist:String?,
    var titleArtist:String?,
    var numberAlbum:Int,
    var numberTrack:Int
):Parcelable {

}