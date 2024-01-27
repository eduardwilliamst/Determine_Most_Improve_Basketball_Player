package com.example.determinemostimprovebasketballplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.determinemostimprovebasketballplayer.statistic.Statistic
import org.json.JSONObject
import java.io.BufferedReader
import java.io.StringReader
import kotlin.streams.toList

class ProfileMatchingActivity : AppCompatActivity() {


    private val statistics = mutableListOf<Statistic>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_matching)
    }

    private fun fetchDataAndPopulateUI() {
        val q = Volley.newRequestQueue(this)
        val url = "http://192.168.18.94/ta_160419129/list_statistik.php"
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
                            event_year = playObj.getString("event_yer").toInt()
                        )
                        statistics.add(statistic)
                        Log.d("masuk", it)
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

//        val result = performProfileMatching(statistics)
//        Log.d("ProfileMatchingResult", result)
//
//        val resultTextView: TextView = findViewById(R.id.textViewResultPM)
//        resultTextView.text = result
    }

    private fun performProfileMatching(data: String): String {
        // Baca data dari string
        val reader = BufferedReader(StringReader(data))
        val lines = reader.lines().toList()

        // Ambil nama pemain sebagai kunci
        val playerNames = lines.drop(1).map { it.split(",")[6] }

        // Hitung nilai total untuk setiap pemain (implementasi sederhana)
        val playerTotalScores = mutableMapOf<String, Double>()

        lines.drop(1).forEach { line ->
            val values = line.split(",")
            val playerName = values[6]
            val ppg = values[1].toDouble()
            val apg = values[2].toDouble()
            val rpg = values[3].toDouble()
            val spg = values[4].toDouble()
            val bpg = values[5].toDouble()

            // Hitung nilai total sederhana (contoh bobot yang sama untuk setiap kriteria)
            val totalScore = (ppg + apg + rpg + spg + bpg) / 5.0

            // Tambahkan atau update nilai total untuk pemain
            playerTotalScores[playerName] = totalScore
        }

        // Urutkan pemain berdasarkan nilai total (descending)
        val sortedPlayers = playerTotalScores.toList().sortedByDescending { it.second }

        // Format hasil
        val resultStringBuilder = StringBuilder()
        resultStringBuilder.append("Hasil Profile Matching:\n")

        sortedPlayers.forEachIndexed { index, (player, totalScore) ->
            resultStringBuilder.append("${index + 1}. $player - Nilai Total: $totalScore\n")
        }

        return resultStringBuilder.toString()
    }
}