package com.ezstudio.playyyyyy.boarding.fragments.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezstudio.playyyyyy.AlbumArt
import com.ezstudio.playyyyyy.FunctionState
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.activity.ArtistAcitviy
import com.ezstudio.playyyyyy.adapter.ArtistAdapter

//import com.ezstudio.playyyyyy.adapter.ArtistAdapter
import com.ezstudio.playyyyyy.databinding.FragmentArtistBinding
import com.ezstudio.playyyyyy.models.AristModel
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.utils.Config
import kotlinx.coroutines.launch
import java.util.ArrayList


class ArtistFragment : Fragment(),RecyclerViewOnClick {
    private lateinit var bindingArtistFragment:FragmentArtistBinding
    private lateinit var artistfile:ArrayList<AristModel>
    private lateinit var artistAdapter: ArtistAdapter
    companion object{

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingArtistFragment =  FragmentArtistBinding.inflate(inflater, container, false)
        val view = bindingArtistFragment.root
        artistfile = arrayListOf()
        lifecycleScope.launch {
            context?.let {
                artistfile.clear()
                val artist = AlbumArt.getAllArtist(it)
                artistfile.addAll(artist)
            }
        }
        getRecyclerArtist()
        clickMenu()
        return view
    }
    fun getRecyclerArtist(){
        bindingArtistFragment.recycleArtist.setHasFixedSize(true)
            artistAdapter = ArtistAdapter(requireContext(), artistfile,this)
            bindingArtistFragment.recycleArtist.adapter = artistAdapter
            bindingArtistFragment.recycleArtist.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        bindingArtistFragment.recycleArtist.setIndexTextSize(12);
        bindingArtistFragment.recycleArtist.setIndexBarTextColor(R.color.white);
        bindingArtistFragment.recycleArtist.setIndexBarColor(R.color.graybackground);
        bindingArtistFragment.recycleArtist.setIndexbarHighLateTextColor("#FF4081");
        bindingArtistFragment.recycleArtist.setIndexBarHighLateTextVisibility(true);
        bindingArtistFragment.recycleArtist.setIndexBarTransparentValue( 1.0f)
        bindingArtistFragment.recycleArtist.setIndexBarCornerRadius(10)

    }
    fun clickMenu(){
        bindingArtistFragment.imgMenuArtist.setOnClickListener {
            val wrapper: Context = ContextThemeWrapper(context, R.style.PopupMenuStyle)
            val popup = PopupMenu(wrapper, bindingArtistFragment.imgMenuArtist)
            popup.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when (item?.itemId) {
                        R.id.name_artist -> {
                            artistfile.sortBy {
                                it.titleArtist
                            }
                            artistAdapter.notifyDataSetChanged()
                        }
                        R.id.date_artist -> {
                            artistfile.sortBy {
                               it.idArtist
                            }
                            artistAdapter.notifyDataSetChanged()
                        }
                        else -> return true
                    }
                    return true
                }
            })
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.custom_menu_artist, popup.menu)
            popup.show()
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(context,ArtistAcitviy::class.java)
        intent.putExtra(Config.DATA_ARTIST_DETAIL,artistfile[position].idArtist)
        startActivity(intent)
    }

    override fun onSelectedFun(trackModel: TrackModel, state: FunctionState) {
        TODO("Not yet implemented")
    }




}