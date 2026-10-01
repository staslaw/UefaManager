package org.example.repository

import org.example.model.Campaign
import org.example.model.Federation


object CampaignRepository {

    fun getAllCampaignsForFederation(federation: Federation): Set<Campaign> {
        return Database.transaction { session ->
            session.createQuery(
                """SELECT c FROM Campaign c WHERE c.federation = :federation""",
                Campaign::class.java
            ).setParameter("federation", federation)
                .resultList.toSet() }
    }

}
