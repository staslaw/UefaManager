package org.example.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne


@Entity
class League(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,
    @ManyToOne
    var campaign: Campaign,
    var name: String,
    var competitionLevel: Int,
    var link: String,
    @ManyToMany(mappedBy = "leagues")
    var clubs: MutableSet<Club> = mutableSetOf(),
) {
//    private val htmlParser = LeagueHtmlParser(this.federation, this.link, this)
//    var leagueTable: LeagueTable? = this.htmlParser.getLeagueTable(this.clubs)

    fun printSummary() {
        println("$competitionLevel LIGA: ${this.name}")
//        leagueTable?.printLeagueTable()
    }

}
