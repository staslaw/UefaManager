package org.example.init.htmlParser

import org.example.init.utils.getHtmlJsoupDocument
import org.example.model.League
import org.example.init.utils.transfermarktBaseLink
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements


class FederationHtmlParser(private val link: String, private val federationName: String) {
    private val html: Document = getHtmlJsoupDocument(link)
    private val leagueTableRows: Elements? = getLeagueTableRows()
    private val firstLeague: League? = parseHtmlToFirstLeague()
    private val secondLeague: League? = parseHtmlToSecondLeague()


    private fun getLeagueTableRows(): Elements? {
        return html.getElementById("yw1")?.let { div ->
            val table = div.getElementsByClass("items")[0]
            val tbody = table.select("tbody")[0]
            tbody.select("tr")
        }
    }

    private fun parseHtmlToFirstLeague(): League? {
        if (!html.toString().contains("1.liga")) return null
        if (leagueTableRows == null) return null
        val leagueInfo = if (this.federationName == "Malta") {
            leagueTableRows.select("td").first { it.text() == "Premier League Closing Round" }
        } else {
            leagueTableRows[1].select("tbody")[0].select("td")[1]
        }
        return getLeagueFromLeagueInfoRow(leagueInfo, 1)
    }

    private fun parseHtmlToSecondLeague(): League? {
        if (!html.toString().contains("2.liga")) return null
        if (leagueTableRows == null) return null

        val headerRow = leagueTableRows.find { it.select("td")[0].text() == "2.liga" }
        val headerRowId = leagueTableRows.indexOf(headerRow)
        for (r in headerRowId + 1..< leagueTableRows.size) {
            if (leagueTableRows[r].getElementsByClass("extrarow bg_blau_20 hauptlink").isNotEmpty()) {
                val nextHeaderRowId = leagueTableRows.indexOf(leagueTableRows[r])
                if (nextHeaderRowId - headerRowId > 3) {
                    println("---   ignoruje 2 ligę, są dwie")
                    return null
                } else {
                    break
                }
            }
        }

        val leagueValue = leagueTableRows[headerRowId + 1].getElementsByClass("rechts hauptlink").text()
        if (leagueValue == "-" || leagueValue == "" || leagueValue.contains("tys")) {
            println("---   ignoruje 2 ligę, zbyt słaba")
            return null
        } else {
            val leagueInfo = leagueTableRows[headerRowId + 1].select("tbody")[0].select("td")[1]
            return getLeagueFromLeagueInfoRow(leagueInfo, 2)
        }
    }

    private fun getLeagueFromLeagueInfoRow(leagueInfo: Element, competitionLevel: Int): League {
        val leagueName = leagueInfo.text()
        val leagueLink = leagueInfo.select("a").attr("href")
        val fullLink = "${transfermarktBaseLink}$leagueLink"
        return League(leagueName, competitionLevel, fullLink)
    }

    fun getFirstLeague() = this.firstLeague
    fun getSecondLeague() = this.secondLeague
}