package org.example.model

import org.example.init.htmlParser.FederationHtmlParser
import org.example.init.utils.CalendarSystem
import org.example.init.utils.PRINTING_NAME_TAB
import org.example.service.SeasonService


class Federation(val name: String, val link: String, val calendarSystem: CalendarSystem) {
    val campaigns: ArrayList<Campaign>
    val clubs: List<Club>
    val ranking: UefaRankingPoints = UefaRankingPoints()

    init {
        val htmlParser = FederationHtmlParser(this.link, this.name)
        val clubsList = mutableListOf<Club>()
        val previousCampaign = Campaign(SeasonService.getPreviousSeason(this.calendarSystem), htmlParser, clubsList)
        previousCampaign.leagues.forEach { clubsList.addAll(it.clubs) }
        val currentCampaign = Campaign(SeasonService.getCurrentSeason(this.calendarSystem), htmlParser, clubsList)
        clubsList.clear()
        currentCampaign.leagues.forEach { clubsList.addAll(it.clubs) }
        this.clubs = clubsList
        this.campaigns = arrayListOf(previousCampaign, currentCampaign)

        currentCampaign.leagues.firstOrNull()?.let { currentFirstLeague ->
            currentCampaign.leagues.getOrNull(1)?.let { currentSecondLeague ->
                if (currentFirstLeague.clubs.containsAll(previousCampaign.leagues.first().clubs)) {
                    println("---   ${this.name} - 1 liga nie zmieniła się pomiędzy sezonami")
                }
                if (currentSecondLeague.clubs.containsAll(previousCampaign.leagues[1].clubs)) {
                    println("---   ${this.name} - 2 liga nie zmieniła się pomiędzy sezonami")
                    val c = previousCampaign.leagues.first().clubs.plus(previousCampaign.leagues[1].clubs).minus(currentFirstLeague.clubs.toSet())
                    currentSecondLeague.clubs = c
                    println("---   ${this.name} - 2 liga została skompletowana dla aktualnego sezonu")
                }
            }
        }
    }

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
