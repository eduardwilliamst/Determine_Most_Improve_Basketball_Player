package com.example.determinemostimprovebasketballplayer.statistic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.determinemostimprovebasketballplayer.R

class StatisticAdapter: RecyclerView.Adapter<StatisticViewHolder>() {

    private val differ = AsyncListDiffer(this, StatisticDiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        return StatisticViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.table_row_layout,
                parent,
                false,
            ),
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {
        holder.bindData(differ.currentList[position])

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<Statistic>) {
        differ.submitList(data)
    }
}