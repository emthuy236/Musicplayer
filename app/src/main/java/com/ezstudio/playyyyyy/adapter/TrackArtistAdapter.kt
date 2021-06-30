package com.ezstudio.playyyyyy.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.boarding.fragments.screen2.ListTrackArtistFragment

import com.ezstudio.playyyyyy.models.TrackModel

class TrackArtistAdapter(
    var context: Context,
    var lsttrackArtist: ArrayList<TrackModel>,
    var recyclerViewOnClick: RecyclerViewOnClick):RecyclerView.Adapter<TrackArtistAdapter.Viewholder>() {
    var checkPosition = 0
    var itemClick:((TrackModel) -> Unit)? = null
    var trackModelCurrent: TrackModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        context = parent.context
        var layout = LayoutInflater.from(context)
        var view = layout.inflate(R.layout.custom_layout_track_artist,parent,false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {
        return lsttrackArtist.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bindData(lsttrackArtist[position])

    }
    inner class Viewholder(item: View):RecyclerView.ViewHolder(item) {
        var txtsong:TextView = item.findViewById(R.id.namemusicartist)
        var stttrack:TextView = item.findViewById(R.id.sstmusicartist)
        var txtTime:TextView = item.findViewById(R.id.timemusicartist)
       // var imgRun:ImageView = item.findViewById(R.id.img_run_aritstlist)


        fun bindData(trackModel: TrackModel){
            if (trackModelCurrent == trackModel) {
                //imgRun.visibility = View.VISIBLE
                txtsong.setTextColor(Color.parseColor("#2C79DA"))
                txtTime.setTextColor(Color.parseColor("#2C79DA"))
            } else {
                //imgRun.visibility = View.GONE
                txtsong.setTextColor(Color.parseColor("#FFFFFF"))

            }
            trackModel.let {
                txtsong.text = trackModel.title
                txtTime.text = trackModel.timemuisc
                stttrack.text = (adapterPosition+1).toString()

                itemView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        //imgRun.visibility = View.VISIBLE
                        txtsong.setTextColor(Color.parseColor("#2C79DA"))
                        txtTime.setTextColor(Color.parseColor("#2C79DA"))
                        if (checkPosition != adapterPosition){
                            notifyItemChanged(checkPosition)
                            checkPosition = adapterPosition
                        }
                        itemClick?.let {
                            it(trackModel)
                        }
                        recyclerViewOnClick.onItemClick(checkPosition)
                    }
                })
            }
        }


    }
}