package com.example.determinemostimprovebasketballplayer

import com.example.determinemostimprovebasketballplayer.statistic.Statistic

class ProfileMatching {

    private val nilaiStandar = arrayOf(
        arrayOf(25.1, 30.0),
        arrayOf(18.1, 25.0),
        arrayOf(12.1, 18.0),
        arrayOf(6.1, 12.0),
        arrayOf(0.0, 6.0)
    )

    private val bobotNilai = arrayOf(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)

    val statistics = mutableListOf<Statistic>()

    fun addStatistic(statistic: Statistic) {
        statistics.add(statistic)
    }

    fun calculateGap(): Array<Array<Int>> {
        val gapMatrix = Array(statistics.size) { Array(statistics.size) { 0 } }

        for (i in statistics.indices) {
            for (j in statistics.indices) {
                if (i != j) {
                    gapMatrix[i][j] = calculateIndividualGap(statistics[i], statistics[j])
                }
            }
        }

        return gapMatrix
    }

    private fun calculateIndividualGap(statistic1: Statistic, statistic2: Statistic): Int {
        val gaps = arrayOf(
            calculateGapValue(statistic1.ppg.toDouble(), statistic2.ppg.toDouble()),
            calculateGapValue(statistic1.apg.toDouble(), statistic2.apg.toDouble()),
            calculateGapValue(statistic1.rpg.toDouble(), statistic2.rpg.toDouble()),
            calculateGapValue(statistic1.spg.toDouble(), statistic2.spg.toDouble()),
            calculateGapValue(statistic1.bpg.toDouble(), statistic2.bpg.toDouble())
        )

        return gaps.sum()
    }

    private fun calculateGapValue(value1: Double, value2: Double): Int {
        for (i in nilaiStandar.indices) {
            if (value1 >= nilaiStandar[i][0] && value1 <= nilaiStandar[i][1] &&
                value2 >= nilaiStandar[i][0] && value2 <= nilaiStandar[i][1]
            ) {
                return i + 1
            }
        }
        return 0
    }

    private fun getWeightedGapValue(index1: Int, index2: Int): Double {
        val individualGap = calculateIndividualGap(statistics[index1], statistics[index2])

        // If the individualGap is 0, return 0.0 to avoid division by zero
        return if (individualGap > 0 && individualGap <= bobotNilai.size) {
            bobotNilai[individualGap - 1]
        } else {
            0.0
        }
    }



    fun convertToWeightedGap(gapMatrix: Array<Array<Int>>): Array<Array<Double>> {
        val weightedGapMatrix = Array(gapMatrix.size) { Array(gapMatrix.size) { 0.0 } }

        for (i in gapMatrix.indices) {
            for (j in gapMatrix[i].indices) {
                if (i != j) {
                    weightedGapMatrix[i][j] = getWeightedGapValue(i, j)
                }
            }
        }
        return weightedGapMatrix
    }

    fun calculateCoreFactor(): Map<String, Double> {
        val coreFactorMap = mutableMapOf<String, Double>()

        for (i in 0 until statistics.size) {
            val coreFactorValues = mutableListOf<Double>()

            // PPG (A1)
            coreFactorValues.add(getWeightedGapValue(i, i))
            // APG (A3)
            coreFactorValues.add(getWeightedGapValue(i, i + 2))
            // RPG (A5)
            coreFactorValues.add(getWeightedGapValue(i, i + 4))
            // SPG (A7)
            coreFactorValues.add(getWeightedGapValue(i, i + 6))
            // BPG (A9)
            coreFactorValues.add(getWeightedGapValue(i, i + 8))

            val nc = coreFactorValues.sum()
            val ic = coreFactorValues.size.toDouble()

            val ncf = if (ic != 0.0) nc / ic else 0.0
            coreFactorMap[statistics[i].name_player] = ncf
        }

        return coreFactorMap
    }

    fun calculateSecondaryFactor(): Map<String, Double> {
        val secondaryFactorMap = mutableMapOf<String, Double>()

        for (i in 0 until statistics.size) {
            val secondaryFactorValues = mutableListOf<Double>()

            // PPG (A2)
            secondaryFactorValues.add(getWeightedGapValue(i, i + 1))
            // APG (A4)
            secondaryFactorValues.add(getWeightedGapValue(i, i + 3))
            // RPG (A6)
            secondaryFactorValues.add(getWeightedGapValue(i, i + 5))
            // SPG (A8)
            secondaryFactorValues.add(getWeightedGapValue(i, i + 7))
            // BPG (A10)
            secondaryFactorValues.add(getWeightedGapValue(i, i + 9))

            val ns = secondaryFactorValues.sum()
            val `is` = secondaryFactorValues.size.toDouble()

            val nsf = if (`is` != 0.0) ns / `is` else 0.0
            secondaryFactorMap[statistics[i].name_player] = nsf
        }

        return secondaryFactorMap
    }
}
