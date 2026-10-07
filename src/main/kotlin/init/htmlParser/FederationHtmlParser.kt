package org.example.init.htmlParser

import org.example.init.exception.NationalCupNotExist
import org.example.init.utils.getHtmlJsoupDocument
import org.example.init.utils.transfermarktBaseLink
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


class FederationHtmlParser(private val link: String, private val federationName: String) {
    private val html: Document = getHtmlJsoupDocument(link)
    private val leagueTableRows: Elements? = getLeagueTableRows()
    private var secondLeagueTableRows: Elements? = null
    private var firstLeagueName: String? = null
    private var secondLeagueName: String? = null
    private var firstLeagueBaseLink: String? = null
    private var secondLeagueBaseLink: String? = null
    private var nationalCupHistoryLink: String? = null

    init {
        parseHtmlToFirstLeagueInfo()
        parseHtmlToSecondLeagueInfo()
        parseHtmlNationalCupBaseLink(this.leagueTableRows)
    }

    private fun getLeagueTableRows(isBaseTable: Boolean? = true): Elements? {
        val tableSelector = if (isBaseTable == true) "yw1" else "yw2"
        return this.html.getElementById(tableSelector)?.let { div ->
            val table = div.getElementsByClass("items")[0]
            val tbody = table.select("tbody")[0]
            tbody.select("tr")
        }
    }

    private fun parseHtmlToFirstLeagueInfo() {
        if (!this.html.toString().contains("1.liga")) {
            println("---   ${this.federationName} - ignoruje 1 ligę, nie istnieje")
            return
        }
        if (this.leagueTableRows == null) return
        val leagueInfo = this.leagueTableRows[1].select("tbody")[0].select("td")[1]
        this.firstLeagueName = leagueInfo.text()
        this.firstLeagueBaseLink = leagueInfo.select("a").attr("href")
    }

    private fun parseHtmlToSecondLeagueInfo() {
        if (!this.html.toString().contains("2.liga")) {
            println("---   ${this.federationName} - ignoruje 2 ligę, nie istnieje")
            return
        }
        if (this.leagueTableRows == null) return

        val headerRow = this.leagueTableRows.find { it.select("td")[0].text() == "2.liga" }
        val headerRowId = this.leagueTableRows.indexOf(headerRow)
        for (r in headerRowId + 1..< this.leagueTableRows.size) {
            if (this.leagueTableRows[r].getElementsByClass("extrarow bg_blau_20 hauptlink").isNotEmpty()) {
                val nextHeaderRowId = this.leagueTableRows.indexOf(this.leagueTableRows[r])
                if (nextHeaderRowId - headerRowId > 3) {
                    println("---   ${this.federationName} - ignoruje 2 ligę, są dwie")
                    return
                } else {
                    break
                }
            }
        }

        val leagueValue = this.leagueTableRows[headerRowId + 1].getElementsByClass("rechts hauptlink").text()
        if (leagueValue == "-" || leagueValue == "" || leagueValue.contains("tys")) {
            println("---   ${this.federationName} - ignoruje 2 ligę, zbyt słaba")
            return
        } else {
            val leagueInfo = leagueTableRows[headerRowId + 1].select("tbody")[0].select("td")[1]
            this.secondLeagueName = leagueInfo.text()
            this.secondLeagueBaseLink = leagueInfo.select("a").attr("href")
        }
    }

    private fun parseHtmlNationalCupBaseLink(leagueTable: Elements?) {
        if (!this.html.toString().contains("Puchar krajowy")) {
            throw NationalCupNotExist("Puchar nie istnieje dla: $federationName")
        }
        val headerRow = leagueTable!!.find { it.select("td")[0].text() == "Puchar krajowy" }
        if (headerRow != null) {
            val nationalCupHeaderRowId = leagueTable.indexOf(headerRow)
            val modifier = if (federationName == "Malta") 5 else 1
            val nationalCupRow = leagueTable[nationalCupHeaderRowId + modifier]
            val nationalCupInfo = nationalCupRow.select("tbody")[0].select("td")[1]
            var link = nationalCupInfo.select("a").attr("href")
            link = link.replace("wettbewerb", "pokalwettbewerb")
            this.nationalCupHistoryLink = link.replace("startseite", "erfolge")
        } else {
            this.secondLeagueTableRows = getLeagueTableRows(false)
            parseHtmlNationalCupBaseLink(this.secondLeagueTableRows)
        }
    }

    fun getFirstLeagueName(): String? {
        return this.firstLeagueName
    }

    fun getFirstLeagueBaseLink(): String? {
        return this.firstLeagueBaseLink?.let { "${transfermarktBaseLink}${it}" }
    }

    fun getSecondLeagueName(): String? {
        return this.secondLeagueName
    }

    fun getSecondLeagueBaseLink(): String? {
        return this.secondLeagueBaseLink?.let { "${transfermarktBaseLink}${it}" }
    }

    fun getNationalCupHistoryLink(): String? {
        return this.nationalCupHistoryLink?.let { "${transfermarktBaseLink}${it}" }
    }
}
