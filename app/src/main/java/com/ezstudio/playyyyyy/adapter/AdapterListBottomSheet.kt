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
import com.ezstudio.playyyyyy.models.TrackModel

class AdapterListBottomSheet(var context: Context,var listBottom:ArrayList<TrackModel>,var itemOnClick: RecyclerViewOnClick):RecyclerView.Adapter<AdapterListBottomSheet.ViewHolder>() {
    var trackModelCurrent: TrackModel? = null
    var checkPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context)
        val view = layout.inflate(R.layout.custom_list_bottomsheet,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listBottom.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(listBottom[position])
    }
    inner class ViewHolder(item: View):RecyclerView.ViewHolder(item) {
        var image:ImageView = item.findViewById(R.id.imgcustomlistbottom)
        var txtSong:TextView = item.findViewById(R.id.namesonglistbottom)
        var txtSing:TextView = item.findViewById(R.id.namesinglistbottom)
        var imgRun:ImageView = item.findViewById(R.id.img_run_music_listbottom)


        fun bindData(trackModel: TrackModel){
            trackModel.let {
                if (trackModelCurrent == trackModel){
                    imgRun.visibility = View.VISIBLE
                    txtSong.setTextColor(Color.parseColor("#2C79DA"))
                    txtSing.setTextColor(Color.parseColor("#75A1DE"))
                } else {
                    imgRun.visibility = View.GONE
                    txtSong.setTextColor(Color.parseColor("#FFFFFF"))
                    txtSing.setTextColor(Color.parseColor("#818181"))
                }
                txtSong.text = it.title
                txtSing.text = it.artist
                Glide.with(context)
                    .load(it.imTrack)
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_default_music)
                    .into(image)
                }
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    imgRun.visibility = View.VISIBLE
                    txtSong.setTextColor(Color.parseColor("#2C79DA"))
                    txtSing.setTextColor(Color.parseColor("#75A1DE"))
                    if (checkPosition != adapterPosition) {
                        notifyItemChanged(checkPosition)
                        checkPosition = adapterPosition
                    }
                    itemOnClick.onItemClick(checkPosition)
                }
            })


            }
        }
    }
