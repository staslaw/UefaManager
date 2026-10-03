package org.example.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.example.init.utils.PRINTING_NAME_TAB
import org.example.service.SeasonService


@Entity
class Club(
    @Id
    var id: Int,
    var name: String,
    var link: String,
    var value: String,
    @ManyToOne
    var federation: Federation,
    @ManyToMany
    var leagues: MutableSet<League> = mutableSetOf(),
    @OneToMany(mappedBy = "club")
    val rankingPoints: MutableList<ClubRankingSeasonPoints> = mutableListOf()
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
        val pointsLine = this.rankingPoints.getRankingSummaryLine(season)
        return "$line$pointsLine"
    }

    fun addNewSeason() {
        val lastSeason = rankingPoints.last().season
        val newSeason = SeasonService.getEuropeanSeasons().last()
        if (lastSeason == newSeason) {
            throw Exception("Can not init new season for ClubRankingSeasonPoints class.")
        }
        rankingPoints.add(ClubRankingSeasonPoints(club = this, season = newSeason, seasonRank = 0.0))
    }
}
