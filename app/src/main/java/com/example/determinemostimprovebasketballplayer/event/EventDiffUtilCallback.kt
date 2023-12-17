package com.example.determinemostimprovebasketballplayer.event

import androidx.recyclerview.widget.DiffUtil

class EventDiffUtilCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.name == newItem.name
                && oldItem.tahun == newItem.tahun
    }

}