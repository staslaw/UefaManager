package org.example

import org.example.init.initFederations
import org.example.init.initRankings
import org.example.service.UIService
import java.util.Scanner


val scan = Scanner(System.`in`)
lateinit var uiService: UIService


fun main() {
    initFederations()
    initRankings()
    println("PRZYGOTOWANIE GRY ZAKOŃCZONE")
    println("====================")
    println("ROZPOCZYNAMY GRĘ!!!")
    uiService = UIService()
    uiService.printCurrentSeason()
    showMainMenu()
}

private fun showMainMenu() {
    println("====================")
    println("Wybierz opcję i wciśnij 'ENTER':")
    println("1 - wyświetl rozgrywki krajowe")
    println("2 - wyświetl ranking federacji")
    println("3 - wyświetl ranking klubowy")
//    println("4 - dodaj nowy sezon")
    println("inne - wyjście")
    when(scan.nextLine().trim()) {
        "1" -> showFederations()
        "2" -> showFederationRanking()
        "3" -> showClubRanking()
//        "4" -> addSeason()
        else -> println("KONIEC GRY")
    }
}

private fun showFederations() {
    println("====================")
    uiService.printFederations()
    println("====================")
    println("Podaj nazwę federacji którą chcesz zobaczyć i wciśnij 'ENTER'")
    val federationName = uiService.printFederationSummary(scan.nextLine().trim())
    if (federationName.isEmpty()) {
        showMainMenu()
    } else {
        showFederationMenu(federationName)
    }
}

private fun showFederationMenu(federationName: String) {
    println("====================")
    val seasons = uiService.getAvailableSeasonsForFederation(federationName)
    println(seasons)
    println("Wybierz dostępny sezon i wciśnij 'ENTER':")
    val chosenSeason = scan.nextLine().trim()
    if (seasons.contains(chosenSeason)) {
        showSeasonMenu(chosenSeason, federationName)
    } else {
        println("Ten sezon nie jest dostępny")
        showMainMenu()
    }
}

private fun showSeasonMenu(chosenSeason: String, federationName: String) {
    println("====================")
    val leagues = uiService.getAvailableLeagues(chosenSeason, federationName)
    println(leagues)
    println("Wybierz dostępną ligę i wciśnij 'ENTER':")
    val chosenLeague = scan.nextLine().trim()
    if (leagues.contains(chosenLeague)) {
        uiService.printLeagueSummary(federationName, chosenLeague, chosenSeason)
    } else {
        println("Ta liga nie jest dostępna")
    }
    showMainMenu()
}

private fun showFederationRanking() {
    println("====================")
    println("Podaj sezon dla którego chcesz zobaczyć ranking i wciśnij 'ENTER'. Dostępne sezony:")
    uiService.printAvailableSeasonsForRanking()
    uiService.printFederationRanking(scan.nextLine().trim())
    showMainMenu()
}

private fun showClubRanking() {
    println("====================")
    println("Podaj sezon dla którego chcesz zobaczyć ranking i wciśnij 'ENTER'. Dostępne sezony:")
    uiService.printAvailableSeasonsForRanking()
    uiService.printClubRanking(scan.nextLine().trim())
    showMainMenu()
}

private fun addSeason() {
    uiService.addSeason()
    println("====================")
    uiService.printCurrentSeason()
    showMainMenu()
}
