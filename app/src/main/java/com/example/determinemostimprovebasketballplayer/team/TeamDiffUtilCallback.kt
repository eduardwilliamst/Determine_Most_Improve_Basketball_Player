package com.example.determinemostimprovebasketballplayer.team

import androidx.recyclerview.widget.DiffUtil

class TeamDiffUtilCallback : DiffUtil.ItemCallback<DataTeam>() {
    override fun areItemsTheSame(oldItem: DataTeam, newItem: DataTeam): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DataTeam, newItem: DataTeam): Boolean {
        return oldItem.name_team == newItem.name_team
                && oldItem.kota_team == newItem.kota_team
    }

}