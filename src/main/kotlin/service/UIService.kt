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

    fun printFederationSummary(federationName: String) {
        federationService.printFederationSummary(federationName)
    }

    fun printFederationRanking(season: String) {
        if (seasonService.checkIfSeasonExists(season)) {
            federationService.printFederationRanking(season)
        } else {
            println("Nie ma takiego sezonu w bazie.")
        }
    }

    fun printClubRanking() {
        clubService.printClubRanking()
    }
}