package org.example

import org.example.init.initRankings
import org.example.service.UIService
import java.util.Scanner


val scan = Scanner(System.`in`)
val uiService = UIService()


fun main() {
    initRankings()
    println("PRZYGOTOWANIE GRY ZAKOŃCZONE")
    println("====================")
    println("ROZPOCZYNAMY GRĘ!!!")
    uiService.printCurrentSeason()
    showMainMenu()
}

private fun showMainMenu() {
    println("====================")
    println("Wybierz opcję i wciśnij 'ENTER':")
    println("1 - wyświetl rozgrywki krajowe")
    println("2 - wyświetl ranking federacji")
    println("3 - wyświetl ranking klubowy")
    println("4 - dodaj nowy sezon")
    println("inne - wyjście")
    when(scan.nextLine().trim()) {
        "1" -> showFederations()
        "2" -> showFederationRanking()
        "3" -> showClubRanking()
        "4" -> addSeason()
        else -> println("KONIEC GRY")
    }
}

private fun showFederations() {
    println("====================")
    uiService.printFederations()
    println("====================")
    println("Podaj nazwę federacji którą chcesz zobaczyć i wciśnij 'ENTER'")
    uiService.printFederationSummary(scan.nextLine().trim())
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
