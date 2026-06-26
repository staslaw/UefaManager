package org.example.service

import org.example.init.utils.CalendarSystem


class SeasonService() {

    companion object {
        private var northCurrentSeason = "2026"
        private var northSeasons = arrayListOf("2025", northCurrentSeason)
        private var europeanCurrentSeason = "2026/2027"
        private val europeanSeasons = arrayListOf(
            "2016/2017", "2017/2018", "2018/2019", "2019/2020", "2020/2021",
            "2021/2022", "2022/2023", "2023/2024", "2024/2025", "2025/2026",
            europeanCurrentSeason, "2027/2028", "2028/2029", "2029/2030"
        )

        fun getEuropeanSeasons(): List<String> { return europeanSeasons }
        fun getEuropeanCurrentSeason(): String { return europeanCurrentSeason }

        fun getCurrentSeason(calendarSystem: CalendarSystem): String {
            return if (calendarSystem == CalendarSystem.EUROPEAN) {
                europeanCurrentSeason
            } else {
                northCurrentSeason
            }
        }

        fun getPreviousSeason(calendarSystem: CalendarSystem): String {
            return if (calendarSystem == CalendarSystem.EUROPEAN) {
                europeanSeasons[europeanSeasons.indexOf(europeanCurrentSeason) - 1]
            } else {
                northSeasons[northSeasons.indexOf(northCurrentSeason) - 1]
            }
        }
    }


    fun printCurrentSeason() {
        println("Aktualny sezon to: $europeanCurrentSeason")
    }

    fun printAllSeasons() {
        println(europeanSeasons)
    }

    fun checkIfSeasonExists(season: String): Boolean {
        return europeanSeasons.contains(season)
    }

    fun addNewSeason() {
        val startYear = europeanSeasons.last().split("/")[1]
        val endYear = (startYear.toInt() + 1).toString()
        val newNextSeason = "$startYear/$endYear"
        europeanSeasons.add(newNextSeason)
        val currentSeasonId = europeanSeasons.indexOf(europeanCurrentSeason)
        europeanCurrentSeason = europeanSeasons[currentSeasonId + 1]
    }

}
