package org.example.model

import org.example.init.utils.PRINTING_RANK_YEAR_COLUMN_TAB
import org.example.service.SeasonService


interface SeasonPoints {
    val season: String
    var seasonRank: Double
}


fun List<SeasonPoints>.assignPointsForSeason(season: String, assignValue: Double) {
    val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(season)
    this[seasonIndex].seasonRank = assignValue
}

fun List<SeasonPoints>.getFiveYearsRanking(forSeason: String): Double {
    var sum = 0.0
    val sortedThis = this.sortedBy { it.season }
    val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(forSeason)
    val lastSeasonIndex = if (seasonIndex < 4) 0 else seasonIndex - 4
    for (i in lastSeasonIndex..seasonIndex) {
        sum += sortedThis[i].seasonRank
    }
    return sum
}

fun List<SeasonPoints>.getCurrentSeasonPoints(season: String): Double {
    val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(season)
    return this[seasonIndex].seasonRank
}

fun List<SeasonPoints>.getPreviousSeasonPoints(season: String): Double {
    val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(season)
    val previousSeasonIndex = if (seasonIndex == 0) 0 else seasonIndex - 1
    return this[previousSeasonIndex].seasonRank
}

fun List<SeasonPoints>.getRankingSummaryLine(forSeason: String): String {
    var line = ""
    val sortedThis = this.sortedBy { it.season }
    val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(forSeason)
    val lastSeasonIndex = if (seasonIndex < 4) 0 else seasonIndex - 4
    var fiveYearsRank = 0.0
    for (i in lastSeasonIndex..seasonIndex) {
        val seasonRank = sortedThis[i].seasonRank
        fiveYearsRank += seasonRank
        line = "$line$seasonRank"
        for (i in seasonRank.toString().length..< PRINTING_RANK_YEAR_COLUMN_TAB) line = "$line "
    }
    val fiveYearsRankString = String.format("%.3f", fiveYearsRank)
    return "$line|     $fiveYearsRankString"
}