package org.example.model

import org.example.init.PRINTING_RANK_YEAR_COLUMN_TAB
import org.example.init.PRINTING_RANK_YEAR_TAB
import org.example.service.SeasonService
import kotlin.collections.ArrayList
import kotlin.collections.indexOf


class UefaRankingPoints() {
    val seasonPoints = ArrayList<UefaSeasonPoints>()


    init {
        for (season in SeasonService.getSeasons()) {
            seasonPoints.add(UefaSeasonPoints(season, 0.0))
        }
    }

    fun assignPointsForSeason(season: String, assignValue: Double) {
        val seasonIndex = SeasonService.getSeasons().indexOf(season)
        seasonPoints[seasonIndex].seasonRank = assignValue
    }

    fun initNewSeason() {
        val lastSeason = seasonPoints.last().season
        val newSeason = SeasonService.getSeasons().last()
        if (lastSeason == newSeason) {
            throw Exception("Can not init new season for UefaRankingPoints class.")
        }
        seasonPoints.add(UefaSeasonPoints(newSeason, 0.0))
    }

    fun getFiveYearsRanking(forSeason: String? = SeasonService.getCurrentSeason()): Double {
        var sum = 0.0
        val seasonIndex = SeasonService.getSeasons().indexOf(forSeason)
        val lastSeasonIndex = if (seasonIndex < 4) 0 else seasonIndex - 4
        for (i in lastSeasonIndex..seasonIndex) {
            sum += this.seasonPoints[i].seasonRank
        }
        return sum
    }

    fun getCurrentSeasonPoints(forSeason: String? = SeasonService.getCurrentSeason()): Double {
        val seasonIndex = SeasonService.getSeasons().indexOf(forSeason)
        return this.seasonPoints[seasonIndex].seasonRank
    }

    fun getPreviousSeasonPoints(forSeason: String? = SeasonService.getCurrentSeason()): Double {
        val seasonIndex = SeasonService.getSeasons().indexOf(forSeason)
        val previousSeasonIndex = if (seasonIndex == 0) 0 else seasonIndex - 1
        return this.seasonPoints[previousSeasonIndex].seasonRank
    }

    fun getRankingSummaryLine(forSeason: String? = SeasonService.getCurrentSeason()): String {
        var line = ""
        val seasonIndex = SeasonService.getSeasons().indexOf(forSeason)
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
        fun getRankingHeader(forSeason: String? = SeasonService.getCurrentSeason()): String {
            var header = ""
            for (i in 0..< PRINTING_RANK_YEAR_TAB) header = "$header "
            val seasonIndex = SeasonService.getSeasons().indexOf(forSeason)
            val lastSeasonIndex = if (seasonIndex < 4) 0 else seasonIndex - 4
            for (i in lastSeasonIndex..seasonIndex) {
                val season = SeasonService.getSeasons()[i]
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
