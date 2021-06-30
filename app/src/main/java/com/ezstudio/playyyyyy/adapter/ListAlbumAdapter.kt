package com.ezstudio.playyyyyy.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.models.TrackModel

class ListAlbumAdapter(var lstData: ArrayList<TrackModel>,var recyclerViewOnClick: RecyclerViewOnClick):RecyclerView.Adapter<ListAlbumAdapter.ViewHolder>() {

    private lateinit var context:Context
    var checkPosition = 0
    var trackmodelCurrent:TrackModel? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layout = LayoutInflater.from(parent.context)
        var view = layout.inflate(R.layout.custom_list_album_fragment,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lstData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(lstData[position])
    }

    inner class ViewHolder(item:View):RecyclerView.ViewHolder(item) {
        var txtstt:TextView = item.findViewById(R.id.sstmusic)
        var namemusic:TextView = item.findViewById(R.id.namemusic)
        var timemusic:TextView = item.findViewById(R.id.timemusic)
        var namesing:TextView = item.findViewById(R.id.tv_namesingalbumlist)
        //var imgRun:ImageView = item.findViewById(R.id.img_run_albumlist)

        fun bindData(trackModel: TrackModel){
            trackModel.let {
                if (trackmodelCurrent == trackModel) {
//                    imgRun.visibility = View.VISIBLE
                    namemusic.setTextColor(Color.parseColor("#2C79DA"))
                    namesing.setTextColor(Color.parseColor("#75A1DE"))
                    timemusic.setTextColor(Color.parseColor("#2C79DA"))
                } else {
                    //imgRun.visibility = View.GONE
                    namemusic.setTextColor(Color.parseColor("#FFFFFF"))
                    namesing.setTextColor(Color.parseColor("#818181"))
                    timemusic.setTextColor(Color.parseColor("#FFFFFF"))
                }
                txtstt.text = (adapterPosition+1).toString()
                namemusic.text = trackModel.title

                timemusic.text = it.timemuisc
                namemusic.isSelected = true
                namesing.text = trackModel.artist
                itemView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        //imgRun.visibility = View.VISIBLE
                        namemusic.setTextColor(Color.parseColor("#2C79DA"))
                        namesing.setTextColor(Color.parseColor("#75A1DE"))
                        timemusic.setTextColor(Color.parseColor("#2C79DA"))
                        if (checkPosition != adapterPosition){
                            notifyItemChanged(checkPosition)
                            checkPosition = adapterPosition
                        }
                        recyclerViewOnClick.onItemClick(checkPosition)
                    }
                })
            }
        }
    }


}