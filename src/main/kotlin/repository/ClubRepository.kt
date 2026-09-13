package org.example.repository

import org.example.model.Club
import org.example.model.Federation

object ClubRepository {

    fun getAllClubsFromFederation(federation: Federation): Set<Club> {
        return Database.transaction { session ->
            session.createQuery(
                """SELECT c FROM Club c WHERE c.federation = :federation""",
                Club::class.java
            ).setParameter("federation", federation)
                .resultList.toSet() }
    }
}