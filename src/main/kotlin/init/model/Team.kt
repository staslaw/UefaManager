package org.example.init.model

import org.example.init.currentSeason
import kotlin.math.min


class Team(val name: String, val link: String, val value: String) {
    val ranking: ArrayList<RankPointsPerSeason> = ArrayList()

    fun calculateRanking(rankingMap: MutableMap<String, String>) {
        rankingMap.remove("name")
        val sortedRanks = rankingMap.toSortedMap(compareBy { it }).toList()
        for (i in 0..< sortedRanks.size) {
            val season = sortedRanks[i].first
            val seasonRank = sortedRanks[i].second.replace(",", ".").toDouble()
            var fiveYearsRank = 0.0
            val numberOfSeasons = min(i, 4)
            for (k in 0..numberOfSeasons) {
                fiveYearsRank += sortedRanks[i - k].second.replace(",", ".").toDouble()
            }
            val ranking = RankPointsPerSeason(season, seasonRank, fiveYearsRank)
            this.ranking.add(ranking)
        }
    }

    override fun toString(): String {
        var line = this.name
        val defaultLength = 30
        for (i in 1..defaultLength - line.length) line = "$line "
        return "$line ${this.value}"
    }

    fun rankSummary(): String {
        var line = this.toString()
        val defaultTab = 20
        for (i in 1..defaultTab - this.value.length) line = "$line "
        line = "$line |"
        val defaultRankTab = 10
        for (i in 4 downTo 0) {
            val rank = this.ranking[this.ranking.lastIndex - i].seasonRank
            line = "$line  $rank"
            for (i in 1..defaultRankTab - rank.toString().length) line = "$line "
        }
        return "$line   |   ${this.ranking.last().fiveYearsRank}"
    }

    fun addNewSeason() {
        if (this.ranking.isEmpty()) return
        val last = ranking.lastIndex
        val fiveYearsRank = ranking[last].fiveYearsRank - ranking[last - 4].seasonRank
        val newSeasonRank = RankPointsPerSeason(currentSeason, 0.0, fiveYearsRank)
        ranking.add(newSeasonRank)
    }
}
