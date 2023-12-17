package com.example.determinemostimprovebasketballplayer.event

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.determinemostimprovebasketballplayer.R

class EventAdapter: RecyclerView.Adapter<EventViewHolder>() {

    private val differ = AsyncListDiffer(this, EventDiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_event_layout,
                parent,
                false,
            ),
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindData(differ.currentList[position])

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<Event>) {
        differ.submitList(data)
    }
}