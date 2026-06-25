package org.example.init.model

import org.example.init.htmlParser.LeagueHtmlParser


class League(val name: String, val competitionLevel: Int, private val link: String) {
    private val htmlParser = LeagueHtmlParser(this.link)
    val teams: List<Team> = this.htmlParser.getTeamList()

}
