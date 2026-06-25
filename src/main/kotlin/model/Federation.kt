package org.example.model

import org.example.init.utils.PRINTING_NAME_TAB
import org.example.init.htmlParser.FederationHtmlParser


class Federation(val name: String, val link: String) {
    private val htmlParser = FederationHtmlParser(this.link, this.name)
    val firstLeague: League? = htmlParser.getFirstLeague()
    val secondLeague: League? = htmlParser.getSecondLeague()
    val ranking: UefaRankingPoints = UefaRankingPoints()
    val clubs: List<Club> = getClubsFromLeagues()


    private fun getClubsFromLeagues(): List<Club> {
        val clubsList = mutableListOf<Club>()
        clubsList.addAll(this.firstLeague?.clubs ?: emptyList())
        clubsList.addAll(this.secondLeague?.clubs ?: emptyList())
        return clubsList
    }

    fun addNewSeason() {
        this.ranking.initNewSeason()
        this.clubs.forEach { it.addNewSeason() }
    }

    fun getRankingSummaryLine(season: String): String {
        var line = this.name
        for (i in line.length..< PRINTING_NAME_TAB) line = "$line "
        val pointsLine = this.ranking.getRankingSummaryLine(season)
        return "$line$pointsLine"
    }

    fun printFederationSummary() {
        println("=====     ${this.name} - PODSUMOWANIE     =====")
        this.firstLeague?.let { league ->
            println("1 LIGA: ${league.name}")
            league.clubs.forEach { club ->
                println(club.getClubInfoLine())
            }
        }
        this.secondLeague?.let { league ->
            println("2 LIGA: ${league.name}")
            league.clubs.forEach { club ->
                println(club.getClubInfoLine())
            }
        }
        println("=====     KONIEC PODSUMOWANIA     =====")
    }
}
