package com.ezstudio.playyyyyy.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.activity.HomeActivity

import com.ezstudio.playyyyyy.models.AlbumModel
import com.ezstudio.playyyyyy.models.TrackModel
import java.util.ArrayList

class AlbumArtistAdapter(var context:Context,var arraylistalbum:ArrayList<AlbumModel>,var recyclerViewOnClick: RecyclerViewOnClick):RecyclerView.Adapter<AlbumArtistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumArtistAdapter.ViewHolder {
        context = parent.context
        val layout = LayoutInflater.from(context)
        val view = layout.inflate(R.layout.custom_layout_album_artist,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arraylistalbum.size
    }

    override fun onBindViewHolder(holder: AlbumArtistAdapter.ViewHolder, position: Int) {
       holder.bindData(arraylistalbum[position])

    }
    inner class ViewHolder(item: View):RecyclerView.ViewHolder(item) {
        var img:ImageView = item.findViewById(R.id.imgcustomAlbumartist)
        var txtsong:TextView = item.findViewById(R.id.txtnamsongAlbumartist)
        var txtsing:TextView = item.findViewById(R.id.txtnamsingAlbumartist)

        fun bindData(albumModel: AlbumModel){
            albumModel.let {
                txtsong.text = albumModel.titlealbum
                txtsing.text = albumModel.artistalbum
                Glide.with(context)
                    .load(albumModel.albumImage)
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_music_default)
                    .into(img)
            }
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    recyclerViewOnClick.onItemClick(position)

                }
            })
        }

    }
}