package org.example.service


class UIService() {
    private val federationService = FederationService()
    private val clubService = ClubService()
    private val seasonService = SeasonService()


    fun printCurrentSeason() {
        seasonService.printCurrentSeason()
    }

    fun addSeason() {
        seasonService.addNewSeason()
        federationService.addNewSeason()
    }

    fun printFederations() {
        federationService.printFederations()
    }

    fun printFederationSummary(federationName: String): String {
        return federationService.printFederationSummary(federationName)
    }

    fun printAvailableSeasonsForRanking() {
        seasonService.printAllSeasons()
    }

    fun printFederationRanking(season: String) {
        if (seasonService.checkIfSeasonExists(season)) {
            federationService.printFederationRanking(season)
        } else {
            println("Nie ma takiego sezonu w bazie.")
        }
    }

    fun printClubRanking(season: String) {
        if (seasonService.checkIfSeasonExists(season)) {
            clubService.printClubRanking(season)
        } else {
            println("Nie ma takiego sezonu w bazie.")
        }
    }

    fun getAvailableSeasonsForFederation(federationName: String): List<String> {
        return federationService.getAvailableSeasonsForFederation(federationName)
    }

    fun getAvailableLeagues(chosenSeason: String, federationName: String): List<String> {
        return federationService.getAvailableLeagues(chosenSeason, federationName)
    }

    fun printLeagueSummary(federationName: String, chosenLeague: String, chosenSeason: String) {
        return federationService.printLeagueSummary(federationName, chosenLeague, chosenSeason)
    }
}