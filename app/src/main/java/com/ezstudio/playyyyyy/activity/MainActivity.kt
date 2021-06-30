package com.ezstudio.playyyyyy.activity

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.ezstudio.playyyyyy.R

import com.ezstudio.playyyyyy.services.ServiceMedia
import com.ezstudio.playyyyyy.convertMilitommss
import com.ezstudio.playyyyyy.databinding.ActivityMainBinding
import com.ezstudio.playyyyyy.models.FileName
import com.ezstudio.playyyyyy.models.MusicFiles
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.utils.Config
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

open class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var arrayTrack:ArrayList<TrackModel>? = ArrayList()
    var trackPlaying = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
       // getintent()
       // initListener()
       // Handletimeseek()

//        arraysong.add(FileName("Chắc gì anh yêu cô ấy",R.drawable.chacgianh,R.raw.chacgianhyeucoay,"Hương Ly"))
//        arraysong.add(FileName("Em say rồi",R.drawable.emsayroi,R.raw.emsayroithuongvo,"Thương Võ"))
//        arraysong.add(FileName("Hoa nở không màu",R.drawable.hoanokomau,R.raw.hoanokomau,"Hoài Lâm"))
//        arraysong.add(FileName("Nàng thơ",R.drawable.nangtho,R.raw.nangtho,"Hoàng Dũng"))
//       // mediaPlayer = MediaPlayer.create(this,arraysong.get(index).playnhac)

    }
//    fun getintent(){
//        arrayTrack = intent.getParcelableArrayListExtra(Config.DATA_TRACK_DETAIL)
//        trackPlaying = intent.getIntExtra(Config.DATA_TRACK_PLAYING, -1)
//        arrayTrack?.let {
//            Glide.with(this)
//                .load(arrayTrack!!.get(trackPlaying).imTrack)
//                .placeholder(R.drawable.ic_loading)
//                .error(R.drawable.ic_music_default)
//                .into(binding.circleImageView)
//            binding.txtnamsong.text = arrayTrack!!.get(trackPlaying).title
//        }
//        binding.imageViewplay.setImageResource(R.drawable.ic_pause)
//    }

//    fun initListener(){
//        Playbtn()
//        Nextbtn()
//        Prevbtn()
//        HandlepullSeek()
//    }

    fun Playbtn() {
        binding.imageViewplay.setOnClickListener {
            if (ServiceMedia.instance == null){

                val intent = Intent(applicationContext, ServiceMedia::class.java)
                intent.putExtra(Config.DATA_MAIN_GSON,arrayTrack)
                intent.putExtra(Config.DATA_PATH_SERVICE,trackPlaying)
                startForegroundService(intent)
                binding.imageViewplay.setImageResource(R.drawable.ic_pause)
                //Songsname(0)
            }else{
                var isPlaying = ServiceMedia.instance!!.isPlaying()
                if (isPlaying) {
                    ServiceMedia.instance!!.pauseAudio()
                    binding.imageViewplay.setImageResource(R.drawable.ic_play_arrow)
                }else{
                    ServiceMedia.instance!!.resumeAudio()
                   // HandlepullSeek()
                    binding.imageViewplay.setImageResource(R.drawable.ic_pause)
                }
            }

        }
    }


    fun Nextbtn() {
        binding.imageViewnext.setOnClickListener {
            ServiceMedia.instance?.let {
                it.nextAudio()
                arrayTrack?.let {
                    Glide.with(this)
                        .load(arrayTrack!!.get(trackPlaying).imTrack)
                        .placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_music_default)
                        .into(binding.circleImageView)
                    binding.txtnamsong.text = arrayTrack!!.get(trackPlaying).title
                }

            }
        }
    }

    fun Prevbtn() {
        binding.imageViewprev.setOnClickListener {
            ServiceMedia.instance?.let {
                it.previousAudio()
            }
        }
    }

//    fun Songsname(index: Int) {
//        if (index == 0) {
//            binding.txtnamsong.text = "Chắc gì anh yêu cô ấy"
//            binding.circleImageView.setImageResource(R.drawable.chacgianh)
//        }
//        if (index == 1) {
//            binding.txtnamsong.text = "Em say rồi"
//            binding.circleImageView.setImageResource(R.drawable.emsayroi)
//        }
//        if (index == 2) {
//            binding.txtnamsong.text = "Hoa nở không màu"
//            binding.circleImageView.setImageResource(R.drawable.hoanokomau)
//        }
//        if (index == 3) {
//            binding.txtnamsong.text = "Nàng thơ"
//            binding.circleImageView.setImageResource(R.drawable.nangtho)
//        }
//    }

//    fun Handletimeseek() {
//        var countdown: CountDownTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
//            override fun onFinish() {
//            }
//            override fun onTick(millisUntilFinished: Long) {
//                ServiceMedia.instance?.let {
//                    binding.seekBar.max = it.getDuration()
//                    binding.textViewtime.text = Date().convertMilitommss(it.getCurrentTime().toLong())
//                    binding.textViewmaxtime.setText(milisecondtoTimer(it.getDuration().toLong()))
//                    binding.seekBar.progress = it.getCurrentTime()
//                    if (it.isPlaying())
//                        binding.imageViewplay.setImageResource(R.drawable.ic_pause)
//                    else
//                        binding.imageViewplay.setImageResource(R.drawable.ic_play_arrow)
//                    return
//                }
//                binding.imageViewplay.setImageResource(R.drawable.ic_play_arrow)
//                //Log.e("TAG", "onTick: " + minute + " " + minutess)
//            }
//        }
//        countdown.start()
//   }

//    fun HandlepullSeek() {
//        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//            }
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//            }
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                ServiceMedia.instance?.let {
//                    it.seekTo(seekBar!!.progress)
//                }
//            }
//        })
//    }
//    fun milisecondtoTimer(mili:Long): String {
//        var timestring = ""
//        var secondstring = ""
//        var minute:Int = ((mili % (1000 * 60 *  60))/ (1000*60)).toInt()
//        var second:Int = ((mili % (1000 * 60 *  60)) % (1000*60)/1000).toInt()
//        if (second<10){
//            secondstring = "0" + second
//        }else{
//            secondstring = "" + second
//        }
//        timestring = "" + minute + ":" + secondstring
//        return timestring
//    }

}


