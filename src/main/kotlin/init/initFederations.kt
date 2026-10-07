package org.example.init

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.init.htmlParser.FederationHtmlParser
import org.example.init.htmlParser.LeagueHtmlParser
import org.example.init.htmlParser.NationalCupHtmlParser
import org.example.init.utils.CalendarSystem
import org.example.model.Campaign
import org.example.model.Club
import org.example.model.Federation
import org.example.model.League
import org.example.repository.Database
import org.example.repository.FederationRepository
import org.example.repository.CampaignRepository
import org.example.repository.ClubRepository
import org.example.repository.LeagueRepository
import org.example.service.SeasonService


class FedSetup(val name: String, val tmID: Int, val calendarSystem: CalendarSystem)

private val federationSetup = listOf(
    FedSetup("Anglia",              189,    CalendarSystem.EUROPEAN),
    FedSetup("Włochy",              75,     CalendarSystem.EUROPEAN),
    FedSetup("Hiszpania",           157,    CalendarSystem.EUROPEAN),
//    FedSetup("Niemcy",              40,     CalendarSystem.EUROPEAN),
//    FedSetup("Francja",             50,     CalendarSystem.EUROPEAN),
//    FedSetup("Holandia",            122,    CalendarSystem.EUROPEAN),
//    FedSetup("Portugalia",          136,    CalendarSystem.EUROPEAN), // 2 liga nie ma aktualnego sezonu
//    FedSetup("Belgia",              19,     CalendarSystem.EUROPEAN),
//    FedSetup("Czechy",              172,    CalendarSystem.EUROPEAN),
//    FedSetup("Turcja",              174,    CalendarSystem.EUROPEAN),
//    FedSetup("Norwegia",            125,    CalendarSystem.NORTH),
//    FedSetup("Grecja",              56,     CalendarSystem.EUROPEAN),
//    FedSetup("Austria",             127,    CalendarSystem.EUROPEAN),
//    FedSetup("Szkocja",             190,    CalendarSystem.EUROPEAN),
//    FedSetup("Polska",              135,    CalendarSystem.EUROPEAN),
//    FedSetup("Dania",               39,     CalendarSystem.EUROPEAN),
//    FedSetup("Szwajcaria",          148,    CalendarSystem.EUROPEAN),
//    FedSetup("Izrael",              74,     CalendarSystem.EUROPEAN),
//    FedSetup("Cypr",                188,    CalendarSystem.EUROPEAN),
//    FedSetup("Szwecja",             147,    CalendarSystem.NORTH),
//    FedSetup("Chorwacja",           37,     CalendarSystem.EUROPEAN), // 2 liga nie ma aktualnego sezonu
//    FedSetup("Serbia",              215,    CalendarSystem.EUROPEAN), // 2 liga nie ma aktualnego sezonu
//    FedSetup("Ukraina",             177,    CalendarSystem.EUROPEAN),
//    FedSetup("Węgry",               178,    CalendarSystem.EUROPEAN),
//    FedSetup("Rumunia",             140,    CalendarSystem.EUROPEAN),
//    FedSetup("Rosja",               141,    CalendarSystem.NORTH),
//    FedSetup("Słowacja",            154,    CalendarSystem.EUROPEAN),
//    FedSetup("Słowenia",            155,    CalendarSystem.EUROPEAN),
//    FedSetup("Bułgaria",            28,     CalendarSystem.EUROPEAN),
//    FedSetup("Azerbejdżan",         13,     CalendarSystem.EUROPEAN),
//    FedSetup("Irlandia",            72,     CalendarSystem.NORTH),
//    FedSetup("Mołdawia",            112,    CalendarSystem.EUROPEAN),
//    FedSetup("Islandia",            73,     CalendarSystem.NORTH),
//    FedSetup("Bośnia i Hercegowina",24,     CalendarSystem.EUROPEAN),
//    FedSetup("Armenia",             10,     CalendarSystem.EUROPEAN), // 2 liga nie ma aktualnego sezonu
//    FedSetup("Łotwa",               92,     CalendarSystem.NORTH),
//    FedSetup("Kosowo",              244,    CalendarSystem.EUROPEAN),
//    FedSetup("Finlandia",           49,     CalendarSystem.NORTH),
//    FedSetup("Kazachstan",          81,     CalendarSystem.NORTH),
//    FedSetup("Wyspy Owcze",         208,    CalendarSystem.NORTH),
//    FedSetup("Malta",               106,    CalendarSystem.EUROPEAN), // 1 liga nie ma aktualnego sezonu
//    FedSetup("Irlandia Północna",   192,    CalendarSystem.EUROPEAN),
//    FedSetup("Litwa",               98,     CalendarSystem.NORTH),
//    FedSetup("Liechtenstein",       97,     CalendarSystem.EUROPEAN), // 1 i 2 liga nie istnieją
//    FedSetup("Estonia",             47,     CalendarSystem.NORTH),
//    FedSetup("Albania",             3,      CalendarSystem.EUROPEAN),
//    FedSetup("Czarnogóra",          216,    CalendarSystem.EUROPEAN),
//    FedSetup("Luksemburg",          99,     CalendarSystem.EUROPEAN),
//    FedSetup("Walia",               191,    CalendarSystem.EUROPEAN), //brak danych o pucharze
//    FedSetup("Gruzja",              53,     CalendarSystem.NORTH),
//    FedSetup("Macedonia Północna",  100,    CalendarSystem.EUROPEAN),
//    FedSetup("Białoruś",            18,     CalendarSystem.NORTH),
//    FedSetup("Andora",              5,      CalendarSystem.EUROPEAN),
//    FedSetup("Gibraltar",           266,    CalendarSystem.EUROPEAN), // 11 zespołów w lidze
//    FedSetup("San Marino",          144,    CalendarSystem.EUROPEAN)
)


fun initFederations() {
    val federationSetupList = federationSetup

    runBlocking(Dispatchers.IO) {
        federationSetupList.forEach { setup ->
            launch {
                val federation = Federation(setup.tmID, setup.name, setup.calendarSystem)
                Database.transaction { session -> session.persist(federation) }
            }
        }
    }

    runBlocking(Dispatchers.IO) {
        val federations = FederationRepository.getAllFederations()
        federations.forEach { federation ->
            launch {
                val previousSeason = SeasonService.getPreviousSeason(federation.calendarSystem)
                val previousCampaign = Campaign(federation = federation, season = previousSeason)
                Database.transaction { session -> session.persist(previousCampaign) }

                val currentSeason = SeasonService.getCurrentSeason(federation.calendarSystem)
                val currentCampaign = Campaign(federation = federation, season = currentSeason)
                Database.transaction { session -> session.persist(currentCampaign) }
            }
        }
    }

    runBlocking(Dispatchers.IO) {
        val federations = FederationRepository.getAllFederations()
        federations.forEach { federation ->
            val htmlParser = FederationHtmlParser(federation.link, federation.name)
            val campaigns = CampaignRepository.getAllCampaignsForFederation(federation)
            campaigns.forEach { campaign ->
                launch {
                    val year = getYear(campaign.season)
                    val firstLeagueName = htmlParser.getFirstLeagueName()
                    val firstLeagueBaseLink = htmlParser.getFirstLeagueBaseLink()
                    firstLeagueName?.let { name ->
                        firstLeagueBaseLink?.let { link ->
                            val fullLink = "$link/plus/?saison_id=$year"
                            val firstLeague = League(name = name, competitionLevel = 1, link = fullLink, campaign = campaign)
                            Database.transaction { session -> session.persist(firstLeague) }
                        }
                    }

                    val secondLeagueName = htmlParser.getSecondLeagueName()
                    val secondLeagueBaseLink = htmlParser.getSecondLeagueBaseLink()
                    secondLeagueName?.let { name ->
                        secondLeagueBaseLink?.let { link ->
                            val fullLink = "$link/plus/?saison_id=$year"
                            val secondLeague = League(name = name, competitionLevel = 2, link = fullLink, campaign = campaign)
                            Database.transaction { session -> session.persist(secondLeague) }
                        }
                    }
                }
            }
        }
    }

    runBlocking(Dispatchers.IO) {
        val federations = FederationRepository.getAllFederations()
        val leagues = LeagueRepository.getAllLeagues()
        federations.forEach { federation ->
            launch {
                val leaguesForFederation = leagues.filter { it.campaign.federation.name == federation.name }
                leaguesForFederation.forEach { league ->
                    val htmlParser = LeagueHtmlParser(league.campaign.federation, league.link, league)
                    val clubs: List<Club> = htmlParser.getClubList()
                    clubs.forEach { club ->
                        Database.transaction { session -> session.merge(club) }
                    }
                    val leagueTable = htmlParser.getLeagueTable(league, clubs.toSet())
                    leagueTable.forEach { tableRecord ->
                        Database.transaction { session -> session.persist(tableRecord) }
                    }
                }
            }
        }
    }

    runBlocking(Dispatchers.IO) {
        val federations = FederationRepository.getAllFederations()
        federations.forEach { federation ->
            val federationHtmlParser = FederationHtmlParser(federation.link, federation.name)
            val nationalCupHistoryLink = federationHtmlParser.getNationalCupHistoryLink()
            val campaigns = CampaignRepository.getAllCampaignsForFederation(federation)
            campaigns.forEach { campaign ->
                launch {
                    val nationalCupHtmlParser = NationalCupHtmlParser(nationalCupHistoryLink)
                    val cupWinnerId = nationalCupHtmlParser.getNationalCupWinnerClubId(campaign.season, federation.calendarSystem)
                    cupWinnerId?.let {
                        val cupWinner = ClubRepository.getClubById(it)
                        campaign.cupWinner = cupWinner
                        Database.transaction { session -> session.merge(campaign) }
                    }
                }
            }
        }
    }
// sprawdzenie czy liga się zmieniła pomiędzy sezonami
}

private fun getYear(season: String): String {
    return if (season.contains("/")) {
        season.split("/")[0]
    } else {
        (season.toInt() - 1).toString()
    }
}
