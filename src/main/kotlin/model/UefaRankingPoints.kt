package org.example.model

import org.example.init.utils.PRINTING_RANK_YEAR_COLUMN_TAB
import org.example.init.utils.PRINTING_RANK_YEAR_TAB
import org.example.service.SeasonService
import kotlin.collections.ArrayList


class UefaRankingPoints() {
    val seasonPoints = ArrayList<UefaSeasonPoints>()


    init {
        for (season in SeasonService.getEuropeanSeasons()) {
            seasonPoints.add(UefaSeasonPoints(season, 0.0))
        }
    }

    fun assignPointsForSeason(season: String, assignValue: Double) {
        val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(season)
        seasonPoints[seasonIndex].seasonRank = assignValue
    }

    fun initNewSeason() {
        val lastSeason = seasonPoints.last().season
        val newSeason = SeasonService.getEuropeanSeasons().last()
        if (lastSeason == newSeason) {
            throw Exception("Can not init new season for UefaRankingPoints class.")
        }
        seasonPoints.add(UefaSeasonPoints(newSeason, 0.0))
    }

    fun getFiveYearsRanking(forSeason: String): Double {
        var sum = 0.0
        val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(forSeason)
        val lastSeasonIndex = if (seasonIndex < 4) 0 else seasonIndex - 4
        for (i in lastSeasonIndex..seasonIndex) {
            sum += this.seasonPoints[i].seasonRank
        }
        return sum
    }

    fun getCurrentSeasonPoints(season: String): Double {
        val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(season)
        return this.seasonPoints[seasonIndex].seasonRank
    }

    fun getPreviousSeasonPoints(season: String): Double {
        val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(season)
        val previousSeasonIndex = if (seasonIndex == 0) 0 else seasonIndex - 1
        return this.seasonPoints[previousSeasonIndex].seasonRank
    }

    fun getRankingSummaryLine(forSeason: String): String {
        var line = ""
        val seasonIndex = SeasonService.getEuropeanSeasons().indexOf(forSeason)
        val lastSeasonIndex = if (seasonIndex < 4) 0 else seasonIndex - 4
        var fiveYearsRank = 0.0
        for (i in lastSeasonIndex..seasonIndex) {
            val seasonRank = this.seasonPoints[i].seasonRank
            fiveYearsRank += seasonRank
            line = "$line$seasonRank"
            for (i in seasonRank.toString().length..< PRINTING_RANK_YEAR_COLUMN_TAB) line = "$line "
        }
        val fiveYearsRankString = String.format("%.3f", fiveYearsRank)
        return "$line|     $fiveYearsRankString"
    }

    companion object {
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
    }
}


class UefaSeasonPoints(val season: String, var seasonRank: Double) {
    override fun toString(): String {
        return "${this.season} - ${this.seasonRank}"
    }
}
