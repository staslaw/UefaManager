package org.example.init

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.init.utils.transfermarktBaseLinkNational
import org.example.model.Federation


private val federationsNameAndId = listOf(
    Pair("Anglia", 189),
    Pair("Włochy", 75),
    Pair("Hiszpania", 157),
    Pair("Niemcy", 4),
    Pair("Francja", 50),
    Pair("Holandia", 122),
    Pair("Portugalia", 136),
    Pair("Belgia", 19),
    Pair("Czechy", 172),
    Pair("Turcja", 174),
    Pair("Norwegia", 125),
    Pair("Grecja", 56),
    Pair("Austria", 127),
    Pair("Szkocja", 190),
    Pair("Polska", 135),
    Pair("Dania", 39),
    Pair("Szwajcaria", 148),
    Pair("Izrael", 74),
    Pair("Cypr", 188),
    Pair("Szwecja", 147),
    Pair("Chorwacja", 37),
    Pair("Serbia", 215),
    Pair("Ukraina", 177),
    Pair("Węgry", 178),
    Pair("Rumunia", 140),
    Pair("Rosja", 141),
    Pair("Słowacja", 154),
    Pair("Słowenia", 155),
    Pair("Bułgaria", 28),
    Pair("Azerbejdżan", 13),
    Pair("Irlandia", 72),
    Pair("Mołdawia", 112),
    Pair("Islandia", 73),
    Pair("Bośnia i Hercegowina", 24),
    Pair("Armenia", 10),
    Pair("Łotwa", 9),
    Pair("Kosowo", 244),
    Pair("Finlandia", 49),
    Pair("Kazachstan", 81),
    Pair("Wyspy Owcze", 208),
    Pair("Malta", 106),
    Pair("Irlandia Północna", 192),
    Pair("Litwa", 98),
    Pair("Liechtenstein", 97),
    Pair("Estonia", 47),
    Pair("Albania", 3),
    Pair("Czarnogóra", 216),
    Pair("Luksemburg", 99),
    Pair("Walia", 191),
    Pair("Gruzja", 53),
    Pair("Macedonia Północna", 100),
    Pair("Białoruś", 18),
    Pair("Andora", 5),
    Pair("Gibraltar", 266),
    Pair("San Marino", 144)
)

val initializedFederations = initFederations()

private fun initFederations(): List<Federation> {
    val federationsNameAndIdList = federationsNameAndId
    val resultList = mutableListOf<Federation>()
    runBlocking(Dispatchers.IO) {
        for (nameAndId in federationsNameAndIdList) {
            launch {
                val name = nameAndId.first
                val link = "$transfermarktBaseLinkNational/${nameAndId.second}"
                resultList.add(Federation(name, link))
            }
        }
    }
    return resultList
}
