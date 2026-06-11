package org.example

import org.example.init.addNewSeason
import org.example.init.currentSeason
import org.example.init.federations
import org.example.init.init
import org.example.init.printRankingList
import org.example.init.seasons
import java.util.Scanner

val scan = Scanner(System.`in`)

fun main() {
    println("====================")
    println("====================")
    init()
    println("====================")
    println("ROZPOCZYNAMY GRĘ!!!")
    println("Aktualny sezon to: $currentSeason")
    showMainMenu()
}

private fun showMainMenu() {
    println("====================")
    println("Wybierz opcję i wciśnij 'ENTER':")
    println("1 - wyświetl ranking federacji")
    println("2 - wyświetl listę federacji")
    println("3 - dodaj nowy sezon")
    println("inne - wyjście")
    println("====================")
    val option = scan.nextLine().trim()
    if (option == "1")  {
        showFederationRanking()
    } else if (option == "2") {
        showFederations()
    } else if (option == "3") {
        addSeason()
    } else {
        println("KONIEC GRY")
    }
}

private fun showFederationRanking() {
    println("====================")
    println("Podaj sezon dla którego chcesz zobaczyć ranking i wciśnij 'ENTER'")
    println("====================")
    val season = scan.nextLine().trim()
    if (seasons.contains(season)) {
        federations.printRankingList(season)
    } else {
        println("Nie ma takiego sezonu w bazie.")
    }
    showMainMenu()
}

private fun showFederations() {
    federations.forEach { println(it.name) }
    println("====================")
    println("Podaj nazwę federacji którą chcesz zobaczyć i wciśnij 'ENTER'")
    println("====================")
    val federationName = scan.nextLine().trim()
    if (federations.map { it.name }.toList().contains(federationName)) {
        val federation = federations.first { it.name == federationName }
        federation.printFederationSummary()
    } else {
        println("Nie ma takiej federacji w bazie.")
    }
    showMainMenu()
}

private fun addSeason() {
    addNewSeason()
    println("====================")
    println("Dodano sezon: $currentSeason")
    println("====================")
    showMainMenu()
}
