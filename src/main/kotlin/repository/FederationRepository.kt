package org.example.repository

import org.example.model.Federation


object FederationRepository {

    fun getAllFederations(): Set<Federation> {
        return Database.transaction { session ->
            session.createQuery(
                """FROM Federation""",
                Federation::class.java
            ).resultList.toSet() }
    }

}
