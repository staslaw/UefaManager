package org.example.init.model


class RankPointsPerSeason(val season: String, val seasonRank: Double, val fiveYearsRank: Double) {
    override fun toString(): String {
        return "${this.season} - ${this.seasonRank} - ${this.fiveYearsRank}"
    }
}
