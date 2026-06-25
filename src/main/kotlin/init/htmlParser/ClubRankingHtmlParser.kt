package org.example.init.htmlParser

import org.example.init.utils.getHtmlJsoupDocument


class ClubRankingHtmlParser(private val link: String) {
    private val html = getHtmlJsoupDocument(link)


    fun getCountryToClubsToRankMapFromLink(): HashMap<String, HashMap<String, HashMap<String, Double>>> {
        val rows = html.select("tbody").first()!!.select("tr")
        val seasons = rows[0].select("td").map { it.text() }.toList().subList(3, 8)
        val countryToClubsToRankMap = hashMapOf<String, HashMap<String, HashMap<String, Double>>>()
        for (i in 1..< rows.size) {
            val cells = rows[i].select("td")
            val country = cells[2].select("img")[0].attr("title")
            val club = cells[1].text()
            val ranks = hashMapOf<String, Double>()
            for (i in 0..< seasons.size) {
                ranks[seasons[i]] = cells[i + 3].text().replace(",", ".").toDouble()
            }
            countryToClubsToRankMap.computeIfAbsent(country) { hashMapOf() }[club] = ranks
        }
        return countryToClubsToRankMap
    }
}