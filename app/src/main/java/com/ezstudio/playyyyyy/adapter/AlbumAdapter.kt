package com.ezstudio.playyyyyy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.activity.AlbumActivity

import com.ezstudio.playyyyyy.models.AlbumModel
import java.util.ArrayList

class AlbumAdapter(var context: Context, var arrayalbum:ArrayList<AlbumModel>,var recyclerViewOnClick: RecyclerViewOnClick):RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    var checkPosition = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context)
        val view = layout.inflate(R.layout.custom_layout_album,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return arrayalbum.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindata(arrayalbum[position])
    }
    inner class ViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        var imAblbum:ImageView = itemview.findViewById(R.id.im_album)
        var tvAlbumName:TextView = itemview.findViewById(R.id.tv_ablbum_name)
        var tvAlbumArtist:TextView = itemview.findViewById(R.id.tv_ablbum_artist)

        fun bindata(albumModel: AlbumModel){
            albumModel.let {
                tvAlbumName.text = it.titlealbum
                tvAlbumArtist.text = it.artistalbum + " |"
                Glide.with(context)
                    .load(it.albumImage)
                    .error(R.drawable.ic_music_default)
                    .into(imAblbum)
            }
            itemView.setOnClickListener {
//                if (checkPosition != adapterPosition) {
//                    notifyItemChanged(checkPosition)
//                    checkPosition = adapterPosition
//                }
                recyclerViewOnClick.onItemClick(position)
            }
        }
    }



}