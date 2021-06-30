package com.ezstudio.playyyyyy.services


import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.ResultReceiver
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.palette.graphics.Palette
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.activity.HomeActivity
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.utils.Config
import java.io.IOException
import java.util.*
import kotlin.random.Random


class ServiceMedia : Service() {

    var mediaSession: MediaSessionCompat? = null
    var mediaplayer: MediaPlayer? = null
    var audimanager: AudioManager? = null
    var lstTrack = arrayListOf<TrackModel>()
    var telphoneManager: TelephonyManager? = null
    var mPhoneStateListener: PhoneStateListener? = null
    var indexMusic = -1
    var uriImage: Uri? = null
    var random = false

    companion object {
        const val CHANNEL_ID = 111
        const val NOTIFICAITON_ID = 112
        var instance: ServiceMedia? = null
        val ACTION_PLAY: String = "Play"

        val ACTION_NEXT: String = "Next"
        val ACTION_PREVIOUS: String = "Previous"
        val ACTION_EXIT: String = "Exit"
    }

    override fun onCreate() {
        super.onCreate()
        if (instance == null) {
            instance = this
        }
        Log.e("XXX", "onCreate")
        mPhoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                // Test for incoming call, dialing call, active or on hold
                if (state == TelephonyManager.CALL_STATE_RINGING || state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // Put here the code to stop your music
                    if (isPlaying()) {
                        pauseAudio()
                        initMediaCompat()
                        updateNotification()
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e("XXX", "onStartCommand")
        if (instance == null) {
            instance = this
        }
        if (intent.action != null) {
            when (intent.action) {
                ACTION_PLAY -> {
                    if (isPlaying()) {
                        pauseAudio()
                    } else {
                        resumeAudio()
                        initMediaCompat()
                    }
                    updateNotification()
                }
                ACTION_NEXT -> {
                    nextAudio()
                    updateNotification()
                }
                ACTION_PREVIOUS -> {
                    previousAudio()
                    updateNotification()
                }
                ACTION_EXIT -> {
                    stopForeground(CHANNEL_ID)
                    onDestroy()
                }
            }
        } else {
            createNotificationChannel()
            lstTrack =
                intent.getParcelableArrayListExtra<TrackModel>(Config.DATA_RUN_TRACK_SERVICE) as ArrayList<TrackModel>
            indexMusic = intent.getIntExtra(Config.DATA_PATH_SERVICE, -1)
            Log.e("XXX", "onStartCommand: " + indexMusic )
            startForeground(NOTIFICAITON_ID, showNotification())
            playAudio()

        }
        return START_STICKY
    }

    override fun onDestroy() {
        if (mediaSession != null) {
            mediaSession!!.release()
            audimanager!!.abandonAudioFocus(audiofocusChangeLisener)
            releaseAudio()
            lstTrack.clear()
            updateUIAll()
        }
        releaseAudio()
        instance = null
        super.onDestroy()
    }

    private fun showNotification(): Notification {
        if (indexMusic < 0) {
               indexMusic = 0
              // uriImage = lstTrack[indexMusic].imTrack
        } else {
            uriImage = lstTrack[indexMusic].imTrack
            }
        var bmImage: Bitmap? = null
        try {
            bmImage = MediaStore.Images.Media.getBitmap(this.contentResolver, uriImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (bmImage == null) {
            bmImage = BitmapFactory.decodeResource(resources, R.drawable.ic_music_default)
        }

        val notifiintent: Intent = Intent(this, HomeActivity::class.java)
        val pending: PendingIntent =
            PendingIntent.getActivity(this, CHANNEL_ID, notifiintent, 0)

        val previntent: Intent = Intent(this, NotificationReciver::class.java).setAction(
            ACTION_PREVIOUS
        )
        val pendingprev =
            PendingIntent.getBroadcast(this, 0, previntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val playintent: Intent = Intent(this, NotificationReciver::class.java).setAction(
            ACTION_PLAY
        )
        val pendingplay =
            PendingIntent.getBroadcast(this, 0, playintent, PendingIntent.FLAG_UPDATE_CURRENT)
        val nextintent: Intent = Intent(this, NotificationReciver::class.java).setAction(
            ACTION_NEXT
        )

        val pendingnext =
            PendingIntent.getBroadcast(this, 0, nextintent, PendingIntent.FLAG_UPDATE_CURRENT)
        val exitintent: Intent = Intent(this, NotificationReciver::class.java).setAction(
            ACTION_EXIT
        )
        val pendingexit =
            PendingIntent.getBroadcast(this, 0, exitintent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID.toString())
            .setContentTitle(lstTrack[indexMusic].title)
            .setContentText(lstTrack[indexMusic].artist)
            .setSmallIcon(R.drawable.ic_notication)
            .setContentIntent(pending)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_skip_previous, "Previous", pendingprev)
            .addAction(
                if (isPlaying()) {
                    R.drawable.ic_play_arrow
                } else {
                    R.drawable.ic_paly_detail
                },
                "Play",
                pendingplay
            )
            .addAction(R.drawable.ic_skip_next, "Next", pendingnext)
            .addAction(R.drawable.ic_clear, "Exit", pendingexit)
            .setLargeIcon(bmImage)
            .setColor(
                Palette.from(bmImage!!).generate().getVibrantColor(Color.parseColor("#403f4d"))
            )
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2, 3)
                    .setMediaSession(mediaSession?.sessionToken)
            )
            .setChannelId("some_channel_id")
            .build()

        return notification
    }

    fun updateNotification() {
        setLockScreen(lstTrack[indexMusic])
        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICAITON_ID, showNotification())
    }

     fun setLockScreen(tracks: TrackModel) {
        try {
            val uriImage = tracks.imTrack
            var bmImage: Bitmap? = null
            try {
                bmImage = MediaStore.Images.Media.getBitmap(this.contentResolver, uriImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (bmImage != null) {
                mediaSession!!.setMetadata(
                    MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, tracks.artist)
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, tracks.album)
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, tracks.title)
                        .putLong(
                            MediaMetadataCompat.METADATA_KEY_DURATION,
                            getDuration().toLong()
                        )
                        .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bmImage)
                        .build()
                )
            }
        } catch (e: Exception) {
            mediaSession!!.setMetadata(
                MediaMetadataCompat.Builder()

                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, tracks.artist)
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, tracks.album)
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, tracks.title)
                    .putLong(
                        MediaMetadataCompat.METADATA_KEY_DURATION,
                        getDuration().toLong()
                    )
                    .build()
            )
        }
        Log.d("MUSIC", "SeekTo ${getCurrentTime()}")
        mediaSession!!.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    if (isPlaying()) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                    getCurrentTime().toLong(),
                    1.0f
                )
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_PLAY_PAUSE or
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
                .build()
        )
        mediaSession!!.isActive = true


    }


    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManagerCompat =
                NotificationManagerCompat.from(this)
            val channelId = "some_channel_id"
            val channelName: CharSequence = "Some Channel"
            val importance = NotificationManager.IMPORTANCE_LOW
            val notificationChannel =
                NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun playAudio() {
        releaseAudio()
        val uri = Uri.parse(lstTrack[indexMusic].path)
        mediaplayer = MediaPlayer.create(applicationContext, uri)
        mediaplayer?.let {
            it.start()
            it.setOnCompletionListener { nextAudio() }
        }
        initMediaCompat()
        updateNotification()
        updateUIAll()
    }

    private fun updateUIAll() {
        val intentBroadcastUIMusic = Intent()
        if (lstTrack.isNotEmpty())
            intentBroadcastUIMusic.putExtra(Config.DATA_UI_MUSIC_BROADCAST, lstTrack[indexMusic])
        intentBroadcastUIMusic.action = "update.UI.Broadcast"
        sendBroadcast(intentBroadcastUIMusic)
    }

    fun pauseAudio() {
        mediaplayer?.let {
            if (it.isPlaying) {
                it.pause()
                updateNotification()
                updateUIAll()
            }
        }
    }

    fun resumeAudio() {
        mediaplayer?.let {
            it.start()
            updateNotification()
            updateUIAll()
        }
    }
    fun runFirstSongMusic(){
        indexMusic = 0
        playAudio()

    }

    fun onComplete() {
        mediaplayer?.let {
            it.setOnCompletionListener {
                nextAudio()
            }
            updateNotification()
            updateUIAll()

        }
    }

    fun nextAudio() {
        if (indexMusic == lstTrack.size - 1) {
            indexMusic = 0
        } else {
            indexMusic++
        }
        playAudio()
    }

    fun repeatMusic() {
        var count = 0
        mediaplayer?.let {
            it.setOnCompletionListener {
                if (count < indexMusic) {
                    count = indexMusic

                } else if (count > indexMusic) {
                    count = indexMusic
                } else {
                    count = indexMusic
                }
                resumeAudio()
            }
        }
    }

    fun randomMusic() {
        val random = Random
        indexMusic = random.nextInt(lstTrack.size - 1)
        playAudio()
        updateNotification()
        updateUIAll()

    }


    fun updateMusicOnComplete() {
        mediaplayer?.let {
            if (it.isPlaying) {
                resumeAudio()
                it.setOnCompletionListener {
                    randomMusic()
                }
            }

        }
    }

    fun previousAudio() {
        if (indexMusic == 0) {
            indexMusic = lstTrack.size - 1
        } else {
            indexMusic--
        }
        playAudio()
    }

    fun isPlaying(): Boolean {
        mediaplayer?.let {
            return it.isPlaying
        }
        return false
    }

    fun seekTo(progress: Int) {
        mediaplayer?.let {
            it.seekTo(progress)
            setLockScreen(lstTrack[indexMusic])
//            updateNotification()
//            updateUIAll()
        }
    }

    fun releaseAudio() {
        mediaplayer?.let {
            it.stop()
            it.release()
            mediaplayer = null
            updateUIAll()
        }
    }

    fun getCurrentTime(): Int {
        mediaplayer?.let {
            return it.currentPosition
        }
        return 0
    }

    fun getDuration(): Int {
        mediaplayer?.let {
            return it.duration
        }
        return 0
    }

    fun stop() {
        mediaplayer?.let {
            it.stop()
        }
    }

    private var audiofocusChangeLisener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (mediaplayer == null) {
                    playAudio()
                } else {
                    if (isPlaying()) pauseAudio()
                    // else resumeAudio()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                if (isPlaying()) {
                    pauseAudio()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                if (isPlaying()) {
                    pauseAudio()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                if (isPlaying()) mediaplayer?.setVolume(0.1f, 0.1f)
            }
        }
        updateNotification()

    }

    fun initMediaCompat() {
        audimanager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        if (mediaSession != null) {
            mediaSession!!.release()
            audimanager!!.abandonAudioFocus(audiofocusChangeLisener)
        }
        mediaSession = MediaSessionCompat(this, "PlayerService")
        mediaSession!!.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )
        mediaSession!!.setCallback(mMediaSessionCallBack)
        mediaSession!!.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 0F)
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .build()
        )
        telphoneManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        if (telphoneManager != null) {
            telphoneManager!!.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
        }

        audimanager!!.requestAudioFocus(
            audiofocusChangeLisener, AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )


    }

    private val mMediaSessionCallBack: MediaSessionCompat.Callback =
        object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                super.onPlay()
                if (!successfullyRetrievedAudioFocus()) {
                    return
                } else {
                    mediaSession!!.isActive = true
                    setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                    playAudio()
                }
            }

            override fun onPause() {
                super.onPause()
                setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED)
                pauseAudio()

            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                mediaSession!!.isActive = true
                setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                nextAudio()
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                mediaSession!!.isActive = true
                setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                previousAudio()
            }

            override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
                super.onPlayFromMediaId(mediaId, extras)
            }

            override fun onCommand(command: String?, extras: Bundle?, cb: ResultReceiver?) {
                super.onCommand(command, extras, cb)
            }

            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
            }
        }

    private fun setMediaPlaybackState(state: Int) {
        val playbackstateBuilder = PlaybackStateCompat.Builder()
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            playbackstateBuilder.setActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PAUSE
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            )
        } else {
            playbackstateBuilder.setActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            )
        }
        playbackstateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
        mediaSession!!.setPlaybackState(playbackstateBuilder.build())
    }

    private fun successfullyRetrievedAudioFocus(): Boolean {
        val result: Int = audimanager!!.requestAudioFocus(
            audiofocusChangeLisener,
            AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN
        )
        return result == AudioManager.AUDIOFOCUS_GAIN
    }
//    fun getBitmapFromURL(strURL: String?): Bitmap? {
//        return try {
//            val url = URL(strURL)
//            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//            connection.setDoInput(true)
//            connection.connect()
//            val input: InputStream = connection.getInputStream()
//            BitmapFactory.decodeStream(input)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }


}



