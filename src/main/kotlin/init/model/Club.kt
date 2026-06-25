package org.example.init.model

import org.example.init.PRINTING_NAME_TAB


class Club(val name: String, val link: String, val value: String) {
    val ranking: UefaRankingPoints = UefaRankingPoints()


    fun getNameWithTab(): String {
        var line = this.name
        for (i in line.length..< PRINTING_NAME_TAB) line = "$line "
        return line
    }

    fun getClubInfoLine(): String {
        return "${this.getNameWithTab()} ${this.value}"
    }

    fun rankSummary(): String {
        val line = this.getNameWithTab()
        val pointsLine = this.ranking.getRankingSummaryLine()
        return "$line$pointsLine"
    }

    fun addNewSeason() {
        this.ranking.initNewSeason()
    }
}
