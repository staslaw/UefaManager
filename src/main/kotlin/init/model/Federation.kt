package org.example.init.model

import org.example.init.PRINTING_NAME_TAB
import org.example.init.htmlParser.FederationHtmlParser


class Federation(val name: String, val link: String) {
    private val htmlParser = FederationHtmlParser(this.link, this.name)
    val firstLeague: League? = htmlParser.getFirstLeague()
    val secondLeague: League? = htmlParser.getSecondLeague()
    val ranking: UefaRankingPoints = UefaRankingPoints()
    val clubs: List<Team> = getClubsFromLeagues()


    private fun getClubsFromLeagues(): List<Team> {
        val clubsList = mutableListOf<Team>()
        clubsList.addAll(this.firstLeague?.teams ?: emptyList())
        clubsList.addAll(this.secondLeague?.teams ?: emptyList())
        return clubsList
    }

    fun addNewSeason() {
        this.ranking.initNewSeason()
        this.firstLeague?.teams?.forEach { it.addNewSeason() }
    }

    fun getRankingSummaryLine(forSeason: String): String {
        var line = this.name
        for (i in line.length..< PRINTING_NAME_TAB) line = "$line "
        val pointsLine = this.ranking.getRankingSummaryLine(forSeason)
        return "$line$pointsLine"
    }

    fun printFederationSummary() {
        println("=====     ${this.name} - PODSUMOWANIE     =====")
        this.firstLeague?.let { league ->
            println("1 LIGA: ${league.name}")
            league.teams.forEach { team ->
                println(team.getTeamInfoLine())
            }
        }
        this.secondLeague?.let { league ->
            println("2 LIGA: ${league.name}")
            league.teams.forEach { team ->
                println(team.getTeamInfoLine())
            }
        }
        println("=====     KONIEC PODSUMOWANIA     =====")
    }
}
