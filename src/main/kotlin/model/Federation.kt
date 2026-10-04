package org.example.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.example.init.utils.CalendarSystem
import org.example.init.utils.PRINTING_NAME_TAB
import org.example.init.utils.transfermarktBaseLinkNational
import org.example.service.SeasonService


@Entity
class Federation(
    @Id
    var id: Int,
    var name: String,
    var calendarSystem: CalendarSystem,
    var link: String = "$transfermarktBaseLinkNational/$id",
    @OneToMany(mappedBy = "federation")
    var campaigns: MutableSet<Campaign> = mutableSetOf(),
    @OneToMany(mappedBy = "federation")
    val rankingPoints: MutableList<FederationRankingSeasonPoints> = mutableListOf(),
) {

    fun addNewSeason() {
        val lastSeason = rankingPoints.last().season
        val newSeason = SeasonService.getEuropeanSeasons().last()
        if (lastSeason == newSeason) {
            throw Exception("Can not init new season for FederationRankingSeasonPoints class.")
        }
        rankingPoints.add(FederationRankingSeasonPoints(federation = this, season = newSeason, seasonRank = 0.0))
//        this.clubs.forEach { it.addNewSeason() }
    }

    fun getRankSummaryLine(season: String): String {
        var line = this.name
        for (i in line.length..< PRINTING_NAME_TAB) line = "$line "
        val pointsLine = this.rankingPoints.getRankingSummaryLine(season)
        return "$line$pointsLine"
    }

    fun printFederationSummary(rankingPosition: Int) {
        println("=====     ${this.name}     =====")
        println("System kalendarza: ${this.calendarSystem}")
        println("Aktualnie na: $rankingPosition miejscu w pięcioletnim rankingu UEFA")
        this.campaigns.lastOrNull().let { campaignOrNull ->
            campaignOrNull?.let { campaign ->
                campaign.leagues.forEach { league ->
                    println("${league.competitionLevel} liga: ${league.name}")
                }
            }
        }
    }
}
