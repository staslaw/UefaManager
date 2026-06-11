package org.example.init

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.LinkedList


const val countryRank17to21Path = "http://www.90minut.pl/ranking_uefa.php?id_sezon=97"
const val countryRank22to26Path = "http://www.90minut.pl/ranking_uefa.php?id_sezon=107"


fun init() {
    println("PRZYGOTOWANIE GRY ROZPOCZĘTE")
    getFederationsRank()
    println("PRZYGOTOWANIE GRY ZAKOŃCZONE")
}

private fun getFederationsRank() {
    getRanksFromLink(countryRank17to21Path)
    getRanksFromLink(countryRank22to26Path)
    federations.calculateRanking()
}

private fun getRanksFromLink(link: String) {
    val doc = Jsoup.connect(link).get()
    val countryTable = doc.select("table").first()
    val countryRecords = countryTable?.select("tr")
    val seasons = getSeasons(countryTable)
    for (f in federations) {
        val row = countryRecords?.find { it.select("td").any { it.text().contains(f.name) } }
        val cells = row?.select("td")
        for (i in 0..< seasons.size) {
            val cellValue =  cells?.get(i + 2)?.text()
            f.rankingPointsRawMap[seasons[i]] = cellValue?.replace(",", ".")?.toDouble() ?: 0.0
        }
    }
}

private fun getSeasons(countryTable: Element?): LinkedList<String> {
    val seasons = LinkedList<String>()
    countryTable?.select("tr")?.first()?.let { row ->
        val cells = row.select("td")
        for (cell in cells) {
            if (cell.text().startsWith("20")) {
                seasons.add(cell.text())
            }
        }
    }
    return seasons
}
