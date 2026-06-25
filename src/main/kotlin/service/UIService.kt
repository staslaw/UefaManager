package org.example.service

import org.example.init.PRINTING_ID_TAB
import org.example.init.addNewSeason
import org.example.init.currentSeason
import org.example.init.federations
import org.example.init.model.Team
import org.example.init.model.UefaRankingPoints
import org.example.init.printRankingList
import org.example.init.seasons


class UIService() {

    fun printCurrentSeason() {
        println("Aktualny sezon to: $currentSeason")
    }

    fun addSeason() {
        addNewSeason()
    }

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
        if (seasons.contains(season)) {
            federations.printRankingList(season)
        } else {
            println("Nie ma takiego sezonu w bazie.")
        }
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