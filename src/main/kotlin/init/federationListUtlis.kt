package org.example.init

import org.example.init.model.Federation
import org.example.init.model.UefaRankingPoints


fun List<Federation>.printRankingList(forSeason: String) {
    println(UefaRankingPoints.getRankingHeader(forSeason))
    val sortedFederations = sortFederations(forSeason)
    for (i in 1..sortedFederations.size) {
        val federationString = sortedFederations[i - 1].getRankingSummaryLine(forSeason)
        var line = "$i."
        for (i in line.length..< PRINTING_ID_TAB) line = "$line "
        println("$line$federationString")
    }
}

private fun List<Federation>.sortFederations(forSeason: String): List<Federation> {
    return ArrayList(this)
        .sortedWith(compareByDescending<Federation> { it.ranking.getFiveYearsRanking(forSeason) }
            .thenByDescending { it.ranking.getCurrentSeasonPoints(forSeason) }
            .thenByDescending { it.ranking.getPreviousSeasonPoints(forSeason) }
        )
}
