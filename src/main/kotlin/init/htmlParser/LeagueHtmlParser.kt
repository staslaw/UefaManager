package org.example.init.htmlParser

import org.example.init.utils.getHtmlJsoupDocument
import org.example.model.Club
import org.example.init.utils.transfermarktBaseLink


class LeagueHtmlParser(private val link: String) {
    private val html = getHtmlJsoupDocument(link)
    private val clubs: List<Club> = parseHtmlToClubList()


    private fun parseHtmlToClubList(): List<Club> {
        val div = this.html.getElementById("yw1")
        val teamRows = div!!.select("tbody")[0].select("tr")
        return teamRows.map {
            val cells = it.select("td")
            val name = cells[0].select("a").attr("title")
            val link = cells[0].select("a").attr("href")
            val fullLink = "${transfermarktBaseLink}$link"
            val value = cells.last()!!.select("a").text()
            Club(name, fullLink, value)
        }
    }

    fun getClubList() = this.clubs
}
