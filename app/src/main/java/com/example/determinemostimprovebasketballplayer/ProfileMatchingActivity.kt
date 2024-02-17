package com.example.determinemostimprovebasketballplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.determinemostimprovebasketballplayer.statistic.Statistic
import org.json.JSONObject

class ProfileMatchingActivity : AppCompatActivity() {

    private val statistics = mutableListOf<Statistic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_matching)
        fetchDataAndPopulateUI()
    }

    private fun fetchDataAndPopulateUI() {
        // ... (implementasi sebelumnya)

        val q = Volley.newRequestQueue(this)
        val url = "http://192.168.1.86/ta_160419129/list_statistik.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresultstatistik", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")

                    statistics.clear()

                    for (i in 0 until data.length()) {
                        val playObj = data.getJSONObject(i)
                        val statistic = Statistic(
                            id = playObj.getString("id"),
                            ppg = playObj.getString("ppg").toFloat(),
                            apg = playObj.getString("apg").toFloat(),
                            rpg = playObj.getString("rpg").toFloat(),
                            spg = playObj.getString("spg").toFloat(),
                            bpg = playObj.getString("bpg").toFloat(),
                            name_player = playObj.getString("name_player"),
                            event_year = playObj.getString("event_year").toInt()
                        )
                        statistics.add(statistic)
                        Log.d("masukPM", it)

                    }

                    calculateProfileMatching()
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)
    }

    private fun calculateProfileMatching() {
        val profileMatching = ProfileMatching()

        // Group statistics by name_player
        val groupedStatistics = statistics.groupBy { it.name_player }

        // Filter groups that have both 2021 and 2022 data
        val filteredGroups = groupedStatistics.filter { (_, statsList) ->
            statsList.any { it.event_year == 2021 } && statsList.any { it.event_year == 2022 }
        }

        // Add filtered statistics to profileMatching
        filteredGroups.forEach { (_, statsList) ->
            statsList.forEach {
                profileMatching.addStatistic(
                    Statistic(
                        id = it.id,
                        name_player = it.name_player,
                        ppg = it.ppg,
                        apg = it.apg,
                        rpg = it.rpg,
                        spg = it.spg,
                        bpg = it.bpg,
                        event_year = it.event_year,
                    )
                )
            }
        }
        Log.d("RankingGap", "$statistics")

        // Hitung nilai gap
        val gapMatrix = profileMatching.calculateGap()

        Log.d("RankingGap", "Gap Matrix:")
        for (row in gapMatrix) {
            Log.d("RankingGAP", row.contentToString())
        }


        // Konversi nilai gap ke bobot
        val weightedGapMatrix = profileMatching.convertToWeightedGap(gapMatrix)
        // Print hasil konversi nilai gap ke bobot (atau sesuaikan dengan tampilan UI Anda)
        Log.d("Ranking", "Weighted Gap Matrix:")
        for (i in weightedGapMatrix.indices) {
            Log.d("Ranking", "${i + 1} ${profileMatching.statistics[i].name_player}")
            Log.d("Ranking", weightedGapMatrix[i].contentToString())
        }


//        // Print hasil konversi (atau sesuaikan dengan tampilan UI Anda)
//        for (i in weightedGapMatrix.indices) {
//            Log.d("Nilai dikonversikan", "${i + 1} ${profileMatching.statistics[i].name_player}")
//            for (j in weightedGapMatrix[i].indices) {
//                Log.d("Nilai dikonversikan", "${weightedGapMatrix[i][j]} ")
//            }
//        }
//
//        // Hitung core factor dan secondary factor
//        val coreFactorMap = profileMatching.calculateCoreFactor()
//        val secondaryFactorMap = profileMatching.calculateSecondaryFactor()
//
//        // Calculate Total Score
//        val totalScoreMap = mutableMapOf<String, Map<String, Double>>()
//
//        for (player in statistics) {
//            val coreFactorValue = coreFactorMap[player.name_player] ?: 0.0
//            val secondaryFactorValue = secondaryFactorMap[player.name_player] ?: 0.0
//
//            val totalScore = mapOf(
//                "PPG" to (0.6 * coreFactorValue + 0.4 * secondaryFactorValue),
//                "APG" to (0.6 * coreFactorValue + 0.4 * secondaryFactorValue),
//                "RPG" to (0.6 * coreFactorValue + 0.4 * secondaryFactorValue),
//                "SPG" to (0.6 * coreFactorValue + 0.4 * secondaryFactorValue),
//                "BPG" to (0.6 * coreFactorValue + 0.4 * secondaryFactorValue)
//            )
//
//            totalScoreMap[player.name_player] = totalScore
//        }
//
//        // Print or use the Total Score results (adjust as needed)
//        for ((player, scores) in totalScoreMap) {
//            Log.d("Total Score", "$player: PPG(${scores["PPG"]}), APG(${scores["APG"]}), RPG(${scores["RPG"]}), SPG(${scores["SPG"]}), BPG(${scores["BPG"]})")
//        }
//
//        // Calculate Ranking
//        val rankingMap = mutableMapOf<String, Double>()
//
//        for (player in statistics) {
//            val ranking = (
//                    0.4 * (totalScoreMap[player.name_player]?.get("PPG") ?: 0.0) +
//                            0.25 * (totalScoreMap[player.name_player]?.get("APG") ?: 0.0) +
//                            0.15 * (totalScoreMap[player.name_player]?.get("RPG") ?: 0.0) +
//                            0.1 * (totalScoreMap[player.name_player]?.get("SPG") ?: 0.0) +
//                            0.1 * (totalScoreMap[player.name_player]?.get("BPG") ?: 0.0)
//                    )
//
//            rankingMap[player.name_player] = ranking
//        }
//
//        // Print or use the Ranking results (adjust as needed)
//        for ((player, ranking) in rankingMap) {
//            Log.d("Ranking", "$player: $ranking")
//        }
    }
}
