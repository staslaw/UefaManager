package org.example.init.model

import org.example.init.PRINTING_NAME_TAB
import org.example.init.transfermarktBaseLink
import org.jsoup.Jsoup


class Federation(val name: String, val link: String) {
    val ranking: UefaRankingPoints = UefaRankingPoints()
    var league: League? = initLeague()


    private fun initLeague(): League? {
        println("Pobieram dane: ${this.name}")
        val doc = Jsoup.connect(this.link).get()
        if (!doc.toString().contains("1.liga")) return null
        val div = doc.getElementById("yw1") ?: return null
        val tables = div.getElementsByClass("inline-table")
        val leagueInfo = tables[0].select("td")[1]
        val leagueName = leagueInfo.text()
        val leagueLink = leagueInfo.select("a").attr("href")
        val fullLink = "${transfermarktBaseLink}$leagueLink"
        return League(leagueName, fullLink)
    }

    fun addNewSeason() {
        this.ranking.initNewSeason()
        this.league?.teams?.forEach { it.addNewSeason() }
    }

    fun getRankingSummaryLine(forSeason: String): String {
        var line = this.name
        for (i in line.length..< PRINTING_NAME_TAB) line = "$line "
        val pointsLine = this.ranking.getRankingSummaryLine(forSeason)
        return "$line$pointsLine"
    }

    fun printFederationSummary() {
        println("=====     ${this.name} - PODSUMOWANIE     =====")
        this.league?.let { league ->
            println("1 LIGA: ${league.name}")
            league.teams.forEach { team ->
                println(team.getTeamInfoLine())
            }
        }
        println("=====     KONIEC PODSUMOWANIA     =====")
    }
}
