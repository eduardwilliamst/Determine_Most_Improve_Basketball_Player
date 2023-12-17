package com.example.determinemostimprovebasketballplayer.statistic

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.determinemostimprovebasketballplayer.ListTeamActivity
import com.example.determinemostimprovebasketballplayer.R

class StatisticViewHolder (

    private val itemView: View,

    ) : RecyclerView.ViewHolder(itemView) {

    private var ppg: AppCompatTextView? = null
    private var apg: AppCompatTextView? = null
    private var rpg: AppCompatTextView? = null
    private var spg: AppCompatTextView? = null
    private var bpg: AppCompatTextView? = null
    private var name_player: AppCompatTextView? = null

    fun bindData(data: Statistic) {

        ppg = itemView.findViewById(R.id.tv_ppg)
        apg = itemView.findViewById(R.id.tv_apg)
        rpg = itemView.findViewById(R.id.tv_rpg)
        spg = itemView.findViewById(R.id.tv_spg)
        bpg = itemView.findViewById(R.id.tv_bpg)
        name_player = itemView.findViewById(R.id.tv_player_name)

        ppg?.text = data.ppg.toString()
        apg?.text = data.apg.toString()
        rpg?.text = data.rpg.toString()
        spg?.text = data.spg.toString()
        bpg?.text = data.bpg.toString()
        name_player?.text = data.name_player
    }

}