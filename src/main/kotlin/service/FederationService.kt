package org.example.service

import org.example.init.PRINTING_ID_TAB
import org.example.init.initializedFederations
import org.example.model.Federation
import org.example.model.UefaRankingPoints


class FederationService() {
    private val federations = initializedFederations

    fun printFederations() {
        federations.forEach { println(it.name) }
    }

    fun printFederationSummary(federationName: String) {
        if (federations.map { it.name }.toList().contains(federationName)) {
            val federation = federations.first { it.name == federationName }
            federation.printFederationSummary()
        } else {
            println("Nie ma takiej federacji w bazie.")
        }
    }

    fun printFederationRanking(season: String) {
        println(UefaRankingPoints.getRankingHeader(season))
        val sortedFederations = sortFederations(season)
        for (i in 1..sortedFederations.size) {
            val federationString = sortedFederations[i - 1].getRankingSummaryLine(season)
            var line = "$i."
            for (i in line.length..< PRINTING_ID_TAB) line = "$line "
            println("$line$federationString")
        }
    }

    private fun sortFederations(season: String): List<Federation> {
        return ArrayList(this.federations)
            .sortedWith(compareByDescending<Federation> { it.ranking.getFiveYearsRanking(season) }
                .thenByDescending { it.ranking.getCurrentSeasonPoints(season) }
                .thenByDescending { it.ranking.getPreviousSeasonPoints(season) }
            )
    }

    fun addNewSeason() {
        federations.forEach { it.addNewSeason() }
    }
}