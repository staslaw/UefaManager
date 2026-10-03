package org.example.repository

import org.example.model.Club
import org.example.model.Federation


object ClubRepository {

    fun getAllClubs(): Set<Club> {
        return Database.transaction { session ->
            session.createQuery(
                """FROM Club""",
                Club::class.java
            ).resultList.toSet() }
    }

    fun getClubsWithLeaguesFromFederation(federation: Federation): Set<Club> {
        return Database.transaction { session ->
            session.createQuery(
                """SELECT DISTINCT c FROM Club c LEFT JOIN FETCH c.leagues WHERE c.federation = :federation""",
                Club::class.java
            ).setParameter("federation", federation)
                .resultList.toSet() }
    }

    fun getClubsWithRankFromFederation(federation: Federation): Set<Club> {
        return Database.transaction { session ->
            session.createQuery(
                """SELECT DISTINCT c FROM Club c LEFT JOIN FETCH c.rankingPoints WHERE c.federation = :federation""",
                Club::class.java
            ).setParameter("federation", federation)
                .resultList.toSet() }
    }

}
