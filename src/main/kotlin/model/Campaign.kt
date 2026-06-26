package org.example.model

import org.example.init.htmlParser.FederationHtmlParser


class Campaign(val season: String, val htmlParser: FederationHtmlParser, existingClubs: List<Club>) {
    val leagues: ArrayList<League> = arrayListOf()

    init {
        val year = getYear(this.season)
        val firstLeagueName = this.htmlParser.getFirstLeagueName()
        val firstLeagueBaseLink = this.htmlParser.getFirstLeagueBaseLink()
        firstLeagueName?.let { name ->
            firstLeagueBaseLink?.let { link ->
                val fullLink = "$link/plus/?saison_id=$year"
                val firstLeague = League(name, 1, fullLink, existingClubs)
                this.leagues.add(firstLeague)
            }
        }

        val secondLeagueName = this.htmlParser.getSecondLeagueName()
        val secondLeagueBaseLink = this.htmlParser.getSecondLeagueBaseLink()
        secondLeagueName?.let { name ->
            secondLeagueBaseLink?.let { link ->
                val fullLink = "$link/plus/?saison_id=$year"
                val allClubs = mutableSetOf<Club>()
                allClubs.addAll(existingClubs)
                allClubs.addAll(this.leagues.firstOrNull()?.clubs ?: emptyList())
                val secondLeague = League(name, 2, fullLink, allClubs.toList())
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
