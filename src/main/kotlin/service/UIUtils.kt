package org.example.service

import org.example.init.utils.PRINTING_RANK_YEAR_COLUMN_TAB
import org.example.init.utils.PRINTING_RANK_YEAR_TAB


fun getRankingHeader(season: String): String {
    var header = ""
    for (i in 0..< PRINTING_RANK_YEAR_TAB) header = "$header "
    val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(season)
    val lastSeasonIndex = if (seasonIndex < 4) 0 else seasonIndex - 4
    for (i in lastSeasonIndex..seasonIndex) {
        val season = SeasonService.getEuropeanSeasons()[i]
        header = "$header$season"
        for (i in season.length..< PRINTING_RANK_YEAR_COLUMN_TAB) header = "$header "
    }
    header = "$header      RAZEM"
    return header
}