package com.ezstudio.playyyyyy.boarding.fragments.screen2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.ezstudio.playyyyyy.AlbumArt
import com.ezstudio.playyyyyy.FunctionState
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.activity.AlbumActivity
import com.ezstudio.playyyyyy.adapter.AlbumArtistAdapter
import com.ezstudio.playyyyyy.databinding.FragmentListAlbumAritstBinding
import com.ezstudio.playyyyyy.models.AlbumModel
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.utils.Config


class ListAlbumAritstFragment : Fragment(),RecyclerViewOnClick {
    private lateinit var binding:FragmentListAlbumAritstBinding
    private lateinit var albumArtistAdapter: AlbumArtistAdapter
    private lateinit var listAlbum:ArrayList<AlbumModel>
    private  var countAlbumListener:((Int) -> Unit)? = null

    companion object{
        fun getInstance(id: Long?): ListAlbumAritstFragment {
            val listAlbumAritstFragment =
                ListAlbumAritstFragment()
            val bundle = Bundle()
            if (id != null) {
                bundle.putLong("data", id)
            }
            listAlbumAritstFragment.arguments = bundle
            return listAlbumAritstFragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListAlbumAritstBinding.inflate(inflater, container, false)
        val view = binding.root
        val idArtistAlbum = arguments?.getLong("data")
        listAlbum = arrayListOf()
        context?.let {
            listAlbum.clear()
            listAlbum.addAll(AlbumArt.getAllAlbum(it,idArtistAlbum,null))
            countAlbumListener?.let {
                it(listAlbum.size)
            }
        }
        getRecyclerview()
        if(albumArtistAdapter.itemCount == 0){
            binding.recyclealbumaritst.visibility = View.GONE
           binding.imgartistalbumactivity.setImageResource(R.drawable.ic_default_music)
            binding.namealbumartist.text = getString(R.string.no_song)
            binding.tvYearAlbumartist.text = getString(R.string.year_release)
        } else {
            binding.recyclealbumaritst.visibility = View.VISIBLE
            Glide.with(requireContext())
                .load(listAlbum[id].albumImage)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_music)
                .into(binding.imgartistalbumactivity)
            binding.namealbumartist.text = listAlbum.get(id).titlealbum
            if (listAlbum.get(id).yearRelease == 0){
                binding.tvYearAlbumartist.visibility = View.GONE
            }else{
                binding.tvYearAlbumartist.text = listAlbum.get(id).yearRelease.toString()
            }
        }
        binding.namealbumartist.isSelected = true
        return view
    }

    fun getRecyclerview(){
        binding.recyclealbumaritst.setHasFixedSize(true)
        albumArtistAdapter = AlbumArtistAdapter(requireContext(),listAlbum,this)
        binding.recyclealbumaritst.adapter = albumArtistAdapter
    }

    override fun onItemClick(position: Int) {
        val intent:Intent = Intent(context,AlbumActivity::class.java)
        intent.putExtra(Config.DATA_ALBUM_DETAIL,listAlbum[position].albumid)
        startActivity(intent)
    }

    override fun onSelectedFun(trackModel: TrackModel, state: FunctionState) {
        TODO("Not yet implemented")
    }





}