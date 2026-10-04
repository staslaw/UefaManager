package org.example.service

import org.example.init.utils.PRINTING_ID_TAB
import org.example.model.Federation
import org.example.model.getCurrentSeasonPoints
import org.example.model.getFiveYearsRanking
import org.example.model.getPreviousSeasonPoints
import org.example.repository.CampaignRepository
import org.example.repository.CampaignRepository.getCampaignWithLeaguesForFederationAndSeason
import org.example.repository.FederationRepository
import org.example.repository.FederationRepository.getAllFederationsWithRanks
import org.example.repository.LeagueRepository
import org.example.service.SeasonService.Companion.getEuropeanCurrentSeason


class FederationService() {

    fun printFederations() {
        val federations = FederationRepository.getAllFederations()
        federations.forEach { println(it.name) }
    }

    fun printFederationSummary(federationName: String): String {
        val sortedFederations = getFederationsSortedByRank(getEuropeanCurrentSeason())
        sortedFederations.find { it.name.contains(federationName) }
            ?.let { federation ->
                val campaigns = getCampaignWithLeaguesForFederationAndSeason(federation)
                federation.campaigns = campaigns.toMutableSet()
                val rankingPosition = sortedFederations.indexOf(federation) + 1
                federation.printFederationSummary(rankingPosition)
                return federationName
            }
        println("Nie ma takiej federacji w bazie.")
        return ""
    }

    fun printFederationRanking(season: String) {
        println(getRankingHeader(season))
        val sortedFederations = getFederationsSortedByRank(season)
        for (i in 1..sortedFederations.size) {
            val federationString = sortedFederations[i - 1].getRankSummaryLine(season)
            var line = "$i."
            for (i in line.length..< PRINTING_ID_TAB) line = "$line "
            println("$line$federationString")
        }
    }

    private fun getFederationsSortedByRank(season: String): List<Federation> {
        val federationsWithRanks = getAllFederationsWithRanks()
        return ArrayList(federationsWithRanks)
            .sortedWith(compareByDescending<Federation> { it.rankingPoints.getFiveYearsRanking(season) }
                .thenByDescending { it.rankingPoints.getCurrentSeasonPoints(season) }
                .thenByDescending { it.rankingPoints.getPreviousSeasonPoints(season) }
            )
    }

    fun addNewSeason() {
        val federations = FederationRepository.getAllFederations()
        federations.forEach { it.addNewSeason() }
    }

    fun getAvailableSeasonsForFederation(federationName: String): List<String> {
        return FederationRepository.getAllFederations()
            .find { it.name == federationName }
            ?.let { federation ->
                val campaignsForFederation = CampaignRepository.getAllCampaignsForFederation(federation)
                campaignsForFederation.map { it.season }
            } ?: emptyList()
    }

    fun getAvailableLeagues(chosenSeason: String, federationName: String): List<String> {
        return FederationRepository.getAllFederations()
            .find { it.name == federationName }?.let { federation ->
                CampaignRepository.getAllCampaignsForFederation(federation)
                    .find { it.season == chosenSeason }?.let { campaign ->
                        LeagueRepository.getLeaguesForCampaign(campaign).map { it.name }
                    }
            } ?: emptyList()
    }

    fun printLeagueSummary(federationName: String, chosenLeague: String, chosenSeason: String) {
        FederationRepository.getAllFederations()
            .find { it.name == federationName }?.let { federation ->
                CampaignRepository.getAllCampaignsForFederation(federation)
                    .find { it.season == chosenSeason }?.let { campaign ->
                        LeagueRepository.getLeaguesForCampaign(campaign)
                            .find { it.name == chosenLeague }?.printSummary()
                    }
            }
    }
}