package org.example.repository

import org.example.model.Campaign
import org.example.model.League


object LeagueRepository {

    fun getAllLeagues(): Set<League> {
        return Database.transaction { session ->
            session.createQuery(
                """FROM League""",
                League::class.java
            ).resultList.toSet() }
    }

    fun getLeaguesForCampaign(campaign: Campaign): Set<League> {
        return Database.transaction { session ->
            session.createQuery(
                """SELECT l FROM League l WHERE l.campaign = :campaign""",
                League::class.java
            ).setParameter("campaign", campaign).resultList.toSet() }
    }

}
