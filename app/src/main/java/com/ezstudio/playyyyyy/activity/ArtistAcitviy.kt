package com.ezstudio.playyyyyy.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.ezstudio.playyyyyy.AlbumArt
import com.ezstudio.playyyyyy.R

import com.ezstudio.playyyyyy.boarding.fragments.screen2.ListAlbumAritstFragment
import com.ezstudio.playyyyyy.boarding.fragments.screen2.ListTrackArtistFragment
import com.ezstudio.playyyyyy.boarding.fragments.ViewPagerArtist
import com.ezstudio.playyyyyy.bottomsheet.HandleBottomSheet
import com.ezstudio.playyyyyy.databinding.ActivityArtistAcitviyBinding
import com.ezstudio.playyyyyy.models.AristModel
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.utils.Config
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ArtistAcitviy : BaseActivity() {
    private lateinit var binding: ActivityArtistAcitviyBinding
    private  var aristModel: AristModel? = null
    private var listTrackArtistFragment: ListTrackArtistFragment? = null
    private var listAlbumAritstFragment:ListAlbumAritstFragment? = null
    var artistId:Long = -1
    var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistAcitviyBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        getData()
        getFragment()
        binding.imgBackPressArtist.setOnClickListener {
            onBackPressed()
        }

    }

    override fun getViewBottomSheet(): HandleBottomSheet? {
        return binding.bottomSheet
    }

    fun updateApter(trackModel: TrackModel) {
        listTrackArtistFragment?.let {
            it.updateAdapterTrackArtist(trackModel)
        }
    }

    fun getFragment() {
        val adapter = ViewPagerArtist(supportFragmentManager, lifecycle)
        listTrackArtistFragment = ListTrackArtistFragment.getInstance(
            aristModel?.idArtist
            )
        listTrackArtistFragment?.let {
            adapter.addFragmentInViewPager(
                it
            )
        }
        listAlbumAritstFragment = ListAlbumAritstFragment.getInstance(aristModel?.idArtist)
        listAlbumAritstFragment?.let {
            adapter.addFragmentInViewPager(it)
        }


        val tablayout: TabLayout = findViewById(R.id.tablayoutArtist)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager2_artist)
        viewPager2.adapter = adapter
        for (i in 0 until tablayout.tabCount) {
            val tab = (tablayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(60, 0, 60, 0)
            tab.requestLayout()
        }

        listTrackArtistFragment?.let {
            it.countTrackListener = {count ->
           TabLayoutMediator(tablayout, viewPager2) { tab, position ->
               when (position){
                   0 -> {
                       tab.setText(getResources().getString(R.string.song) + "(" + count + ")")
                   }
                   1 -> {
                       tab.setText(getResources().getString(R.string.album) + "(" + count + ")")
                   }
               }
           }.attach()
        }
        }
    }

    fun getData() {
        artistId = intent.getLongExtra(Config.DATA_ARTIST_DETAIL,-1)
        AlbumArt.getAllArtist(this,artistId.toLong()).let {
            if (it.isNotEmpty()){
                aristModel = it[0]
            }
        }
        aristModel?.let {
            binding.tvNameArtist.text = it.titleArtist

        }

        binding.tvNameArtist.isSelected = true
    }

}