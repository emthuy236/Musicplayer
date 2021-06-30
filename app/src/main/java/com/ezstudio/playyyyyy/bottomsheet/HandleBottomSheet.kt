package com.ezstudio.playyyyyy.bottomsheet

import android.content.Context
import android.media.AudioManager
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.*

import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.ezstudio.playyyyyy.AlbumArt.Companion.milisecondtoTimer
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.boarding.fragments.screen.TracksFragment
import com.ezstudio.playyyyyy.convertMilitommss
import com.ezstudio.playyyyyy.databinding.FragmentTracksBinding
import com.ezstudio.playyyyyy.databinding.LayoutBottomSheetBinding
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.services.ServiceMedia
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import java.util.*
import kotlin.collections.ArrayList

class HandleBottomSheet(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs),
    View.OnClickListener {

    private lateinit var binding: LayoutBottomSheetBinding
    var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>? = null
    var position = 0
    var check: Boolean = false


    init {
        initView()
        initData()
        updateListTrackBottom()
        initLisener()
        setupSoundMusic()
        showRunMusic()
        updateMusicRandomOnComplete()
        updateLikeMusic()
        clickIconBottom()
        repeatMusic()
    }

    private fun initView() {

        binding = LayoutBottomSheetBinding.inflate(LayoutInflater.from(context), this, true)
        background = resources.getDrawable(android.R.color.transparent)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.frBottom)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.frBottom.visibility = View.GONE
        val headerlayout: ConstraintLayout = findViewById(R.id.header_constraint_layout)
        val contentLayout: ConstraintLayout = findViewById(R.id.constraint_content)
        headerlayout.setOnClickListener {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetBehavior!!.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {


                bottomSheetBehavior!!.isDraggable = true
                headerlayout.alpha = 1 - slideOffset
                contentLayout.alpha = slideOffset + 0.3f
                if (headerlayout.alpha == 0f) {
                    headerlayout.visibility = View.GONE
                    headerlayout.isClickable = false
                } else {
                    headerlayout.visibility = View.VISIBLE
                    headerlayout.isClickable = true
                }
//                if (binding.handleListBottomSheet.visibility == View.VISIBLE){
//                   bottomSheetBehavior!!.isDraggable = false
//                } else if (binding.handleListBottomSheet.visibility == View.GONE){
//                    Log.e("XXXXXX", "onSlide: dong" )
//                    bottomSheetBehavior!!.isDraggable = true
//                    headerlayout.alpha = 1 - slideOffset
//                    contentLayout.alpha = slideOffset + 0.3f
//                    if (headerlayout.alpha == 0f){
//                        headerlayout.visibility = View.GONE
//                        headerlayout.isClickable = false
//                    }else{
//                        headerlayout.visibility = View.VISIBLE
//                        headerlayout.isClickable = true
//                    }
//
//                }

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    Log.e("XXXXXX", "onStateChanged: " + newState)
                    binding.handleListBottomSheet.visibility = View.GONE
                    binding.imgMenuClick.visibility = View.VISIBLE
                    binding.imBlackBackgroud.visibility = View.GONE
                }
                //bottomSheet.isClickable = true
                bottomSheet.setOnClickListener {
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        })


        binding.txtSongBottom.isSelected = true
        binding.txtSongMusicBottomsheet.isSelected = true
        binding.imgDownMusic.setOnClickListener {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
//        binding.imgMenuMusicHeader.setOnClickListener {
//            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
//            binding.handleListBottomSheet.visibility = View.VISIBLE
//            binding.headerConstraintLayout.visibility = View.GONE
//            binding.imgMenuClick.visibility = View.GONE
//            binding.imBlackBackgroud.visibility = View.VISIBLE
//        }


    }

    fun clickIconBottom() {
        binding.imgMenuMusicHeader.setOnClickListener {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            binding.handleListBottomSheet.visibility = View.VISIBLE
            binding.headerConstraintLayout.visibility = View.GONE
            binding.imgMenuClick.visibility = View.GONE
            binding.imBlackBackgroud.visibility = View.VISIBLE


        }

    }

    fun onBackpress() {
        if (binding.handleListBottomSheet.visibility == View.GONE) {
//            binding.imgMenuHeart.visibility = View.VISIBLE
//            binding.imgMenuAdd.visibility = View.VISIBLE
//            binding.imgMenuClick.visibility = View.VISIBLE
            binding.imBlackBackgroud.visibility = View.GONE
        }
    }

    fun initData() {
        binding.handleListBottomSheet.backPressButtonListener = {
            binding.handleListBottomSheet.visibility = View.GONE
//            binding.imgMenuHeart.visibility = View.VISIBLE
//            binding.imgMenuAdd.visibility = View.VISIBLE
            binding.imgMenuClick.visibility = View.VISIBLE
            binding.imBlackBackgroud.visibility = View.GONE
        }
        binding.imgDownMusic.setOnClickListener {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    fun setupSoundMusic() {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        binding.imgSpeakerMusic.setOnClickListener {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
        }
    }

    fun showRunMusic() {
        binding.imgChartMusic.setOnClickListener {
            ServiceMedia.instance?.let {
                if (it.isPlaying()) {
                    Toast.makeText(context, "Music is running", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Music is stopping", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun updateUITrack(trackModel: TrackModel) {
        binding.frBottom.visibility = View.VISIBLE
        visibility = View.VISIBLE
        trackModel.let {
            binding.txtSongBottom.text = it.title
            binding.txtSingBottom.text = it.artist
            binding.txtSongMusicBottomsheet.text = it.title
            binding.txtSingMusicBottomsheet.text = it.artist
            Glide.with(this)
                .load(it.imTrack)
                .error(R.drawable.ic_default_music)
                .into(binding.circleImgBottomsheet)
            Glide.with(this)
                .load(it.imTrack)
                .error(R.drawable.ic_default_music)
                .into(binding.imgArtMusic)
            Glide.with(this)
                .load(it.imTrack)
                .error(R.drawable.ic_default_music)
                .into(binding.imgScreenBottomsheet)
//            binding.circleImgBottomsheet.setImageResource(it.imTrack)
//            binding.imgArtMusic.setImageURI(it.imTrack)
//            binding.imgScreenBottomsheet.setImageURI(it.imTrack)
        }

        ServiceMedia.instance?.let {
            if (it.isPlaying()) {
                binding.imgPlayDetail.setImageResource(R.drawable.ic_play_arrow)
                binding.imgPlayHeader.setImageResource(R.drawable.ic_play_arrow)
            } else {
                binding.imgPlayDetail.setImageResource(R.drawable.ic_paly_detail)
                binding.imgPlayHeader.setImageResource(R.drawable.ic_paly_detail)
            }

            binding.handleListBottomSheet.initData(trackModel)

        }

    }

    private fun updateListTrackBottom() {
        binding.imgMenuClick.setOnClickListener {
            binding.imgMenuHeart.visibility = View.GONE
            binding.imgMenuAdd.visibility = View.GONE
            binding.imgMenuClick.visibility = View.GONE
            binding.handleListBottomSheet.visibility = View.VISIBLE
            binding.imBlackBackgroud.visibility = View.VISIBLE
        }
    }

    private fun initLisener() {
        binding.imgPlayDetail.setOnClickListener(this)
        binding.imgPlayHeader.setOnClickListener(this)
        binding.imgNextHeader.setOnClickListener(this)
        binding.imgNextDetail.setOnClickListener(this)
        binding.imgPrevDetail.setOnClickListener(this)
        binding.imagPrevBottomsheet.setOnClickListener(this)
        HandlepullSeek()
        Handletimeseek()
    }

    private fun Handletimeseek() {
        var countdown: CountDownTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onFinish() {
            }

            override fun onTick(millisUntilFinished: Long) {
                ServiceMedia.instance?.let {
                    //binding.seekbarDetail.max = it.getDuration()
                    binding.txtTimeRun.text = Date().convertMilitommss(it.getCurrentTime().toLong())
                    binding.txtTimeMax.setText(milisecondtoTimer(it.getDuration().toLong()))
                    binding.seekbarDetail.progress = it.getCurrentTime()
                    binding.seekbarDetail.max = it.getDuration()
                    return
                }
                //Log.e("TAG", "onTick: " + minute + " " + minutess)
            }
        }
        countdown.start()
    }

    private fun HandlepullSeek() {
        binding.seekbarDetail.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                ServiceMedia.instance?.let {
                    it.seekTo(seekBar!!.progress)
                }
            }
        })
    }


    override fun onClick(view: View?) {

        view?.let {
            when (view.id) {
                R.id.img_play_header, R.id.img_play_detail -> {
                    val isPlaying = ServiceMedia.instance!!.isPlaying()
                    if (isPlaying) {
                        ServiceMedia.instance!!.pauseAudio()
                    } else {
                        ServiceMedia.instance!!.resumeAudio()
                    }
                }

                R.id.img_next_header, R.id.img_next_detail -> {
                    ServiceMedia.instance?.let {
                        it.nextAudio()
                    }
                }

                R.id.img_prev_detail, R.id.imag_prev_bottomsheet -> {
                    ServiceMedia.instance?.let {
                        it.previousAudio()
                    }
                }
                else -> {

                }
            }
        }
    }

    fun updateMusicRandomOnComplete() {
        binding.imgFollowDetail.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (position == 1) {
                    position = 0
                    binding.imgFollowDetail.setColorFilter(resources.getColor(R.color.white))
                } else {
                    binding.imgFollowDetail.setColorFilter(resources.getColor(R.color.teal_200))
                    ServiceMedia.instance?.let {
                        it.updateMusicOnComplete()
                    }
                    position++
                }

            }

        })
    }

    fun updateLikeMusic() {
        binding.imgMenuHeart.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (position == 1) {
                    position = 0
                    binding.imgMenuHeart.setColorFilter(resources.getColor(R.color.white))
                } else {
                    binding.imgMenuHeart.setColorFilter(resources.getColor(R.color.red))
                    position++
                }
            }

        })
    }

    fun repeatMusic() {
        binding.imgWordDetail.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (position == 1) {
                    position = 0
                    binding.imgWordDetail.setColorFilter(resources.getColor(R.color.white))
                    ServiceMedia.instance?.let {
                        it.onComplete()
                    }
                } else {
                    binding.imgWordDetail.setColorFilter(resources.getColor(R.color.teal_200))
                    ServiceMedia.instance?.let {
                        it.repeatMusic()
                    }
                    position++
                }
            }

        })
    }


}
