package org.example.service


class SeasonService() {

    companion object {
        private var currentSeason = "2025/2026"
        private val seasons = arrayListOf(
            "2016/2017", "2017/2018", "2018/2019", "2019/2020", "2020/2021",
            "2021/2022", "2022/2023", "2023/2024", "2024/2025", currentSeason
        )

        fun getCurrentSeason(): String { return currentSeason }
        fun getSeasons(): List<String> { return seasons }
    }


    fun printCurrentSeason() {
        println("Aktualny sezon to: $currentSeason")
    }

    fun checkIfSeasonExists(season: String): Boolean {
        return seasons.contains(season)
    }

    fun addNewSeason() {
        val startYear = currentSeason.split("/")[1]
        val endYear = (startYear.toInt() + 1).toString()
        currentSeason = "$startYear/$endYear"
        seasons.add(currentSeason)
    }

}
