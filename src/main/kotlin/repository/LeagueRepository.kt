package org.example.repository

import org.example.model.League


object LeagueRepository {

    fun getAllLeagues(): Set<League> {
        return Database.transaction { session ->
            session.createQuery(
                """FROM League""",
                League::class.java
            ).resultList.toSet() }
    }

}
