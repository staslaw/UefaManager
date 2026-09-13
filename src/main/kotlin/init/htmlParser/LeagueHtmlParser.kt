package org.example.init.htmlParser

import org.example.init.utils.getHtmlJsoupDocument
import org.example.model.Club
import org.example.init.utils.transfermarktBaseLink
import org.example.model.Federation
import org.example.model.LeagueTable
import org.example.model.LeagueTableRecord
import org.example.repository.Database


class LeagueHtmlParser(val federation: Federation, link: String, val existingClubs: Set<Club>) {
    private val html = getHtmlJsoupDocument(link)
    private val clubs: List<Club> = parseHtmlToClubList()

    private fun parseHtmlToClubList(): List<Club> {
        val div = this.html.getElementById("yw1")
        val teamRows = div!!.select("tbody")[0].select("tr")
        return teamRows.map {
            val cells = it.select("td")
            val name = cells[0].select("a").attr("title")
            val link = cells[0].select("a").attr("href")
            val id = link.split("startseite/verein/").last().split("/").first().toInt()
            val shortLink = link.substringBeforeLast("/")
            val existingClubList = existingClubs.filter { club ->
                val exFullLink = club.link.substringBeforeLast("/")
                exFullLink.contains(shortLink)
            }
            if (existingClubList.size == 1) {
                val club = existingClubList.first()
                club.link = link
                club
            } else {
                val fullLink = "${transfermarktBaseLink}$link"
                val value = cells.last()!!.select("a").text()
                val club = Club(id, name, fullLink, value, federation)
                Database.transaction { session -> session.persist(club) }
                club
            }
        }
    }

    fun getClubList() = this.clubs

    fun getLeagueTable(existingClubs: List<Club>): LeagueTable {
        val div = html.getElementsByClass("content-box-headline ").find { it.text().contains("Tabela") }?.parent()
        val rows = div!!.select("table").first()!!.select("tr")
        val leagueTable = mutableListOf<LeagueTableRecord>()
        for (i in 1..< rows.size) {
            val cells = rows[i].select("td")
            val club = existingClubs.find { it.name == cells[2].select("a").attr("title").trim() }
            val matches = cells[3].text().toInt()
            val goals = cells[4].text().toInt()
            val points = cells[5].text().removeSuffix("*").toInt()
            val record = LeagueTableRecord(club, matches, goals, points)
            leagueTable.add(record)
        }
        return LeagueTable(leagueTable)
    }
}
