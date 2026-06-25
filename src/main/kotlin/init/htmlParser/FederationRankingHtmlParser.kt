package org.example.init.htmlParser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class FederationRankingHtmlParser(private val link: String) {
    private val html = getHtmlJsoupDocument()


    private fun getHtmlJsoupDocument(): Document {
        return Jsoup.connect(this.link).get()
    }

    fun getFederationsRankMapFromLink(): HashMap<String, HashMap<String, Double>> {
        val countryTable = html.select("table").first()
        val countryRecords = countryTable?.select("tr") ?: emptyList()
        val seasons = countryRecords[0].select("td").map { it.text() }.toList().subList(2, 7)
        val countryToRanksMap = hashMapOf<String, HashMap<String, Double>>()
        for (i in 1..< countryRecords.size) {
            val cells = countryRecords[i].select("td")
            val name = cells[1].text().trim()
            val ranks = hashMapOf<String, Double>()
            for (i in 0..< seasons.size) {
                ranks[seasons[i]] = cells[i + 2].text().replace(",", ".").toDouble()
            }
            countryToRanksMap[name] = ranks
        }
        return countryToRanksMap
    }
}