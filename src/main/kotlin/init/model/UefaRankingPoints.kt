package org.example.init.model

import org.example.init.PRINTING_RANK_YEAR_COLUMN_TAB
import org.example.init.PRINTING_RANK_YEAR_TAB
import org.example.init.currentSeason
import org.example.init.seasons
import kotlin.collections.ArrayList
import kotlin.collections.indexOf


class UefaRankingPoints() {
    val seasonPoints = ArrayList<UefaSeasonPoints>()

    init {
        for (season in seasons) {
            seasonPoints.add(UefaSeasonPoints(season, 0.0))
        }
    }

    fun assignPointsForSeason(season: String, assignValue: Double) {
        val seasonIndex = seasons.indexOf(season)
        seasonPoints[seasonIndex].seasonRank = assignValue
    }

    fun initNewSeason() {
        val lastSeason = seasonPoints.last().season
        val newSeason = seasons.last()
        if (lastSeason == newSeason) {
            throw Exception("Can not init new season for UefaRankingPoints class.")
        }
        seasonPoints.add(UefaSeasonPoints(newSeason, 0.0))
    }

    fun getFiveYearsRanking(forSeason: String? = currentSeason): Double {
        var sum = 0.0
        val seasonIndex = seasons.indexOf(forSeason)
        val lastSeasonIndex = if (seasonIndex < 4) 0 else seasonIndex - 4
        for (i in lastSeasonIndex..seasonIndex) {
            sum += this.seasonPoints[i].seasonRank
        }
        return sum
    }

    fun getCurrentSeasonPoints(forSeason: String? = currentSeason): Double {
        val seasonIndex = seasons.indexOf(forSeason)
        return this.seasonPoints[seasonIndex].seasonRank
    }

    fun getPreviousSeasonPoints(forSeason: String? = currentSeason): Double {
        val seasonIndex = seasons.indexOf(forSeason)
        val previousSeasonIndex = if (seasonIndex == 0) 0 else seasonIndex - 1
        return this.seasonPoints[previousSeasonIndex].seasonRank
    }

    fun getRankingSummaryLine(forSeason: String? = currentSeason): String {
        var line = ""
        val seasonIndex = seasons.indexOf(forSeason)
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
        fun getRankingHeader(forSeason: String? = currentSeason): String {
            var header = ""
            for (i in 0..< PRINTING_RANK_YEAR_TAB) header = "$header "
            val seasonIndex = seasons.indexOf(forSeason)
            val lastSeasonIndex = if (seasonIndex < 4) 0 else seasonIndex - 4
            for (i in lastSeasonIndex..seasonIndex) {
                val season = seasons[i]
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
