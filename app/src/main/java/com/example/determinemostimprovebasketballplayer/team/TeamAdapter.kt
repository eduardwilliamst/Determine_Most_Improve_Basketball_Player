package com.example.determinemostimprovebasketballplayer.team

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.determinemostimprovebasketballplayer.R

class TeamAdapter: RecyclerView.Adapter<TeamViewHolder>() {

    private val differ = AsyncListDiffer(this, TeamDiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_team_layout,
                parent,
                false,
            ),
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bindData(differ.currentList[position])

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<DataTeam>) {
        differ.submitList(data)
    }
}