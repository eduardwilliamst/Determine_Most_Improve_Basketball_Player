package com.example.determinemostimprovebasketballplayer.player

import androidx.recyclerview.widget.DiffUtil

class PlayerDiffUtilCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.id == newItem.id
                && oldItem.nama == newItem.nama
    }

}