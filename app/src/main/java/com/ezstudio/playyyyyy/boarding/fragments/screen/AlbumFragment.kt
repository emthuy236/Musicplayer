package com.ezstudio.playyyyyy.boarding.fragments.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.ezstudio.playyyyyy.AlbumArt
import com.ezstudio.playyyyyy.FunctionState
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.activity.AlbumActivity

import com.ezstudio.playyyyyy.adapter.AlbumAdapter

//import com.ezstudio.playyyyyy.adapter.AlbumAdapter
import com.ezstudio.playyyyyy.databinding.FragmentAlbumBinding
import com.ezstudio.playyyyyy.models.AlbumModel
import com.ezstudio.playyyyyy.models.TrackModel
import com.ezstudio.playyyyyy.utils.Config
import kotlinx.coroutines.launch


class AlbumFragment : Fragment(), RecyclerViewOnClick {
    private lateinit var bindingalbum: FragmentAlbumBinding
    private lateinit var modelfile: ArrayList<AlbumModel>
    private lateinit var albumAdapter: AlbumAdapter

    companion object {

//        fun getInstance(id: Int): AlbumFragment {
//            val albumFragment = AlbumFragment()
//            albumFragment.arguments = bundleOf(
//                "data" to id
//            )
//            return albumFragment
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingalbum = FragmentAlbumBinding.inflate(inflater, container, false)
        val view = bindingalbum.root
        modelfile = arrayListOf()
        lifecycleScope.launch {
                modelfile.clear()
                val album = AlbumArt.getAllAlbum(requireContext(),null,null)
                modelfile.addAll(album)
            }

        getRecyclerAlbum()
        clickMenu()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        arguments?.let {
//            var id = it.getInt("data")
//            Toast.makeText(requireActivity(), id.toString(), Toast.LENGTH_SHORT).show()
//        }
    }

    fun getRecyclerAlbum() {
        bindingalbum.recycleAlbum.setHasFixedSize(true)
        albumAdapter = AlbumAdapter(requireContext(), modelfile, this)
        bindingalbum.recycleAlbum.adapter = albumAdapter
    }

    fun clickMenu() {
        bindingalbum.imgMenuAlbum.setOnClickListener {
            val wrapper: Context = ContextThemeWrapper(context, R.style.PopupMenuStyle)
            val popup = PopupMenu(wrapper, bindingalbum.imgMenuAlbum)
            popup.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when (item?.itemId) {
                        R.id.year_release -> {
                            modelfile.sortBy {
                                it.yearRelease
                            }
                            albumAdapter.notifyDataSetChanged()
                        }
                        R.id.name_album -> {
                            modelfile.sortBy {
                                it.titlealbum
                            }
                            albumAdapter.notifyDataSetChanged()
                        }
                        R.id.album_artist -> {
                            modelfile.sortBy {
                                it.artistalbum
                            }
                            albumAdapter.notifyDataSetChanged()
                        }
                        else -> return true
                    }
                    return true
                }

            })
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.custom_menu_album, popup.menu)
            popup.show()
        }

    }

    override fun onItemClick(position: Int) {
        val intent = Intent(context, AlbumActivity::class.java)
        intent.putExtra(Config.DATA_ALBUM_DETAIL, modelfile[position].albumid)
        startActivity(intent)
    }



    override fun onSelectedFun(trackModel: TrackModel, state: FunctionState) {
        TODO("Not yet implemented")
    }


}