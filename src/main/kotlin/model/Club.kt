package org.example.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.example.init.utils.PRINTING_NAME_TAB


@Entity
class Club(
    @Id
    var id: Int,
    var name: String,
    var link: String,
    var value: String,
    @ManyToOne
    var federation: Federation,
    @Transient
    var ranking: UefaRankingPoints = UefaRankingPoints()
) {

    fun getNameWithTab(): String {
        var line = this.name
        for (i in line.length..< PRINTING_NAME_TAB) line = "$line "
        return line
    }

    fun getClubInfoLine(): String {
        return "${this.getNameWithTab()} ${this.value}"
    }

    fun rankSummary(season: String): String {
        val line = this.getNameWithTab()
        val pointsLine = this.ranking.getRankingSummaryLine(season)
        return "$line$pointsLine"
    }

    fun addNewSeason() {
        this.ranking.initNewSeason()
    }
}
