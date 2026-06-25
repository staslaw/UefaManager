package org.example.init.model

import org.example.init.PRINTING_NAME_TAB
import org.example.init.transfermarktBaseLink
import org.jsoup.Jsoup


class Federation(val name: String, val link: String) {
    val ranking: UefaRankingPoints = UefaRankingPoints()
    var firstLeague: League? = null
    var secondLeague: League? = null
    val clubs: List<Team>

    init {
        println("Pobieram dane: ${this.name}")
        val doc = Jsoup.connect(this.link).get()

        if (doc.toString().contains("1.liga")) {
            doc.getElementById("yw1")?.let { div ->
                val table = div.getElementsByClass("items")[0]
                val tbody = table.select("tbody")[0]
                val rows = tbody.select("tr")
                val leagueInfo = if (this.name == "Malta") {
                    rows.select("td").first { it.text() == "Premier League Closing Round" }
                } else {
                    rows[1].select("tbody")[0].select("td")[1]
                }
                val leagueName = leagueInfo.text()
                val leagueLink = leagueInfo.select("a").attr("href")
                val fullLink = "${transfermarktBaseLink}$leagueLink"
                this.firstLeague = League(leagueName, 1, fullLink)
            }

        }
        if (doc.toString().contains("2.liga")) {
            doc.getElementById("yw1")?.let { div ->
                val table = div.getElementsByClass("items")[0]
                val tbody = table.select("tbody")[0]
                val rows = tbody.select("tr")
                val headerRow = rows.find { it.select("td")[0].text() == "2.liga" }
                val headerRowId = rows.indexOf(headerRow)
                for (r in headerRowId + 1..< rows.size) {
                    if (rows[r].getElementsByClass("extrarow bg_blau_20 hauptlink").isNotEmpty()) {
                        val nextHeaderRowId = rows.indexOf(rows[r])
                        if (nextHeaderRowId - headerRowId > 3) {
                            println("---   ignoruje 2 ligę, są dwie")
                            return@let
                        } else {
                            break
                        }
                    }
                }

                val leagueValue = rows[headerRowId + 1].getElementsByClass("rechts hauptlink").text()
                if (leagueValue == "-" || leagueValue == "" || leagueValue.contains("tys")) {
                    println("---   ignoruje 2 ligę, zbyt słaba")
                } else {
                    val leagueInfo = rows[headerRowId + 1].select("tbody")[0].select("td")[1]
                    val leagueName = leagueInfo.text()
                    val leagueLink = leagueInfo.select("a").attr("href")
                    val fullLink = "${transfermarktBaseLink}$leagueLink"
                    this.secondLeague = League(leagueName, 2, fullLink)
                }
            }
        }
        val clubsList = mutableListOf<Team>()
        clubsList.addAll(this.firstLeague?.teams ?: emptyList())
        clubsList.addAll(this.secondLeague?.teams ?: emptyList())
        clubs = clubsList
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
