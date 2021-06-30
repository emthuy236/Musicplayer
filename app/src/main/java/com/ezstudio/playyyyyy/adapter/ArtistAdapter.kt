package com.ezstudio.playyyyyy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SectionIndexer
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.models.AristModel
import java.util.*

class ArtistAdapter(
    var context: Context,
    var arrayartist:ArrayList<AristModel>,
    var recyclerViewOnClick: RecyclerViewOnClick
):RecyclerView.Adapter<ArtistAdapter.ViewHolder>(), SectionIndexer {
    private  var mSectionPosition:ArrayList<Int>? = null

    class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview) {
        var imgartist:ImageView = itemview.findViewById(R.id.imgcustomartist)
        var txtsingartist:TextView = itemview.findViewById(R.id.namesingartist)
        var txtstartist1:TextView = itemview.findViewById(R.id.txtsttAlbum)
        var txtstartist2:TextView = itemview.findViewById(R.id.txtsttTrack)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context)
        val view = layout.inflate(R.layout.custom_layout_artist,parent,false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayartist.size
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder,  position: Int) {
        val artist = arrayartist.get(position)
        artist.let {
            holder.txtsingartist.setText(artist.titleArtist)
            holder.txtstartist1.setText(artist.numberAlbum.toString() + " Album |")
            holder.txtstartist2.setText(artist.numberTrack.toString() + " Track")
            Glide.with(context)
                .load(it.imArtist)
                .error(R.drawable.ic_default_music)
                .into(holder.imgartist)
        }
        holder.itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                recyclerViewOnClick.onItemClick(position)
            }
        })
    }

    override fun getSections(): Array<String> {
        val sections: MutableList<String> = ArrayList()
        mSectionPosition = ArrayList()
        var i = 0
        val size: Int = arrayartist.size
        while (i < size) {
            val section: String =
                java.lang.String.valueOf(arrayartist.get(i).titleArtist!!.get(0)).toUpperCase(Locale.ROOT)
            if (!sections.contains(section)) {
                sections.add(section)
                mSectionPosition!!.add(i)
            }
            i++

        }

        return sections.toTypedArray<String>()
    }

    override fun getSectionForPosition(position: Int): Int {
        return 0
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        return mSectionPosition!![sectionIndex]
    }
}