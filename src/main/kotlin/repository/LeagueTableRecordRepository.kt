package org.example.repository

import org.example.model.League
import org.example.model.LeagueTableRecord


object LeagueTableRecordRepository {

    fun getTableRecordsForLeague(league: League): Set<LeagueTableRecord> {
        return Database.transaction { session ->
            session.createQuery(
                """SELECT r FROM LeagueTableRecord r WHERE r.league = :league""",
                LeagueTableRecord::class.java
            ).setParameter("league", league).resultList.toSet() }
    }
}