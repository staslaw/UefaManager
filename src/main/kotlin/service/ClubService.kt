package org.example.service

import org.example.init.PRINTING_ID_TAB
import org.example.init.initializedFederations
import org.example.model.Club
import org.example.model.UefaRankingPoints


class ClubService() {
    private val clubs = initializedFederations.map { it.clubs }.flatten()


    fun printClubRanking() {
        val ranked = this.clubs.filter { it.ranking.getFiveYearsRanking() != 0.0 }
        println("CLUBS: ${this.clubs.size}, RANKED: ${ranked.size}")
        println(UefaRankingPoints.getRankingHeader())
        val sortedClubs = sortClubsForRanking(ranked)
        sortedClubs.forEachIndexed { id, club ->
            var line = "${id + 1}."
            for (i in line.length..< PRINTING_ID_TAB) line = "$line "
            val clubRankLine = club.rankSummary()
            println("$line$clubRankLine")
        }
    }

    private fun sortClubsForRanking(rankedClubs: List<Club>): List<Club> {
        return rankedClubs.sortedWith(compareByDescending<Club> { it.ranking.getFiveYearsRanking() }
            .thenByDescending { it.ranking.getCurrentSeasonPoints() }
            .thenByDescending { it.ranking.getPreviousSeasonPoints() }
        )
    }
}