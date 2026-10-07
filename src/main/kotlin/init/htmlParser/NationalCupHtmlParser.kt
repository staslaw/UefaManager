package org.example.init.htmlParser

import org.example.init.utils.CalendarSystem
import org.example.init.utils.getHtmlJsoupDocument
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


class NationalCupHtmlParser(val link: String?) {
    private val html: Document
    private val mainTableRows: Elements?

    init {
        if (link == null) {
            throw NullPointerException("link is null for NationalCupHtmlParser")
        } else {
            this.html = getHtmlJsoupDocument(link)
            this.mainTableRows = getMainTableRows()
        }
    }

    private fun getMainTableRows(): Elements? {
        val div = this.html.getElementById("yw1") ?: return null
        val tables = div.getElementsByClass("items")
        if (tables.isEmpty()) return null
        val tbodys = tables[0].select("tbody")
        if (tbodys.isEmpty()) return null
        return tbodys[0].select("tr")
    }

    fun getNationalCupWinnerClubId(season: String, calendarSystem: CalendarSystem): Int? {
        var shortSeason = if (season.contains("/")) { season.replace("20","") } else { season }
        val winnerLine = this.mainTableRows?.find { it.select("td")[0].text() == shortSeason }
        var winnerId: Int? = null
        if (winnerLine != null) {
            val winnerLink = winnerLine.select("td")[1].select("a").attr("href")
            winnerId = winnerLink.split("verein/").last().split("/").first().toInt()
        } else if (calendarSystem == CalendarSystem.NORTH) {
            shortSeason = season.replace("20","")
            val winnerLine = this.mainTableRows?.find { it.select("td")[0].text().endsWith(shortSeason) }
            if (winnerLine != null) {
                val winnerLink = winnerLine.select("td")[1].select("a").attr("href")
                winnerId = winnerLink.split("verein/").last().split("/").first().toInt()
            }
        }
        return winnerId
    }
}