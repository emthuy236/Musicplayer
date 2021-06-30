package com.ezstudio.playyyyyy.boarding.fragments.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezstudio.playyyyyy.AlbumArt
import com.ezstudio.playyyyyy.FunctionState
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.activity.AlbumActivity
import com.ezstudio.playyyyyy.activity.ArtistAcitviy
import com.ezstudio.playyyyyy.activity.MusicDetailActivity
import com.ezstudio.playyyyyy.adapter.SongAdapter
import com.ezstudio.playyyyyy.databinding.FragmentTracksBinding
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.services.ServiceMedia
import com.ezstudio.playyyyyy.utils.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class TracksFragment : Fragment(), RecyclerViewOnClick {
    private lateinit var bindingTrackFragment: FragmentTracksBinding
    private lateinit var trackfile: ArrayList<TrackModel>
    private var songAdapter: SongAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingTrackFragment = FragmentTracksBinding.inflate(inflater, container, false)
        val view = bindingTrackFragment.root
        trackfile = arrayListOf()
//        context?.let {
//            trackfile.clear()
//            trackfile.addAll(AlbumArt.getAudioByAlbumId(it, null, null))
//        }

        lifecycleScope.launch {
            trackfile.clear()
            val tracks = AlbumArt.getAudioByAlbumId(requireContext(), null, null)
            trackfile.addAll(tracks)
        }
        getRecyclerview()
        clickMenu()
        onClick()
        onClickRandom()
        return view
    }

    fun getRecyclerview() {

        bindingTrackFragment.recycleTrack.setHasFixedSize(true)
        songAdapter = SongAdapter(
            requireContext(),
            trackfile, this
        )

        bindingTrackFragment.recycleTrack.adapter = songAdapter
        bindingTrackFragment.recycleTrack.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        bindingTrackFragment.recycleTrack.setIndexTextSize(12);
        bindingTrackFragment.recycleTrack.setIndexBarTextColor(R.color.white);
        bindingTrackFragment.recycleTrack.setIndexBarColor(R.color.colormenu);
        bindingTrackFragment.recycleTrack.setIndexbarHighLateTextColor("#FF4081");
        bindingTrackFragment.recycleTrack.setIndexBarHighLateTextVisibility(true);
        bindingTrackFragment.recycleTrack.setIndexBarTransparentValue( 1.0f)
        bindingTrackFragment.recycleTrack.setIndexBarCornerRadius(10)

    }
    fun onClick(){
        bindingTrackFragment.imgRunMusicTrack.setOnClickListener {
            ServiceMedia.instance?.let {
                it.runFirstSongMusic()
            }
        }
    }
    fun onClickRandom(){
        bindingTrackFragment.imgSkipMusic.setOnClickListener {
            ServiceMedia.instance?.let {
                it.randomMusic()
            }
        }
    }

    fun clickMenu() {
        bindingTrackFragment.imgMenuTrack.setOnClickListener {
            val wrapper: Context = ContextThemeWrapper(context, R.style.PopupMenuStyle)
            val popup = PopupMenu(wrapper, bindingTrackFragment.imgMenuTrack)
             popup.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener {
                 override fun onMenuItemClick(item: MenuItem?): Boolean {
                     when (item?.itemId) {
                         R.id.nametrack -> {
                             trackfile.sortBy {
                                 it.title
                             }
                             songAdapter!!.notifyDataSetChanged()
                         }
                         R.id.datetrack -> {
                             trackfile.sortBy {
                                 it.year
                             }
                             songAdapter!!.notifyDataSetChanged()
                         }
                         R.id.artiststrack -> {
                             trackfile.sortBy {
                                 it.artist
                             }
                             songAdapter!!.notifyDataSetChanged()
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

    override fun onItemClick(position: Int) {
        val intent = Intent(requireContext(), ServiceMedia::class.java)
        intent.putParcelableArrayListExtra(Config.DATA_RUN_TRACK_SERVICE, trackfile)
        intent.putExtra(Config.DATA_PATH_SERVICE, position)
        startForegroundService(requireContext(),intent)
    }

    @SuppressLint("SetTextI18n")
    override fun onSelectedFun(trackModel: TrackModel, state: FunctionState) {
        when(state){
            FunctionState.SHARE ->{
                val intent = Intent()
                intent.type = "audio/mpeg"
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.ACTION_MEDIA_SHARED,trackModel.path)
                startActivity(Intent.createChooser(intent,"Please seclect app: "))
//                val videoFileUri =
//                    Uri.parse(trackModel.path)
//                val shareVideo = ShareVideo.Builder()
//                    .setLocalUrl(videoFileUri)
//                    .build()
//                val content = ShareVideoContent.Builder()
//                    .setVideo(shareVideo)
//                    .build()

            }
            FunctionState.DETAILS -> {
                val intent = Intent(requireContext(),MusicDetailActivity::class.java)
                intent.putExtra(Config.DATA_DETAIL_MUSIC_ACTIVITY,trackModel.albumid)
                startActivity(intent)
            }
            FunctionState.ALBUM ->{
               val intent = Intent(requireContext(),AlbumActivity::class.java)
                intent.putExtra(Config.DATA_ALBUM_DETAIL,trackModel.albumid)
                startActivity(intent)
            }
            FunctionState.ARTIST -> {
                val intent = Intent(requireContext(),ArtistAcitviy::class.java)
                intent.putExtra(Config.DATA_ARTIST_DETAIL,trackModel.artistId)
                startActivity(intent)
            }

            else -> return
        }
    }

    fun updateAdapter(trackModel: TrackModel) {
        songAdapter?.let {
            it.trackModelCurrent = trackModel
            it.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
      //  job.cancel()
    }




}