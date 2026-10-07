package org.example.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.example.init.utils.PRINTING_ID_TAB
import org.example.init.utils.PRINTING_RANK_YEAR_TAB


@Entity
class LeagueTableRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,
    @ManyToOne
    var league: League,
    @ManyToOne
    var club: Club,
    var matches: Int,
    var goals: Int,
    var points: Int
) {

}


fun Set<LeagueTableRecord>.printLeagueTable() {
    val sortedRecords = this.sortedWith(compareByDescending<LeagueTableRecord> { it.points }
        .thenByDescending { it.goals }
        .thenBy { it.matches }
        .thenBy { it.club.name }
    )
    var tableHeader = ""
    for (i in 0..< PRINTING_ID_TAB) tableHeader = "$tableHeader "
    tableHeader = "${tableHeader}CLUB"
    for (i in tableHeader.length..< PRINTING_RANK_YEAR_TAB) tableHeader = "$tableHeader "
    tableHeader = "${tableHeader}MATCHES"
    for (i in tableHeader.length..< PRINTING_RANK_YEAR_TAB + 10) tableHeader = "$tableHeader "
    tableHeader = "${tableHeader}GOALS"
    for (i in tableHeader.length..< PRINTING_RANK_YEAR_TAB + 20) tableHeader = "$tableHeader "
    tableHeader = "${tableHeader}POINTS"
    println(tableHeader)
    sortedRecords.forEachIndexed { id, tableRecord ->
        var line = "${id + 1}."
        for (i in line.length..< PRINTING_ID_TAB) line = "$line "
        line = "$line${tableRecord.club.name}"
        for (i in line.length..< PRINTING_RANK_YEAR_TAB) line = "$line "
        line = "$line${tableRecord.matches}"
        for (i in line.length..< PRINTING_RANK_YEAR_TAB + 10) line = "$line "
        line = "$line${tableRecord.goals}"
        for (i in line.length..< PRINTING_RANK_YEAR_TAB + 20) line = "$line "
        line = "$line${tableRecord.points}"
        println(line)
    }
}
