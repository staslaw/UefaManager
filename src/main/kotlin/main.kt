package org.example

import org.example.init.addNewSeason
import org.example.init.currentSeason
import org.example.init.federations
import org.example.init.init
import org.example.init.printList
import org.example.init.seasons
import java.util.Scanner

val scan = Scanner(System.`in`)

fun main() {
    println("====================")
    println("====================")
    init()
    println("====================")
    println("ROZPOCZYNAMY GRĘ!!!")
    showMainMenu()
}

private fun showMainMenu() {
    println("====================")
    println("Wybierz opcję i wciśnij 'ENTER':")
    println("1 - wyświetl ranking federacji")
    println("2 - dodaj nowy sezon")
    println("inne - wyjście")
    println("====================")
    val input = scan.nextLine().trim()
    if (input == "1")  {
        showFederationMenu()
    } else if (input == "2") {
        addSeason()
    } else {
        println("KONIEC GRY")
    }
}

private fun showFederationMenu() {
    println("====================")
    println("Aktualny sezon to: $currentSeason")
    println("Podaj sezon dla którego chcesz zobaczyć ranking i wciśnij 'ENTER':")
    println("====================")
    val season = scan.nextLine().trim()
    if (seasons.contains(season)) {
        federations.printList(season)
    } else {
        println("Nie ma takiego sezonu w bazie.")
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