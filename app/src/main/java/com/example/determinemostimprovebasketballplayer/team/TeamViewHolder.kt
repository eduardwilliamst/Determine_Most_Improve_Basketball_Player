package com.example.determinemostimprovebasketballplayer.team

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.determinemostimprovebasketballplayer.DetailTeamActivity
import com.example.determinemostimprovebasketballplayer.R

class TeamViewHolder (

    private val itemView: View,

) : RecyclerView.ViewHolder(itemView) {

    private var nama_team: AppCompatTextView? = null
    private var kota_team: AppCompatTextView? = null

    fun bindData(data: DataTeam) {
        nama_team = itemView.findViewById(R.id.txt_name_team)
        kota_team = itemView.findViewById(R.id.txt_name_city)

        nama_team?.text = data.name_team
        kota_team?.text = data.kota_team

        val teamId = data.id_team

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, DetailTeamActivity::class.java)
            intent.putExtra("TEAM_ID", teamId)
            itemView.context.startActivity(intent)
        }

    }

}