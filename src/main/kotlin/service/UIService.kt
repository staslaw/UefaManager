package org.example.service

import org.example.init.addNewSeason
import org.example.init.currentSeason


class UIService() {
    private val federationService = FederationService()
    private val clubService = ClubService()

    fun printCurrentSeason() {
        println("Aktualny sezon to: $currentSeason")
    }

    fun addSeason() {
        addNewSeason()
    }

    fun printFederations() {
        federationService.printFederations()
    }

    fun printFederationSummary(federationName: String) {
        federationService.printFederationSummary(federationName)
    }

    fun printFederationRanking(season: String) {
        federationService.printFederationRanking(season)
    }

    fun printClubRanking() {
        clubService.printClubRanking()
    }
}