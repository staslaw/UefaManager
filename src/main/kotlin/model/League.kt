package org.example.model

import org.example.init.htmlParser.LeagueHtmlParser


class League(val name: String, val competitionLevel: Int, private val link: String, existingClubs: List<Club>) {
    private val htmlParser = LeagueHtmlParser(this.link, existingClubs)
    var clubs: List<Club> = this.htmlParser.getClubList()
    var leagueTable: LeagueTable? = this.htmlParser.getLeagueTable(this.clubs)

    fun printSummary() {
        println("$competitionLevel LIGA: ${this.name}")
        leagueTable?.printLeagueTable()
    }

}
