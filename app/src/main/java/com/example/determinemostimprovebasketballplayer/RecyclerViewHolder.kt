package com.example.determinemostimprovebasketballplayer

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewHolder (

    private val itemView: View,

) : RecyclerView.ViewHolder(itemView) {

    private var nama_team: AppCompatTextView? = null
    private var kota_team: AppCompatTextView? = null

    fun bindData(data: DataTeam) {
        nama_team = itemView.findViewById(R.id.txt_name_team)

        nama_team?.text = data.name_team

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, DetailTeamActivity::class.java)
            itemView.context.startActivity(intent)
        }

    }

}