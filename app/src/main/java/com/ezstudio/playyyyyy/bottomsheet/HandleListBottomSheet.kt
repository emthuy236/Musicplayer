package com.ezstudio.playyyyyy.bottomsheet

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.ezstudio.playyyyyy.AlbumArt
import com.ezstudio.playyyyyy.FunctionState
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.adapter.AdapterListBottomSheet
import com.ezstudio.playyyyyy.databinding.ActivityListOrderBinding
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.services.ServiceMedia
import com.ezstudio.playyyyyy.utils.Config
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HandleListBottomSheet(context: Context?, attrs: AttributeSet) : LinearLayout(context, attrs),RecyclerViewOnClick {
    private lateinit var binding: ActivityListOrderBinding
    private var adapterListBottomSheet: AdapterListBottomSheet? = null
    private var listBottom = ArrayList<TrackModel>()

    var backPressButtonListener: ((Unit) -> Unit)? = null

    init {
        initView()
        getRecyclerview()
        clickMenu()
    }

    private fun initView() {
        binding = ActivityListOrderBinding.inflate(LayoutInflater.from(context), this, true)
        binding.backPress.setOnClickListener {
            backPressButtonListener?.let {
                it(Unit)
            }
        }
    }

    private fun getRecyclerview() {
        binding.recyclerView.setHasFixedSize(true)
        adapterListBottomSheet = AdapterListBottomSheet(context, listBottom,this)
        binding.recyclerView.adapter = adapterListBottomSheet
    }

    fun initData(trackModel: TrackModel) {
        ServiceMedia.instance?.let {
            listBottom.clear()
            listBottom.addAll(it.lstTrack)
            adapterListBottomSheet?.trackModelCurrent = trackModel
            adapterListBottomSheet?.notifyDataSetChanged()
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(context, ServiceMedia::class.java)
        intent.putParcelableArrayListExtra(Config.DATA_RUN_TRACK_SERVICE, listBottom)
        intent.putExtra(Config.DATA_PATH_SERVICE, position)
        ContextCompat.startForegroundService(context, intent)
    }

    override fun onSelectedFun(trackModel: TrackModel, state: FunctionState) {
        TODO("Not yet implemented")
    }

//    override fun onItemClick(position: Int) {
//        val intent = Intent(context, ServiceMedia::class.java)
//        intent.putParcelableArrayListExtra(Config.DATA_RUN_TRACK_SERVICE, listBottom)
//        intent.putExtra(Config.DATA_PATH_SERVICE, position)
//        ContextCompat.startForegroundService(context, intent)
//    }
//
//    override fun onSelectedFun(trackModel: TrackModel, state: FunctionState) {
//        TODO("Not yet implemented")
//    }
    fun clickMenu() {
    binding.imageView9.setOnClickListener {
        val wrapper: Context = ContextThemeWrapper(context, R.style.PopupMenuStyle)
        val popup = PopupMenu(wrapper, binding.imageView9)
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.nametrack -> {
                        listBottom.sortBy {
                            it.title
                        }
                        adapterListBottomSheet!!.notifyDataSetChanged()
                    }
                    R.id.datetrack -> {
                        listBottom.sortBy {
                            it.year
                        }
                        adapterListBottomSheet!!.notifyDataSetChanged()
                    }
                    R.id.artiststrack -> {
                        listBottom.sortBy {
                            it.artist
                        }
                        adapterListBottomSheet!!.notifyDataSetChanged()
                    }
                    else -> return true
                }
                return true
            }

        })
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.custom_menu_track, popup.menu)
        popup.show()
    }
}

}