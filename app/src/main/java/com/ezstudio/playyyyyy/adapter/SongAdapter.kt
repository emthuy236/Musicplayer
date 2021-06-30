package com.ezstudio.playyyyyy.adapter

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SectionIndexer
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezstudio.playyyyyy.FunctionState
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.RecyclerViewOnClick
import com.ezstudio.playyyyyy.models.TrackModel
import java.util.*
import kotlin.collections.ArrayList


class SongAdapter(
    var context: Context,
    var arraysong: ArrayList<TrackModel>,
    var itemOnClick: RecyclerViewOnClick
) : RecyclerView.Adapter<SongAdapter.ViewHolder>(),SectionIndexer  {

    var checkPosition = -1
    var trackModelCurrent: TrackModel? = null
    var itemClick: ((TrackModel) -> Unit)? = null
    private  var mSectionPosition:ArrayList<Int>? = null
    private var dataList:List<String>? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layout = LayoutInflater.from(context)
        var view = layout.inflate(R.layout.custom_tracks, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arraysong.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(arraysong[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var namesong: TextView = itemView.findViewById(R.id.namesongartist)
        var namesing: TextView = itemView.findViewById(R.id.namesingartisttrack)
        var imgtrack: ImageView = itemView.findViewById(R.id.imgcustomTrackartist)
       // var imgRun: ImageView = itemView.findViewById(R.id.img_run_music)
        var imgMenugray: ImageView = itemView.findViewById(R.id.menu_music_gray)

        fun bindData(trackModel: TrackModel) {
            trackModel.let {
                if (trackModelCurrent == trackModel) {
                    //imgRun.visibility = View.VISIBLE
                    namesong.setTextColor(Color.parseColor("#2C79DA"))
                    namesing.setTextColor(Color.parseColor("#75A1DE"))
                } else {
                    //imgRun.visibility = View.GONE
                    namesong.setTextColor(Color.parseColor("#FFFFFF"))
                    namesing.setTextColor(Color.parseColor("#818181"))
                }

                namesong.text = it.title
                namesing.text = it.artist
                Glide.with(context)
                    .load(it.imTrack)
                    .error(R.drawable.ic_default_music)
                    .into(imgtrack)

                imgMenugray.setOnClickListener {
                    val wrapper: Context = ContextThemeWrapper(context, R.style.PopupMenuStyle)
                    val popup = PopupMenu(wrapper, imgMenugray)
                    popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(item: MenuItem?): Boolean {
                            when (item?.itemId) {
                                R.id.share -> itemOnClick.onSelectedFun(
                                    trackModel,
                                    FunctionState.SHARE
                                )
                                R.id.track_detail -> itemOnClick.onSelectedFun(
                                    trackModel,
                                    FunctionState.DETAILS
                                )
                                R.id.albums -> itemOnClick.onSelectedFun(
                                    trackModel,
                                    FunctionState.ALBUM
                                )
                                R.id.artist -> itemOnClick.onSelectedFun(
                                    trackModel,
                                    FunctionState.ARTIST
                                )

                                else -> return true
                            }
                            return true
                        }
                    })
                    val inflater = popup.menuInflater
                    inflater.inflate(R.menu.custom_menu_track_vertical, popup.menu)
                    popup.show()
                }

                itemView.setOnClickListener {
                    //imgRun.visibility = View.VISIBLE
                    namesong.setTextColor(Color.parseColor("#2C79DA"))
                    namesing.setTextColor(Color.parseColor("#75A1DE"))
//                    if (checkPosition != adapterPosition) {
//                        notifyItemChanged(checkPosition)
//                        checkPosition = adapterPosition
//                    }
                    itemClick?.let {
                        it(trackModel)
                    }
                    itemOnClick.onItemClick(position)
                }
            }
        }
    }


    override fun getSections(): Array<String> {
        val sections: MutableList<String> = ArrayList()
        mSectionPosition = ArrayList()
            var i = 0
            val size: Int = arraysong.size
            while (i < size) {
                val section: String =
                    java.lang.String.valueOf(arraysong.get(i).title?.get(0)).toUpperCase(Locale.ROOT)
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