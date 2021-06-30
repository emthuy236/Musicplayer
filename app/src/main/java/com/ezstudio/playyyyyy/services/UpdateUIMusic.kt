package com.ezstudio.playyyyyy.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.utils.Config

class UpdateUIMusic : BroadcastReceiver() {

    var listenerUpdate: ((TrackModel?) -> Unit)? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val trackModel = it.getParcelableExtra<TrackModel>(Config.DATA_UI_MUSIC_BROADCAST)
            listenerUpdate?.let {
                it(trackModel)
            }
        }
    }
}