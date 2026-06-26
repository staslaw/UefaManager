package org.example.init

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.init.utils.CalendarSystem
import org.example.init.utils.transfermarktBaseLinkNational
import org.example.model.Federation

class FedSetup(val name: String, val tmID: Int, val calendarSystem: CalendarSystem)

private val federationSetup = listOf(
    FedSetup("Anglia",              189,    CalendarSystem.EUROPEAN),
    FedSetup("Włochy",              75,     CalendarSystem.EUROPEAN),
    FedSetup("Hiszpania",           157,    CalendarSystem.EUROPEAN),
    FedSetup("Niemcy",              40,     CalendarSystem.EUROPEAN),
    FedSetup("Francja",             50,     CalendarSystem.EUROPEAN),
    FedSetup("Holandia",            122,    CalendarSystem.EUROPEAN),
    FedSetup("Portugalia",          136,    CalendarSystem.EUROPEAN), // 2 liga nie ma aktualnego sezonu
    FedSetup("Belgia",              19,     CalendarSystem.EUROPEAN),
    FedSetup("Czechy",              172,    CalendarSystem.EUROPEAN),
    FedSetup("Turcja",              174,    CalendarSystem.EUROPEAN),
    FedSetup("Norwegia",            125,    CalendarSystem.NORTH),
    FedSetup("Grecja",              56,     CalendarSystem.EUROPEAN),
    FedSetup("Austria",             127,    CalendarSystem.EUROPEAN),
    FedSetup("Szkocja",             190,    CalendarSystem.EUROPEAN),
    FedSetup("Polska",              135,    CalendarSystem.EUROPEAN),
    FedSetup("Dania",               39,     CalendarSystem.EUROPEAN),
    FedSetup("Szwajcaria",          148,    CalendarSystem.EUROPEAN),
    FedSetup("Izrael",              74,     CalendarSystem.EUROPEAN),
    FedSetup("Cypr",                188,    CalendarSystem.EUROPEAN),
    FedSetup("Szwecja",             147,    CalendarSystem.NORTH),
    FedSetup("Chorwacja",           37,     CalendarSystem.EUROPEAN), // 2 liga nie ma aktualnego sezonu
    FedSetup("Serbia",              215,    CalendarSystem.EUROPEAN), // 2 liga nie ma aktualnego sezonu
    FedSetup("Ukraina",             177,    CalendarSystem.EUROPEAN),
    FedSetup("Węgry",               178,    CalendarSystem.EUROPEAN),
    FedSetup("Rumunia",             140,    CalendarSystem.EUROPEAN),
    FedSetup("Rosja",               141,    CalendarSystem.NORTH),
    FedSetup("Słowacja",            154,    CalendarSystem.EUROPEAN),
    FedSetup("Słowenia",            155,    CalendarSystem.EUROPEAN),
    FedSetup("Bułgaria",            28,     CalendarSystem.EUROPEAN),
    FedSetup("Azerbejdżan",         13,     CalendarSystem.EUROPEAN),
    FedSetup("Irlandia",            72,     CalendarSystem.NORTH),
    FedSetup("Mołdawia",            112,    CalendarSystem.EUROPEAN),
    FedSetup("Islandia",            73,     CalendarSystem.NORTH),
    FedSetup("Bośnia i Hercegowina",24,     CalendarSystem.EUROPEAN),
    FedSetup("Armenia",             10,     CalendarSystem.EUROPEAN), // 2 liga nie ma aktualnego sezonu
    FedSetup("Łotwa",               92,     CalendarSystem.NORTH),
    FedSetup("Kosowo",              244,    CalendarSystem.EUROPEAN),
    FedSetup("Finlandia",           49,     CalendarSystem.NORTH),
    FedSetup("Kazachstan",          81,     CalendarSystem.NORTH),
    FedSetup("Wyspy Owcze",         208,    CalendarSystem.NORTH),
    FedSetup("Malta",               106,    CalendarSystem.EUROPEAN), // 1 liga nie ma aktualnego sezonu
    FedSetup("Irlandia Północna",   192,    CalendarSystem.EUROPEAN),
    FedSetup("Litwa",               98,     CalendarSystem.NORTH),
    FedSetup("Liechtenstein",       97,     CalendarSystem.EUROPEAN),
    FedSetup("Estonia",             47,     CalendarSystem.NORTH),
    FedSetup("Albania",             3,      CalendarSystem.EUROPEAN),
    FedSetup("Czarnogóra",          216,    CalendarSystem.EUROPEAN),
    FedSetup("Luksemburg",          99,     CalendarSystem.EUROPEAN),
    FedSetup("Walia",               191,    CalendarSystem.EUROPEAN),
    FedSetup("Gruzja",              53,     CalendarSystem.NORTH),
    FedSetup("Macedonia Północna",  100,    CalendarSystem.EUROPEAN),
    FedSetup("Białoruś",            18,     CalendarSystem.NORTH),
    FedSetup("Andora",              5,      CalendarSystem.EUROPEAN),
    FedSetup("Gibraltar",           266,    CalendarSystem.EUROPEAN), // 11 zespołów w lidze
    FedSetup("San Marino",          144,    CalendarSystem.EUROPEAN)
)

val initializedFederations = initFederations()

private fun initFederations(): List<Federation> {
    val federationSetupList = federationSetup
    val resultList = mutableListOf<Federation>()
    runBlocking(Dispatchers.IO) {
        for (setup in federationSetupList) {
            launch {
                val link = "$transfermarktBaseLinkNational/${setup.tmID}"
                resultList.add(Federation(setup.name, link, setup.calendarSystem))
            }
        }
    }
    return resultList
}
