package com.example.determinemostimprovebasketballplayer.player

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.determinemostimprovebasketballplayer.R

class PlayersAdapter: RecyclerView.Adapter<PlayerViewHolder>() {

    private val differ = AsyncListDiffer(this, PlayerDiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_player_layout,
                parent,
                false,
            ),
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bindData(differ.currentList[position])

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<Player>) {
        differ.submitList(data)
    }
}