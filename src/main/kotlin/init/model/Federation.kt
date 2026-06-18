package org.example.init.model

import org.example.init.currentSeason
import org.example.init.seasons
import org.example.init.transfermarktBaseLink
import org.jsoup.Jsoup
import kotlin.math.min


class Federation(val name: String, val link: String) {
    val rankingPointsRawMap: MutableMap<String, Double> = mutableMapOf()
    val ranking: ArrayList<RankPointsPerSeason> = ArrayList()
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

    fun calculateRanking() {
        val sortedRanks = this.rankingPointsRawMap.toSortedMap(compareBy { it }).toList()
        for (i in 0..< sortedRanks.size) {
            val season = sortedRanks[i].first
            val seasonRank = sortedRanks[i].second
            var fiveYearsRank = 0.0
            val numberOfSeasons = min(i, 4)
            for (k in 0..numberOfSeasons) {
                fiveYearsRank += sortedRanks[i - k].second
            }
            val ranking = RankPointsPerSeason(season, seasonRank, fiveYearsRank)
            this.ranking.add(ranking)
        }
    }

    fun addNewSeason() {
        val last = ranking.lastIndex
        val fiveYearsRank = ranking[last].fiveYearsRank - ranking[last - 4].seasonRank
        val newSeasonRank = RankPointsPerSeason(currentSeason, 0.0, fiveYearsRank)
        ranking.add(newSeasonRank)
        this.league?.teams?.forEach { it.addNewSeason() }
    }

    fun getRankingSummaryLine(forSeason: String): String {
        var line = this.name
        val defaultLength = 30
        val defaultTab = 10
        for (i in 1..defaultLength - line.length) line = "$line "

        val seasonsIndex = seasons.indexOf(forSeason)
        val totalRank = ranking[seasonsIndex].fiveYearsRank
        for (i in 4 downTo 0) {
            val seasonRank = ranking[seasonsIndex - i].seasonRank
            line = "$line $seasonRank"
            for (i in 1..defaultTab - seasonRank.toString().length) line = "$line "
        }
        val rankString = String.format("%.3f", totalRank)
        return "$line|     $rankString"
    }

    fun printFederationSummary() {
        println("=====     ${this.name} - PODSUMOWANIE     =====")
        this.league?.let { league ->
            println("1 LIGA: ${league.name}")
            league.teams.forEach { team ->
                println(team.toString())
            }
        }
        println("=====     KONIEC PODSUMOWANIA     =====")
    }
}
