package com.ezstudio.playyyyyy.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ezstudio.playyyyyy.AlbumArt
import com.ezstudio.playyyyyy.AlbumArt.Companion.milisecondtoTimer
import com.ezstudio.playyyyyy.FunctionState
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.adapter.ListAlbumAdapter
import com.ezstudio.playyyyyy.bottomsheet.HandleBottomSheet
import com.ezstudio.playyyyyy.databinding.ActivityAlbumBinding
import com.ezstudio.playyyyyy.models.AlbumModel
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.services.ServiceMedia
import com.ezstudio.playyyyyy.utils.Config
import kotlinx.coroutines.launch

class AlbumActivity : BaseActivity(), RecyclerViewOnClick {
    private lateinit var binding: ActivityAlbumBinding
    private var albumModel: AlbumModel? = null

    //private lateinit var albumTrack: TrackModel
    private var trackAlbumAdapter: ListAlbumAdapter? = null
    private lateinit var listener: (TrackModel) -> Unit
    var albumId:Long = -1
    var position = 0
    var timeMusic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        initView()
        getData()
        eventRunMusic()
        onClickRandom()



    }

    override fun getViewBottomSheet(): HandleBottomSheet? {
        return binding.bottomSheet
    }

    private fun initView() {
        trackAlbumAdapter = ListAlbumAdapter(arrayListOf(), this)
        binding.recyclelistalbum.adapter = trackAlbumAdapter
    }

    fun updateAdapterListAlbum(trackModel: TrackModel) {
        trackAlbumAdapter?.let {
            it.trackmodelCurrent = trackModel
            it.notifyDataSetChanged()
        }
    }


    @SuppressLint("SetTextI18n")
    fun getData() {
        albumId = intent.getLongExtra(Config.DATA_ALBUM_DETAIL, 0)


        AlbumArt.getAllAlbum(this, null, albumId).let {
            if (it.isNotEmpty()) {
                albumModel = it[0]
            }
        }

        albumModel?.let {
            lifecycleScope.launch {
                trackAlbumAdapter?.let { track ->

                    track.lstData.addAll(
                        AlbumArt.getAudioByAlbumId(
                            this@AlbumActivity,
                            it.albumid,
                            null
                        )
                    )
                    var totalTime = 0L
                    for (item in track.lstData) {
                        item.duration?.let { duration ->
                            totalTime += duration
                        }
                        binding.txttimealbum.text = milisecondtoTimer(totalTime)
                        track.notifyDataSetChanged()

                    }
                }
            }

            Glide.with(this)
                .load(it.albumImage)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_music)
                .into(binding.imgmusicAlbum)

            binding.txttitlealbum.text = it.titlealbum
            binding.txtartistalbum.text = it.artistalbum
            binding.recyclelistalbum.setHasFixedSize(true)

            Glide.with(this)
                .load(it.albumImage)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_music)
                .into(binding.imgHomeListScreen)

        }
        binding.imgbackalbum.setOnClickListener {
            onBackPressed()
        }

    }
    fun eventRunMusic(){
        binding.imageView12.setOnClickListener {
            ServiceMedia.instance?.let {
                if (it.isPlaying()){
                    it.pauseAudio()
                }else {
                    it.resumeAudio()
                }
            }
        }


    }
    fun onClickRandom(){
        binding.imageView11.setOnClickListener {
            ServiceMedia.instance?.let {
                it.randomMusic()
            }

        }
    }


    override fun onItemClick(position: Int) {
        val intent = Intent(applicationContext, ServiceMedia::class.java)
        intent.putParcelableArrayListExtra(
            Config.DATA_RUN_TRACK_SERVICE,
            trackAlbumAdapter?.lstData
        )
        intent.putExtra(Config.DATA_PATH_SERVICE, position)
        startForegroundService(intent)
    }

    override fun onSelectedFun(trackModel: TrackModel, state: FunctionState) {
        TODO("Not yet implemented")
    }

//    fun updateBottomUI(listener: (TrackModel) -> Unit) {
//        this.listener = listener
//    }


}