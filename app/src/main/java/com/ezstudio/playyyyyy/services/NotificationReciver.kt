package com.ezstudio.playyyyyy.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReciver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val intentService = Intent(
            context?.applicationContext,
            ServiceMedia::class.java
        )
        if (intent?.action != null) {
            context?.let {
                intentService.action = intent.action
                it.startService(intentService)
            }
        }
    }
}