package com.ezstudio.playyyyyy.boarding.fragments.screen2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ezstudio.playyyyyy.AlbumArt
import com.ezstudio.playyyyyy.AlbumArt.Companion.getAudioByAlbumId
import com.ezstudio.playyyyyy.FunctionState
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick

import com.ezstudio.playyyyyy.adapter.TrackArtistAdapter
import com.ezstudio.playyyyyy.databinding.FragmentListTrackArtistBinding
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.services.ServiceMedia
import com.ezstudio.playyyyyy.utils.Config
import kotlinx.coroutines.launch


class ListTrackArtistFragment : Fragment(), RecyclerViewOnClick {
    private lateinit var binding: FragmentListTrackArtistBinding
    var trackArtistAdapter: TrackArtistAdapter? = null
    private lateinit var listTrack: ArrayList<TrackModel>


    var countTrackListener: ((Int) -> Unit)? = null
    //var albumId:Long = 0

    companion object {
        fun getInstance(id: Long?): ListTrackArtistFragment {
            val listTrackArtistFragment =
                ListTrackArtistFragment()
            val bundle = Bundle()
            if (id != null) {
                bundle.putLong("data", id)
            }
            listTrackArtistFragment.arguments = bundle
            return listTrackArtistFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListTrackArtistBinding.inflate(inflater, container, false)
        val view = binding.root
        listTrack = arrayListOf()

             val idArtist = arguments?.getLong("data")


        lifecycleScope.launch {
            context?.let {
                listTrack.clear()
                listTrack.addAll(getAudioByAlbumId(it,null,idArtist))
                countTrackListener?.let {
                    it(listTrack.size)
                }
            }
        }
        Glide.with(requireContext())
            .load(listTrack[id].imTrack)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_default_music)
            .into(binding.imgartistactivity)
        binding.namealbum.text = listTrack.get(id).title
        if (listTrack.get(id).yearrelease == 0){
            binding.tvYear.visibility = View.GONE
        }else{
            binding.tvYear.text = listTrack.get(id).yearrelease.toString()
        }


        getRecyclerview()
        eventRunMusic()
        onClickRandom()
        updateUIListTrack()
        return view
    }

//    

    fun getRecyclerview() {
        binding.listTrackArtist.setHasFixedSize(true)
        trackArtistAdapter = TrackArtistAdapter(requireContext(), listTrack, this)
        binding.listTrackArtist.adapter = trackArtistAdapter

    }
    fun eventRunMusic(){
        binding.imgPlayArtist.setOnClickListener {
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
        binding.imgFollowArtist.setOnClickListener {
            ServiceMedia.instance?.let {
               it.randomMusic()
            }

        }
    }


    override fun onItemClick(position: Int) {
        val intent = Intent(requireContext(), ServiceMedia::class.java)
        intent.putParcelableArrayListExtra(
            Config.DATA_RUN_TRACK_SERVICE,
            trackArtistAdapter?.lsttrackArtist
        )
        intent.putExtra(Config.DATA_PATH_SERVICE, position)
        ContextCompat.startForegroundService(requireContext(), intent)
    }
    fun updateUIListTrack(){
        trackArtistAdapter?.let {
            it.itemClick = {track ->
                Glide.with(requireContext())
                    .load( track.imTrack )
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_default_music)
                    .into(binding.imgartistactivity)
                binding.namealbum.text = track.title
                if (track.year == 0) {
                    binding.tvYear.visibility = View.GONE
                } else {
                    binding.tvYear.text = track.year.toString()
                }

            }
        }
    }
    override fun onSelectedFun(trackModel: TrackModel, state: FunctionState) {
        TODO("Not yet implemented")
    }



    fun updateAdapterTrackArtist(trackModel: TrackModel) {
        trackArtistAdapter?.let {
            it.trackModelCurrent = trackModel
            it.notifyDataSetChanged()
        }
    }


}