package org.example.service

import org.example.init.utils.PRINTING_ID_TAB
import org.example.model.Club
import org.example.model.getCurrentSeasonPoints
import org.example.model.getFiveYearsRanking
import org.example.model.getPreviousSeasonPoints
import org.example.repository.ClubRepository


class ClubService() {

    fun printClubRanking(season: String) {
        val clubs = ClubRepository.getClubsWithRank()
        val sortedClubs = sortClubsForRanking(clubs, season)
        println("CLUBS: ${clubs.size}, RANKED: ${sortedClubs.size}")
        println(getRankingHeader(season))
        sortedClubs.forEachIndexed { id, club ->
            var line = "${id + 1}."
            for (i in line.length..< PRINTING_ID_TAB) line = "$line "
            val clubRankLine = club.rankSummary(season)
            println("$line$clubRankLine")
        }
    }

    private fun sortClubsForRanking(clubs: Set<Club>, season: String): List<Club> {
        return clubs.filter { it.rankingPoints.getFiveYearsRanking(season) != 0.0 }
            .sortedWith(compareByDescending<Club> { it.rankingPoints.getFiveYearsRanking(season) }
            .thenByDescending { it.rankingPoints.getCurrentSeasonPoints(season) }
            .thenByDescending { it.rankingPoints.getPreviousSeasonPoints(season) }
        )
    }
}