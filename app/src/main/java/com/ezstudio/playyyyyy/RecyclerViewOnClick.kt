package com.ezstudio.playyyyyy

import com.ezstudio.playyyyyy.models.TrackModel
import java.text.FieldPosition

enum class FunctionState {
    SHARE, DETAILS, ALBUM, ARTIST
}

interface RecyclerViewOnClick {
    fun onItemClick(position: Int)
   // fun onLongItemClick(position: Int)
    fun onSelectedFun(trackModel: TrackModel, state: FunctionState)
}