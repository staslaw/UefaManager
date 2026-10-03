package org.example.service

import org.example.init.utils.PRINTING_ID_TAB
import org.example.model.Federation
import org.example.model.getCurrentSeasonPoints
import org.example.model.getFiveYearsRanking
import org.example.model.getPreviousSeasonPoints
import org.example.repository.FederationRepository
import org.example.service.SeasonService.Companion.getEuropeanCurrentSeason


class FederationService() {
    private val federations = FederationRepository.getAllFederations()

    fun printFederations() {
        federations.forEach { println(it.name) }
    }

    fun printFederationSummary(federationName: String): String {
        if (federations.map { it.name }.toList().contains(federationName)) {
            val federation = federations.first { it.name == federationName }
            val rankingPosition = sortFederations(getEuropeanCurrentSeason()).indexOf(federation) + 1
            federation.printFederationSummary(rankingPosition)
            return federationName
        } else {
            println("Nie ma takiej federacji w bazie.")
            return ""
        }
    }

    fun printFederationRanking(season: String) {
        println(getRankingHeader(season))
        val sortedFederations = sortFederations(season)
        for (i in 1..sortedFederations.size) {
            val federationString = sortedFederations[i - 1].getRankSummaryLine(season)
            var line = "$i."
            for (i in line.length..< PRINTING_ID_TAB) line = "$line "
            println("$line$federationString")
        }
    }

    private fun sortFederations(season: String): List<Federation> {
        return ArrayList(this.federations)
            .sortedWith(compareByDescending<Federation> { it.rankingPoints.getFiveYearsRanking(season) }
                .thenByDescending { it.rankingPoints.getCurrentSeasonPoints(season) }
                .thenByDescending { it.rankingPoints.getPreviousSeasonPoints(season) }
            )
    }

    fun addNewSeason() {
        federations.forEach { it.addNewSeason() }
    }

    fun getAvailableSeasonsForFederation(federationName: String): List<String> {
        return federations
            .find { it.name == federationName }
            ?.let { federation ->
                val current = SeasonService.getCurrentSeason(federation.calendarSystem)
                val previous = SeasonService.getPreviousSeason(federation.calendarSystem)
                listOf(previous, current)
            } ?: emptyList()
    }

    fun getAvailableLeagues(chosenSeason: String, federationName: String): List<String> {
        return federations.find { it.name == federationName }?.let { federation ->
            federation.campaigns.find { it.season == chosenSeason }?.let { campaign ->
                campaign.leagues.map { it.name } }
        } ?: emptyList()
    }

    fun printLeagueSummary(federationName: String, chosenLeague: String, chosenSeason: String){
        federations.find { it.name == federationName }?.let { federation ->
            federation.campaigns.find { it.season == chosenSeason }?.let { campaign ->
                campaign.leagues.find { it.name == chosenLeague }?.printSummary()
            }
        }
    }
}