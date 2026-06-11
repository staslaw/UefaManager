package org.example.init

import org.example.init.model.Federation


fun List<Federation>.printRankingList(forSeason: String) {
    printListHeader(forSeason)
    val sortedFederations = sortFederations(forSeason)
    for (i in 1..sortedFederations.size) {
        val federationString = sortedFederations[i - 1].getRankingSummaryLine(forSeason)
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
