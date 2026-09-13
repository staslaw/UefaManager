package org.example.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.example.init.htmlParser.FederationHtmlParser


@Entity
class Campaign(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,
    @ManyToOne
    var federation: Federation,
    var season: String,
    @Transient
    var htmlParser: FederationHtmlParser,
    @Transient
    var leagues: ArrayList<League> = arrayListOf()
) {

    init {
        val year = getYear(this.season)
        val firstLeagueName = this.htmlParser.getFirstLeagueName()
        val firstLeagueBaseLink = this.htmlParser.getFirstLeagueBaseLink()
        firstLeagueName?.let { name ->
            firstLeagueBaseLink?.let { link ->
                val fullLink = "$link/plus/?saison_id=$year"
                val firstLeague = League(this.federation, name, 1, fullLink)
                this.leagues.add(firstLeague)
            }
        }

        val secondLeagueName = this.htmlParser.getSecondLeagueName()
        val secondLeagueBaseLink = this.htmlParser.getSecondLeagueBaseLink()
        secondLeagueName?.let { name ->
            secondLeagueBaseLink?.let { link ->
                val fullLink = "$link/plus/?saison_id=$year"
                val secondLeague = League(this.federation, name, 2, fullLink)
                this.leagues.add(secondLeague)
            }
        }
    }

    private fun getYear(season: String): String {
        return if (season.contains("/")) {
            season.split("/")[0]
        } else {
            (season.toInt() - 1).toString()
        }
    }
}
