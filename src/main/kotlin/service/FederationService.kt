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
        printRankingList(season)
    }

    private fun printRankingList(forSeason: String) {
        println(UefaRankingPoints.getRankingHeader(forSeason))
        val sortedFederations = sortFederations(forSeason)
        for (i in 1..sortedFederations.size) {
            val federationString = sortedFederations[i - 1].getRankingSummaryLine(forSeason)
            var line = "$i."
            for (i in line.length..< PRINTING_ID_TAB) line = "$line "
            println("$line$federationString")
        }
    }

    private fun sortFederations(forSeason: String): List<Federation> {
        return ArrayList(this.federations)
            .sortedWith(compareByDescending<Federation> { it.ranking.getFiveYearsRanking(forSeason) }
                .thenByDescending { it.ranking.getCurrentSeasonPoints(forSeason) }
                .thenByDescending { it.ranking.getPreviousSeasonPoints(forSeason) }
            )
    }

    fun addNewSeason() {
        federations.forEach { it.addNewSeason() }
    }
}