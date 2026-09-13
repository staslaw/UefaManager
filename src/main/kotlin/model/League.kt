package org.example.model

import org.example.init.htmlParser.LeagueHtmlParser


class League(val federation: Federation, val name: String, val competitionLevel: Int, private val link: String, existingClubs: Set<Club>) {
    private val htmlParser = LeagueHtmlParser(this.federation, this.link, existingClubs)
    var clubs: List<Club> = this.htmlParser.getClubList()
    var leagueTable: LeagueTable? = this.htmlParser.getLeagueTable(this.clubs)

    fun printSummary() {
        println("$competitionLevel LIGA: ${this.name}")
        leagueTable?.printLeagueTable()
    }

}
