package com.example.determinemostimprovebasketballplayer

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.determinemostimprovebasketballplayer.statistic.Statistic
import org.json.JSONObject
import kotlin.math.max


class AhpActivity : AppCompatActivity() {

    private lateinit var buttonCalculate: Button
    private lateinit var textViewResult: TextView

    private val statistics = mutableListOf<Statistic>() // List to store player statistics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ahp)

        textViewResult = findViewById(R.id.textViewResult)

        // Fetch statistics from API and update UI
        fetchDataAndPopulateUI()
    }

    private fun fetchDataAndPopulateUI() {
        val q = Volley.newRequestQueue(this)
        val url = "http://192.168.100.79/ta_160419129/get_statistik.php"
        var stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> {
                Log.d("apiresultstatistik", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")

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
                            event_year = playObj.getString("event_year").toInt(),
                        )
                        statistics.add(statistic)
                        Log.d("masuk", it)
                    }

                    // Inisialisasi UI selesai, panggil calculateAhp()
                    calculateAhp()
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })
        q.add(stringRequest)
    }

    private fun calculateAhp() {
        // Check for duplicate player names
        val groupedStatistics = statistics.groupBy { it.name_player }

        // Iterate through grouped statistics and adjust values
        val adjustedStatistics = groupedStatistics.flatMap { (_, playerStatistics) ->
            if (playerStatistics.size >= 2) {
                // Sort the player statistics by event_year in descending order
                val sortedStatistics = playerStatistics.sortedByDescending { it.event_year }

                // Subtract the statistics from the latest year with the previous year
                val latestYearStats = sortedStatistics[0]
                val previousYearStats = sortedStatistics[1]

                val adjustedStats = latestYearStats.copy(
                    ppg = latestYearStats.ppg - previousYearStats.ppg,
                    apg = latestYearStats.apg - previousYearStats.apg,
                    rpg = latestYearStats.rpg - previousYearStats.rpg,
                    spg = latestYearStats.spg - previousYearStats.spg,
                    bpg = latestYearStats.bpg - previousYearStats.bpg
                )

                listOf(adjustedStats)
            } else {
                // If only one entry, keep it as is
                playerStatistics
            }
        }

        // Perform AHP calculations using the adjusted statistics data
        val pairwiseComparisonMatrix = createPairwiseComparisonMatrix(adjustedStatistics)
        val weights = calculateAhpWeights(pairwiseComparisonMatrix)

        // Menentukan indeks lima pemain terbaik berdasarkan bobot
        val topPlayersIndices = weights.indices.sortedByDescending { weights[it] }.take(5)

        // Menampilkan hasil AHP untuk lima pemain terbaik
        val resultText = if (topPlayersIndices.isNotEmpty()) {
            val topPlayersNames = topPlayersIndices.map { adjustedStatistics[it].name_player }
            "Top 5 Players: ${topPlayersNames.joinToString(", ")}"
        } else {
            "AHP Result: No player found"
        }

        textViewResult.text = resultText
    }

    private fun createPairwiseComparisonMatrix(statistics: List<Statistic>): Array<DoubleArray> {
        val n = statistics.size
        val matrix = Array(n) { DoubleArray(n) }

        for (i in 0 until n) {
            for (j in 0 until n) {
                if (i != j) {
                    // Using the provided statistics for comparison
                    matrix[i][j] = when {
                        statistics[i].ppg > statistics[j].ppg -> 9.0
                        statistics[i].ppg < statistics[j].ppg -> 1.0 / 9.0
                        else -> 1.0
                    }
                } else {
                    matrix[i][j] = 1.0
                }
            }
        }

        return matrix
    }

    private fun calculateAhpWeights(pairwiseComparisonMatrix: Array<DoubleArray>): DoubleArray {
        val n = pairwiseComparisonMatrix.size
        val weights = DoubleArray(n)

        for (i in 0 until n) {
            var sum = 0.0
            for (j in 0 until n) {
                sum += pairwiseComparisonMatrix[i][j]
            }
            weights[i] = sum / n.toDouble()
        }

        return weights
    }
}
