package com.example.determinemostimprovebasketballplayer.event

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.determinemostimprovebasketballplayer.DetailTeamActivity
import com.example.determinemostimprovebasketballplayer.ListTeamActivity
import com.example.determinemostimprovebasketballplayer.R

class EventViewHolder (

    private val itemView: View,

    ) : RecyclerView.ViewHolder(itemView) {

    private var name_event: AppCompatTextView? = null
    private var year_event: AppCompatTextView? = null

    fun bindData(data: Event) {
        name_event = itemView.findViewById(R.id.txt_name_event)
        year_event = itemView.findViewById(R.id.txt_event_year)

        name_event?.text = data.name
        year_event?.text = data.tahun

        val eventId = data.id_event

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, ListTeamActivity::class.java)
            intent.putExtra("EVENT_ID", eventId)
            itemView.context.startActivity(intent)
        }

    }

}