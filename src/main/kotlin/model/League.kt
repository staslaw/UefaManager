package org.example.model

import org.example.init.htmlParser.LeagueHtmlParser


class League(val name: String, val competitionLevel: Int, private val link: String) {
    private val htmlParser = LeagueHtmlParser(this.link)
    val clubs: List<Club> = this.htmlParser.getClubList()

}
