package com.example.determinemostimprovebasketballplayer.statistic

data class Statistic(
    val id: String,
    val ppg: Float,
    val apg: Float,
    val rpg: Float,
    val spg: Float,
    val bpg: Float,
    val name_player: String,
    val event_year: Int
)
