package org.example.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.example.init.utils.CalendarSystem
import org.example.init.utils.PRINTING_NAME_TAB
import org.example.init.utils.transfermarktBaseLinkNational


@Entity
class Federation(
    @Id
    var id: Int,
    var name: String,
    var calendarSystem: CalendarSystem,
    var link: String = "$transfermarktBaseLinkNational/$id",
    @Transient
    var campaigns: ArrayList<Campaign> = arrayListOf(),
    @OneToMany(mappedBy = "federation")
    var clubs: MutableSet<Club> = mutableSetOf<Club>(),
    @Transient
    var ranking: UefaRankingPoints = UefaRankingPoints()
) {

    fun addNewSeason() {
        this.ranking.initNewSeason()
        this.clubs.forEach { it.addNewSeason() }
    }

    fun getRankingSummaryLine(season: String): String {
        var line = this.name
        for (i in line.length..< PRINTING_NAME_TAB) line = "$line "
        val pointsLine = this.ranking.getRankingSummaryLine(season)
        return "$line$pointsLine"
    }

    fun printFederationSummary(rankingPosition: Int) {
        println("=====     ${this.name}     =====")
        println("System kalendarza: ${this.calendarSystem}")
        println("Aktualnie na: $rankingPosition miejscu w pięcioletnim rankingu UEFA")
        println("Łącznie klubów: ${clubs.size}")
        this.campaigns.lastOrNull().let { campaignOrNull ->
            campaignOrNull?.let { campaign ->
                campaign.leagues.forEach { league ->
                    println("${league.competitionLevel} liga: ${league.clubs.size}")
                }
            }
        }
    }
}
