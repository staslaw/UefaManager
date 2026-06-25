package org.example.service


class SeasonService() {

    companion object {
        private var currentSeason = "2025/2026"
        private val seasons = arrayListOf(
            "2016/2017", "2017/2018", "2018/2019", "2019/2020", "2020/2021",
            "2021/2022", "2022/2023", "2023/2024", "2024/2025", currentSeason,
            "2026/2027", "2027/2028", "2028/2029", "2029/2030"
        )

        fun getCurrentSeason(): String { return currentSeason }
        fun getSeasons(): List<String> { return seasons }
    }


    fun printCurrentSeason() {
        println("Aktualny sezon to: $currentSeason")
    }

    fun printAllSeasons() {
        println(seasons)
    }

    fun checkIfSeasonExists(season: String): Boolean {
        return seasons.contains(season)
    }

    fun addNewSeason() {
        val startYear = seasons.last().split("/")[1]
        val endYear = (startYear.toInt() + 1).toString()
        val newNextSeason = "$startYear/$endYear"
        seasons.add(newNextSeason)
        val currentSeasonId = seasons.indexOf(currentSeason)
        currentSeason = seasons[currentSeasonId + 1]
    }

}
