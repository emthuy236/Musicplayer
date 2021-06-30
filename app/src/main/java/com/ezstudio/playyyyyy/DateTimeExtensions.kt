package com.ezstudio.playyyyyy

import java.util.*
import java.util.concurrent.TimeUnit

fun Date.convertMilitommss(time: Long): String{
    var ms:String = String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(time),
       TimeUnit.MILLISECONDS.toSeconds(time)% TimeUnit.MINUTES.toSeconds(1))
    return ms
}