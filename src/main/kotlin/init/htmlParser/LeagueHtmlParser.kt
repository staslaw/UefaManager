package org.example.init.htmlParser

import org.example.init.model.Team
import org.example.init.transfermarktBaseLink
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class LeagueHtmlParser(private val link: String) {
    private val html = getHtmlJsoupDocument()
    private val teams: List<Team> = parseHtmlToTeamList()


    private fun getHtmlJsoupDocument(): Document {
        return Jsoup.connect(this.link).get()
    }

    private fun parseHtmlToTeamList(): List<Team> {
        val div = this.html.getElementById("yw1")
        val teamRows = div!!.select("tbody")[0].select("tr")
        return teamRows.map {
            val cells = it.select("td")
            val name = cells[0].select("a").attr("title")
            val link = cells[0].select("a").attr("href")
            val fullLink = "${transfermarktBaseLink}$link"
            val value = cells.last()!!.select("a").text()
            Team(name, fullLink, value)
        }
    }

    fun getTeamList() = this.teams
}
