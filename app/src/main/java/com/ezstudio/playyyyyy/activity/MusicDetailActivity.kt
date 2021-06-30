package com.ezstudio.playyyyyy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ezstudio.playyyyyy.AlbumArt
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.databinding.ActivityMusicDetailBinding
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.utils.Config
import kotlinx.coroutines.launch

class MusicDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMusicDetailBinding
    private var trackModelDetail:TrackModel? = null
    var index = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        binding.imgBackDetailMusic.setOnClickListener {
            onBackPressed()
        }
        getData()
    }

     fun getData() {
         index = intent.getIntExtra(Config.DATA_DETAIL_MUSIC_ACTIVITY,-1).toLong()
         lifecycleScope.launch {
             AlbumArt.getAudioByAlbumId(this@MusicDetailActivity,null,index).let {
                 if (it.isNotEmpty()){
                     trackModelDetail = it[0]
                 }
             }
         }

         trackModelDetail?.let {
             Glide.with(this)
                 .load(it.imTrack)
                 .placeholder(R.drawable.ic_loading)
                 .error(R.drawable.ic_default_music)
                 .into(binding.imgDetailMusic)
             Glide.with(this)
                 .load(it.imTrack)
                 .placeholder(R.drawable.ic_loading)
                 .error(R.drawable.ic_default_music)
                 .into(binding.imgHomeListScreenDetail)
            // binding.imgHomeListScreenDetail.setImageResource(it.imTrack)
             binding.txtTitleDetailMusic.isSelected = true
             binding.txtTitleDetailMusic.text = it.title
             binding.txtAlbumDetailMusic.text = it.album
             binding.txtLengthDetailMusic.text = it.duration.toString()
             binding.txtNumberDetailMusic.text = it.albumid.toString()
             binding.txtTpyeDetailMusic.text = it.type
             binding.txtSizeDetailMusic.text = it.size
             binding.txtPathDetailMusic.text = it.path
         }
    }
}