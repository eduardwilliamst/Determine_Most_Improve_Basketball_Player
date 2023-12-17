package com.example.determinemostimprovebasketballplayer.player

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.determinemostimprovebasketballplayer.R

class PlayerViewHolder (

    private val itemView: View,

    ) : RecyclerView.ViewHolder(itemView) {

    private var id: AppCompatTextView? = null
    private var nama: AppCompatTextView? = null
    private var umur: AppCompatTextView? = null
    private var tinggi: AppCompatTextView? = null
    private var berat: AppCompatTextView? = null

    fun bindData(data: Player) {
        nama = itemView.findViewById(R.id.text_view_player_name)
        umur = itemView.findViewById(R.id.text_view_player_age)
        tinggi = itemView.findViewById(R.id.text_view_player_height)
        berat = itemView.findViewById(R.id.text_view_player_weight)

        nama?.text = data.nama
        umur?.text = data.umur
        tinggi?.text = data.tinggi
        berat?.text = data.berat
    }

}