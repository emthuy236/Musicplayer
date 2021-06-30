package com.ezstudio.playyyyyy.activity

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ezstudio.playyyyyy.boarding.fragments.screen.AlbumFragment
import com.ezstudio.playyyyyy.boarding.fragments.screen.ArtistFragment
import com.ezstudio.playyyyyy.boarding.fragments.screen.TracksFragment
import com.ezstudio.playyyyyy.bottomsheet.HandleBottomSheet
import com.ezstudio.playyyyyy.services.ServiceMedia
import com.ezstudio.playyyyyy.services.UpdateUIMusic
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.layout_bottom_sheet.view.*

open class BaseActivity : AppCompatActivity() {

    private val broadCastUI = UpdateUIMusic()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    open fun getViewBottomSheet(): HandleBottomSheet? {
        return null
    }

    override fun onResume() {
        super.onResume()
        ServiceMedia.instance?.let {
            getViewBottomSheet()?.let { bottom ->
                bottom.updateUITrack(it.lstTrack[it.indexMusic])
            }
        }
    }

    private fun initListener() {
        broadCastUI.listenerUpdate = {
            if (it == null)
                getViewBottomSheet()?.visibility = View.GONE

            it?.let {
                getViewBottomSheet()?.let { bottom ->
                    bottom.updateUITrack(it)

                    if (this is HomeActivity) {
                        this.updateAdapter(it)

                    }
                    if (this is AlbumActivity) {
                        this.updateAdapterListAlbum(it)

                    }
                    if (this is ArtistAcitviy) {
                        this.updateApter(it)

                    }
                }
            }
        }
        val filter = IntentFilter("update.UI.Broadcast")
        registerReceiver(broadCastUI, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadCastUI)
    }


    override fun onBackPressed() {
        if (getViewBottomSheet()!!.handleListBottomSheet.visibility == View.VISIBLE) {
            getViewBottomSheet()!!.handleListBottomSheet.visibility = View.GONE
            getViewBottomSheet()!!.onBackpress()
        } else if (getViewBottomSheet()!!.handleListBottomSheet.visibility == View.GONE) {
            getViewBottomSheet()?.bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        if (getViewBottomSheet()?.bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
            super.onBackPressed()
        }
    }


}