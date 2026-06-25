package org.example.init.model

import org.example.init.transfermarktBaseLink
import org.jsoup.Jsoup


class League(val name: String, val competitionLevel: Int, val link: String) {
    val teams: List<Team> = initTeams()

    private fun initTeams(): List<Team> {
        println("---   init league: ${this.name} (level $competitionLevel)")
        val doc = Jsoup.connect(this.link).get()
        val div = doc.getElementById("yw1")
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
}
