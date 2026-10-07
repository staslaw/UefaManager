package org.example.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne


@Entity
class ClubRankingSeasonPoints(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,
    @ManyToOne
    var club: Club,
    override val season: String,
    override var seasonRank: Double
): SeasonPoints {
    override fun toString(): String {
        return "${this.season} - ${this.seasonRank}"
    }
}