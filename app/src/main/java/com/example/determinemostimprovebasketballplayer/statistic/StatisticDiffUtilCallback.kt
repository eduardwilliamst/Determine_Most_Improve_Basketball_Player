package com.example.determinemostimprovebasketballplayer.statistic

import androidx.recyclerview.widget.DiffUtil

class StatisticDiffUtilCallback : DiffUtil.ItemCallback<Statistic>() {
    override fun areItemsTheSame(oldItem: Statistic, newItem: Statistic): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Statistic, newItem: Statistic): Boolean {
        return oldItem.id == newItem.id
                && oldItem.ppg == newItem.ppg
                && oldItem.apg == newItem.apg
                && oldItem.rpg == newItem.rpg
                && oldItem.spg == newItem.spg
                && oldItem.bpg == newItem.bpg

    }

}