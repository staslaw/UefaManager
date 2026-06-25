package org.example.service

import org.example.init.PRINTING_ID_TAB
import org.example.init.addNewSeason
import org.example.init.currentSeason
import org.example.init.federations
import org.example.init.model.Team
import org.example.init.model.UefaRankingPoints


class UIService() {
    private val federationService = FederationService()

    fun printCurrentSeason() {
        println("Aktualny sezon to: $currentSeason")
    }

    fun addSeason() {
        addNewSeason()
    }

    fun printFederations() {
        federationService.printFederations()
    }

    fun printFederationSummary(federationName: String) {
        federationService.printFederationSummary(federationName)
    }

    fun printFederationRanking(season: String) {
        federationService.printFederationRanking(season)
    }

    fun printClubRanking() {
        val teams = federations.map { it.clubs }.flatten()
        val ranked = teams.filter { it.ranking.getFiveYearsRanking() != 0.0 }
        println("TEAMS: ${teams.size}, RANKED: ${ranked.size}")
        println(UefaRankingPoints.getRankingHeader())
        ranked.sortedWith(compareByDescending<Team> { it.ranking.getFiveYearsRanking() }
            .thenByDescending { it.ranking.getCurrentSeasonPoints() }
            .thenByDescending { it.ranking.getPreviousSeasonPoints() }
        ).forEachIndexed { id, team ->
            var line = "${id + 1}."
            for (i in line.length..< PRINTING_ID_TAB) line = "$line "
            val teamRankLine = team.rankSummary()
            println("$line$teamRankLine")
        }
    }
}