package org.example.init

import org.example.init.model.Federation


const val transfermarktBaseLink = "https://www.transfermarkt.pl"
const val transfermarktBaseLinkNational = "$transfermarktBaseLink/wettbewerbe/national/wettbewerbe"

val federations = listOf(
    Federation("Anglia", "$transfermarktBaseLinkNational/189"),
    Federation("Włochy", "$transfermarktBaseLinkNational/75"),
    Federation("Hiszpania", "$transfermarktBaseLinkNational/157"),
    Federation("Niemcy", "$transfermarktBaseLinkNational/40"),
    Federation("Francja", "$transfermarktBaseLinkNational/50"),
//    Federation("Holandia", "$transfermarktBaseLinkNational/122"),
//    Federation("Portugalia", "$transfermarktBaseLinkNational/136"),
//    Federation("Belgia", "$transfermarktBaseLinkNational/19"),
//    Federation("Czechy", "$transfermarktBaseLinkNational/172"),
//    Federation("Turcja", "$transfermarktBaseLinkNational/174"),
//    Federation("Norwegia", "$transfermarktBaseLinkNational/125"),
//    Federation("Grecja", "$transfermarktBaseLinkNational/56"),
//    Federation("Austria", "$transfermarktBaseLinkNational/127"),
//    Federation("Szkocja", "$transfermarktBaseLinkNational/190"),
//    Federation("Polska", "$transfermarktBaseLinkNational/135"),
//    Federation("Dania", "$transfermarktBaseLinkNational/39"),
//    Federation("Szwajcaria", "$transfermarktBaseLinkNational/148"),
//    Federation("Izrael", "$transfermarktBaseLinkNational/74"),
//    Federation("Cypr", "$transfermarktBaseLinkNational/188"),
//    Federation("Szwecja", "$transfermarktBaseLinkNational/147"),
//    Federation("Chorwacja", "$transfermarktBaseLinkNational/37"),
//    Federation("Serbia", "$transfermarktBaseLinkNational/215"),
//    Federation("Ukraina", "$transfermarktBaseLinkNational/177"),
//    Federation("Węgry", "$transfermarktBaseLinkNational/178"),
//    Federation("Rumunia", "$transfermarktBaseLinkNational/140"),
//    Federation("Rosja", "$transfermarktBaseLinkNational/141"),
//    Federation("Słowacja", "$transfermarktBaseLinkNational/154"),
//    Federation("Słowenia", "$transfermarktBaseLinkNational/155"),
//    Federation("Bułgaria", "$transfermarktBaseLinkNational/28"),
//    Federation("Azerbejdżan", "$transfermarktBaseLinkNational/13"),
//    Federation("Irlandia", "$transfermarktBaseLinkNational/72"),
//    Federation("Mołdawia", "$transfermarktBaseLinkNational/112"),
//    Federation("Islandia", "$transfermarktBaseLinkNational/73"),
//    Federation("Bośnia i Hercegowina", "$transfermarktBaseLinkNational/24"),
//    Federation("Armenia", "$transfermarktBaseLinkNational/10"),
//    Federation("Łotwa", "$transfermarktBaseLinkNational/92"),
//    Federation("Kosowo", "$transfermarktBaseLinkNational/244"),
//    Federation("Finlandia", "$transfermarktBaseLinkNational/49"),
//    Federation("Kazachstan", "$transfermarktBaseLinkNational/81"),
//    Federation("Wyspy Owcze", "$transfermarktBaseLinkNational/208"),
//    Federation("Malta", "$transfermarktBaseLinkNational/106"),
//    Federation("Irlandia Północna", "$transfermarktBaseLinkNational/192"),
//    Federation("Litwa", "$transfermarktBaseLinkNational/98"),
//    Federation("Liechtenstein", "$transfermarktBaseLinkNational/97"),
//    Federation("Estonia", "$transfermarktBaseLinkNational/47"),
//    Federation("Albania", "$transfermarktBaseLinkNational/3"),
//    Federation("Czarnogóra", "$transfermarktBaseLinkNational/216"),
//    Federation("Luksemburg", "$transfermarktBaseLinkNational/99"),
//    Federation("Walia", "$transfermarktBaseLinkNational/191"),
//    Federation("Gruzja", "$transfermarktBaseLinkNational/53"),
//    Federation("Macedonia Północna", "$transfermarktBaseLinkNational/100"),
//    Federation("Białoruś", "$transfermarktBaseLinkNational/18"),
//    Federation("Andora", "$transfermarktBaseLinkNational/5"),
//    Federation("Gibraltar", "$transfermarktBaseLinkNational/266"),
//    Federation("San Marino", "$transfermarktBaseLinkNational/144")
)

const val PRINTING_ID_TAB = 5
const val PRINTING_NAME_TAB = 30
const val PRINTING_RANK_YEAR_TAB = PRINTING_ID_TAB + PRINTING_NAME_TAB
const val PRINTING_RANK_YEAR_COLUMN_TAB = 13
