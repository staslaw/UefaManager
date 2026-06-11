package org.example.init


var currentSeason = "2025/2026"

val seasons = arrayListOf(
    "2016/2017",
    "2017/2018",
    "2018/2019",
    "2019/2020",
    "2020/2021",
    "2021/2022",
    "2022/2023",
    "2023/2024",
    "2024/2025",
    currentSeason
)

val federations = listOf(
    Federation("Anglia",""),
    Federation("Włochy",""),
    Federation("Hiszpania",""),
    Federation("Niemcy",""),
    Federation("Francja",""),
    Federation("Holandia",""),
    Federation("Portugalia",""),
    Federation("Belgia",""),
    Federation("Czechy",""),
    Federation("Turcja",""),
    Federation("Norwegia",""),
    Federation("Grecja",""),
    Federation("Austria",""),
    Federation("Szkocja",""),
    Federation("Polska",""),
    Federation("Dania",""),
    Federation("Szwajcaria",""),
    Federation("Izrael",""),
    Federation("Cypr",""),
    Federation("Szwecja",""),
    Federation("Chorwacja",""),
    Federation("Serbia",""),
    Federation("Ukraina",""),
    Federation("Węgry",""),
    Federation("Rumunia",""),
    Federation("Rosja",""),
    Federation("Słowacja",""),
    Federation("Słowenia",""),
    Federation("Bułgaria",""),
    Federation("Azerbejdżan",""),
    Federation("Irlandia",""),
    Federation("Mołdawia",""),
    Federation("Islandia",""),
    Federation("Bośnia i Hercegowina",""),
    Federation("Armenia",""),
    Federation("Łotwa",""),
    Federation("Kosowo",""),
    Federation("Finlandia",""),
    Federation("Kazachstan",""),
    Federation("Wyspy Owcze",""),
    Federation("Malta",""),
    Federation("Irlandia Północna",""),
    Federation("Litwa",""),
    Federation("Liechtenstein",""),
    Federation("Estonia",""),
    Federation("Albania",""),
    Federation("Czarnogóra",""),
    Federation("Luksemburg",""),
    Federation("Walia",""),
    Federation("Gruzja",""),
    Federation("Macedonia Północna",""),
    Federation("Białoruś",""),
    Federation("Andora",""),
    Federation("Gibraltar",""),
    Federation("San Marino","")
)

fun addNewSeason() {
    val startYear = currentSeason.split("/")[1]
    val endYear = (startYear.toInt() + 1).toString()
    currentSeason = "$startYear/$endYear"
    seasons.add(currentSeason)
    federations.addNewSeason()
}
