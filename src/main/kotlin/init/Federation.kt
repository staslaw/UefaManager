package org.example.init

import kotlin.math.min


class RankPointsPerSeason(val season: String, val seasonRank: Double, val fiveYearsRank: Double) {
    override fun toString(): String {
        return "${this.season} - ${this.seasonRank} - ${this.fiveYearsRank}"
    }
}


class Federation(val name: String, val link: String) {
    val rankingPointsRawMap: MutableMap<String, Double> = mutableMapOf()
    val ranking: ArrayList<RankPointsPerSeason> = ArrayList()

    fun calculateRanking() {
        val sortedRanks = this.rankingPointsRawMap.toSortedMap(compareBy { it }).toList()
        for (i in 0..< sortedRanks.size) {
            val season = sortedRanks[i].first
            val seasonRank = sortedRanks[i].second
            var fiveYearsRank = 0.0
            val numberOfSeasons = min(i, 4)
            for (k in 0..numberOfSeasons) {
                fiveYearsRank += sortedRanks[i - k].second
            }
            val ranking = RankPointsPerSeason(season, seasonRank, fiveYearsRank)
            this.ranking.add(ranking)
        }
    }

    fun addNewSeason() {
        val last = ranking.lastIndex
        val fiveYearsRank = ranking[last].fiveYearsRank - ranking[last - 4].seasonRank
        val newSeasonRank = RankPointsPerSeason(currentSeason, 0.0, fiveYearsRank)
        ranking.add(newSeasonRank)
    }

    fun toString(forSeason: String): String {
        var line = this.name
        val defaultLength = 30
        val defaultTab = 10
        for (i in 1..defaultLength - line.length) line = "$line "

        val seasonsIndex = seasons.indexOf(forSeason)
        val totalRank = ranking[seasonsIndex].fiveYearsRank
        for (i in 4 downTo 0) {
            val seasonRank = ranking[seasonsIndex - i].seasonRank
            line = "$line $seasonRank"
            for (i in 1..defaultTab - seasonRank.toString().length) line = "$line "
        }
        val rankString = String.format("%.3f", totalRank)
        return "$line|     $rankString"
    }
}


fun List<Federation>.calculateRanking() {
    this.forEach { it.calculateRanking() }
}

fun List<Federation>.printList(forSeason: String) {
    printListHeader(forSeason)
    val sortedFederations = sortFederations(forSeason)
    for (i in 1..sortedFederations.size) {
        val federationString = sortedFederations[i - 1].toString(forSeason)
        val spaces = if (i < 10) "   " else "  "
        println("$i.$spaces$federationString")
    }
}

private fun printListHeader(forSeason: String) {
    val defaultLength = 34
    var header = ""
    for (i in 1..defaultLength) header = "$header "
    val forSeasonIndex = seasons.indexOf(forSeason)
    for (i in 4 downTo 0) {
        header = "$header  ${seasons[forSeasonIndex - i]}"
    }
    println("$header       RAZEM")
}

private fun List<Federation>.sortFederations(forSeason: String): List<Federation> {
    return ArrayList(this)
        .sortedWith(compareByDescending<Federation> { it.ranking.find { it.season == forSeason }!!.fiveYearsRank }
            .thenByDescending { it.ranking.find { it.season == forSeason }!!.seasonRank })
}

fun List<Federation>.addNewSeason() {
    this.forEach { it.addNewSeason() }
}
