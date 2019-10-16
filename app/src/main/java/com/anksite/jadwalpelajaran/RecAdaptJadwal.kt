package com.anksite.jadwalpelajaran

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_list_jadwal.view.*

class RecAdaptJadwal(listJadwal:ArrayList<DataJadwal>) : RecyclerView.Adapter<RecAdaptJadwal.ViewHolderJadwal>(){
    val mListJadwal = listJadwal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderJadwal {
        val inflater = LayoutInflater.from(parent.context)
        val viewInflate = inflater.inflate(R.layout.row_list_jadwal, parent, false)
        return ViewHolderJadwal(viewInflate)
    }

    override fun getItemCount(): Int = mListJadwal.size

    override fun onBindViewHolder(holder: ViewHolderJadwal, position: Int) {
        val dataJadwal = mListJadwal[position]
        var day = dataJadwal.day

        val i = holder.itemView

        when(day){
            "1" -> day = "MINGGU"
            "2" -> day = "SENIN"
            "3" -> day = "SELASA"
            "4" -> day = "RABU"
            "5" -> day = "KAMIS"
            "6" -> day = "JUMAT"
            "7" -> day = "SABTU"
        }

        if(day.contains("#")){
            i.cv_day.visibility = View.GONE
        }else{
            i.cv_day.visibility = View.VISIBLE
        }

        i.tv_day.text   = day
        i.tv_jam.text   = dataJadwal.start+" - "+dataJadwal.end
        i.tv_room.text  = dataJadwal.room
        i.tv_mapel.text = dataJadwal.mapel
        i.tv_guru.text  = dataJadwal.guru
    }

    class ViewHolderJadwal(itemView: View):RecyclerView.ViewHolder(itemView)
}