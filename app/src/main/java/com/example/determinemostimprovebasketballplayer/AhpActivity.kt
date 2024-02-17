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

class AhpActivity : AppCompatActivity() {

    private lateinit var buttonCalculate: Button
    private lateinit var textViewResult: TextView

    private val statistics = mutableListOf<Statistic>() // List to store player statistics
    private lateinit var topPlayersNames: List<String> // List to store top players' names

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ahp)

        textViewResult = findViewById(R.id.textViewResult)

        // Fetch statistics from API and update UI
        fetchDataAndPopulateUI()
    }

    private fun fetchDataAndPopulateUI() {
        val q = Volley.newRequestQueue(this)
        val url = "http://192.168.1.86/ta_160419129/get_statistik.php"
        val stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("apiresultstatistik", response)
                val obj = JSONObject(response)
                if(obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    for(i in 0 until data.length()) {
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
            topPlayersNames = topPlayersIndices.map { adjustedStatistics[it].name_player }
            "Top 5 Players: ${topPlayersNames.joinToString(", ")}"
        } else {
            "AHP Result: No player found"
        }

        textViewResult.text = resultText

        // Ambil data baru untuk pemain terbaik dan lakukan sesuatu dengan data tersebut
        fetchDataForTopPlayers()
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

    private fun fetchDataForTopPlayers() {
        val q = Volley.newRequestQueue(this)
        val url = "http://192.168.1.86/ta_160419129/list_statistik.php"
        val stringRequest = StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("apiresultstatistik", response)
                val obj = JSONObject(response)
                if(obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    val topPlayersStatistics = mutableListOf<Statistic>()

                    for(i in 0 until data.length()) {
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

                        // Memeriksa apakah name_player terdapat dalam list topPlayersNames
                        if (topPlayersNames.contains(statistic.name_player)) {
                            topPlayersStatistics.add(statistic)
                        }
                    }

                    // Lakukan sesuatu dengan data untuk pemain terbaik, misalnya simpan ke dalam variabel atau lakukan operasi lain
                    // Misalnya:
                    // processDataForTopPlayers(topPlayersStatistics)
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            })

        q.add(stringRequest)
    }

    private fun calculateMostProgressivePlayer(players: List<Statistic>): Statistic {
        // Data untuk musim 2021 dan 2022
        val players2021 = players.filter { it.event_year == 2021 }
        val players2022 = players.filter { it.event_year == 2022 }

        // Hitung profil matching untuk setiap pemain
        val profileMatchings = players2021.mapIndexed { index, player2021 ->
            val player2022 = players2022.find { it.name_player == player2021.name_player }
                ?: throw IllegalArgumentException("Data pemain tidak ditemukan untuk tahun 2022")
            val profileMatching = calculateProfileMatching(player2022, player2021)
            Pair(player2021, profileMatching)
        }

        // Tentukan pemain dengan profil matching tertinggi
        return profileMatchings.maxByOrNull { it.second }
            ?.first ?: throw IllegalStateException("Tidak dapat menentukan pemain paling progresif")
    }

    private fun calculateProfileMatching(player2022: Statistic, player2021: Statistic): Double {
        // Hitung perbedaan antara data tahun 2022 dan 2021 untuk setiap kriteria
        val ppgDifference = player2022.ppg - player2021.ppg
        val apgDifference = player2022.apg - player2021.apg
        val rpgDifference = player2022.rpg - player2021.rpg
        val spgDifference = player2022.spg - player2021.spg
        val bpgDifference = player2022.bpg - player2021.bpg

        // Hitung pemetaan gap
        val gapMapping = calculateGapMapping(player2022, player2021)

        // Pembobotan nilai gap
        val weightedGap = calculateWeightedGap(gapMapping)

        // Hitung profil matching dengan menjumlahkan bobot nilai gap untuk setiap kriteria
        val profileMatching = weightedGap.sum()

        // Hitung core factor untuk player2022
        val coreFactor = calculateCoreFactor(player2022)

        // Hitung secondary factor untuk player2022
        val secondaryFactor = calculateSecondaryFactor(player2022)

        // Hitung total score
        val totalScore = calculateTotalScore(coreFactor, secondaryFactor, 0.6, 0.4)

        // Menampilkan hasil pemetaan gap, bobot nilai gap, core factor, secondary factor, dan total score untuk setiap pemain
        Log.d("Gap Mapping", "${player2022.name_player}: ${gapMapping.contentToString()}")
        Log.d("Weighted Gap", "${player2022.name_player}: ${weightedGap.contentToString()}")
        Log.d("Core Factor", "${player2022.name_player}: $coreFactor")
        Log.d("Secondary Factor", "${player2022.name_player}: $secondaryFactor")
        Log.d("Total Score", "${player2022.name_player}: $totalScore")

        return profileMatching
    }


    private fun getFieldValue(statistic: Statistic, fieldName: String): Float {
        return when (fieldName) {
            "PPG" -> statistic.ppg
            "APG" -> statistic.apg
            "RPG" -> statistic.rpg
            "SPG" -> statistic.spg
            "BPG" -> statistic.bpg
            else -> throw IllegalArgumentException("Field $fieldName not found in Statistic object")
        }
    }

    private fun calculateCoreFactor(player2022: Statistic): Double {
        // Kriteria Core Factor
        val coreFactorFields = listOf("PPG", "APG", "RPG", "SPG", "BPG").subList(0, 3)

        // Menghitung total nilai Core Factor
        val totalCoreFactor = coreFactorFields.sumByDouble { field -> getFieldValue(player2022, field).toDouble() }

        // Menghitung rata-rata Core Factor
        val coreFactorAverage = totalCoreFactor / coreFactorFields.size

        return coreFactorAverage
    }

    private fun calculateSecondaryFactor(player2022: Statistic): Double {
        // Kriteria Secondary Factor
        val secondaryFactorFields = listOf("PPG", "APG", "RPG", "SPG", "BPG").subList(3, 5)

        // Menghitung total nilai Secondary Factor
        val totalSecondaryFactor = secondaryFactorFields.sumByDouble { field -> getFieldValue(player2022, field).toDouble() }

        // Menghitung rata-rata Secondary Factor
        val secondaryFactorAverage = totalSecondaryFactor / secondaryFactorFields.size

        return secondaryFactorAverage
    }

    private fun calculateTotalScore(coreFactor: Double, secondaryFactor: Double, coreFactorPercentage: Double, secondaryFactorPercentage: Double): Double {
        // Hitung nilai total berdasarkan persentase core factor dan secondary factor
        val totalScore = (coreFactorPercentage * coreFactor) + (secondaryFactorPercentage * secondaryFactor)
        return totalScore
    }

    private fun calculateWeightedGap(gapMapping: Array<Double>): Array<Double> {
        // Bobot nilai gap sesuai dengan tabel yang diberikan
        val weightMapping = mapOf(
            0 to 5.0,
            1 to 4.5,
            -1 to 4.0,
            2 to 3.5,
            -2 to 3.0,
            3 to 2.5,
            -3 to 2.0,
            4 to 1.5,
            -4 to 1.0
        )

        // Lakukan pembobotan nilai gap
        return gapMapping.map { gap -> weightMapping[gap.toInt()] ?: error("Bobot nilai gap tidak ditemukan") }
            .toTypedArray()
    }


    private fun calculateGapMapping(player2022: Statistic, player2021: Statistic): Array<Double> {
        val ppgGap = player2022.ppg - player2021.ppg
        val apgGap = player2022.apg - player2021.apg
        val rpgGap = player2022.rpg - player2021.rpg
        val spgGap = player2022.spg - player2021.spg
        val bpgGap = player2022.bpg - player2021.bpg

        return arrayOf(ppgGap.toDouble(), apgGap.toDouble(), rpgGap.toDouble(), spgGap.toDouble(), bpgGap.toDouble())
    }

}
