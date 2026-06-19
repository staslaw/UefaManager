package org.example.init

import org.example.init.model.Team
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.min


const val countryRank17to21Path = "http://www.90minut.pl/ranking_uefa.php?id_sezon=97"
const val countryRank22to26Path = "http://www.90minut.pl/ranking_uefa.php?id_sezon=107"
const val clubRank22to26Path = "http://www.90minut.pl/ranking_uefa.php?i=1&id_sezon=107"


fun init() {
    println("PRZYGOTOWANIE GRY ROZPOCZĘTE")
    getFederationsRank()
    getClubsRank()
    println("PRZYGOTOWANIE GRY ZAKOŃCZONE")
}

private fun getFederationsRank() {
    getRanksFromLink(countryRank17to21Path)
    getRanksFromLink(countryRank22to26Path)
}

private fun getRanksFromLink(link: String) {
    val doc = Jsoup.connect(link).get()
    val countryTable = doc.select("table").first()
    val countryRecords = countryTable?.select("tr")
    val seasons = getSeasons(countryTable)
    for (federation in federations) {
        val row = countryRecords?.find { it.select("td").any { it.text().contains(federation.name) } }
        val cells = row?.select("td")
        for (i in 0..< seasons.size) {
            val cellValue =  cells?.get(i + 2)?.text()
            val rankValue = cellValue?.replace(",", ".")?.toDouble() ?: 0.0
            federation.ranking.assignPointsForSeason(seasons[i], rankValue)
        }
    }
}

private fun getSeasons(countryTable: Element?): LinkedList<String> {
    val seasons = LinkedList<String>()
    countryTable?.select("tr")?.first()?.let { row ->
        val cells = row.select("td")
        for (cell in cells) {
            if (cell.text().startsWith("20")) {
                seasons.add(cell.text())
            }
        }
    }
    return seasons
}

private fun getClubsRank() {
    val doc = Jsoup.connect(clubRank22to26Path).get()
    val rows = doc.select("tbody").first()!!.select("tr")

    val allNotMatched = mutableListOf<MutableMap<String, String>>()
    for (federation in federations) {
        val leagueTeams = federation.league?.teams?: emptyList()
        val leagueClubs = leagueTeams.toMutableList()
        val rankingClubs = mutableListOf<MutableMap<String, String>>()
        for (i in 1..< rows.size) {
            val cells = rows[i].select("td")
            val country = cells[2].select("img").attr("title")
            if (country == federation.name) {
                val rankingClubMap = hashMapOf<String, String>()
                rankingClubMap["name"] = cells[1].text()
                rankingClubMap["2021/2022"] = cells[3].text()
                rankingClubMap["2022/2023"] = cells[4].text()
                rankingClubMap["2023/2024"] = cells[5].text()
                rankingClubMap["2024/2025"] = cells[6].text()
                rankingClubMap["2025/2026"] = cells[7].text()
                rankingClubs.add(rankingClubMap)
            }
        }

        var notMatched = matchTeamNames(rankingClubs, leagueClubs, ::isTheSame)
        notMatched = matchTeamNames(notMatched, leagueClubs, ::containsOrContainsParts)
        notMatched = matchTeamNames(notMatched, leagueClubs, ::isTheSameWithChangedChars)
        notMatched = matchTeamNames(notMatched, leagueClubs, ::isTheSameFiltered)
        notMatched = matchTeamNames(notMatched, leagueClubs, ::containsWithChangedChars)
        notMatched = matchTeamNames(notMatched, leagueClubs, ::containsOrContainsPartFiltered)
        notMatched = matchTeamNames(notMatched, leagueClubs, ::isTheSameFilteredWithChangedChars)
        notMatched = matchTeamNames(notMatched, leagueClubs, ::containsFilteredWithChangedChars)
        notMatched = matchTeamNames(notMatched, leagueClubs, ::isItSpecialMatch)
        allNotMatched.addAll(notMatched)
    }
    println("${allNotMatched.size} klubów niedopasowanych z rankingiem")
}

private fun matchTeamNames(
    rankClubs: MutableList<MutableMap<String, String>>,
    leagueClubs: MutableList<Team>,
    matchMethod: (name1: String, name2: String) -> Boolean
): MutableList<MutableMap<String, String>> {
    val rankingClubNotMatched = mutableListOf<MutableMap<String, String>>()
    rankClubs.forEach { rankingClubMap ->
        val matches = leagueClubs.filter { matchMethod(it.name, rankingClubMap["name"]!!) }
        if (matches.size != 1) {
            rankingClubNotMatched.add(rankingClubMap)
        } else {
            leagueClubs.remove(matches[0])
            rankingClubMap.remove("name")
            rankingClubMap.forEach {
                val season = it.key
                val seasonRank = it.value.replace(",", ".").toDouble()
                matches[0].ranking.assignPointsForSeason(season, seasonRank)
            }
        }
    }
    return rankingClubNotMatched
}

private fun filterAndCallMatchMethod(name1: String, name2: String, matchMethod: (name1: String, name2: String) -> Boolean):  Boolean {
    val filteredName1 = filterName(name1)
    val filteredName2 = filterName(name2)
    return if (filteredName1.isEmpty() || filteredName2.isEmpty()) {
        false
    } else {
        matchMethod(filteredName1, filteredName2)
    }
}

private fun isTheSame(name1: String, name2: String): Boolean {
    return name1 == name2
}

private fun containsOrContainsParts(name1: String, name2: String): Boolean {
    return contains(name1, name2) || containsParts(name1, name2)
}

private fun contains(name1: String, name2: String): Boolean {
    return name1.contains(name2) || name2.contains(name1)
}

private fun containsParts(name1: String, name2: String): Boolean {
    val split1 = name1.split(" ")
    val split2 = name2.split(" ")
    return split1.containsAll(split2) || split2.containsAll(split1)
}

private fun isTheSameWithChangedChars(name1: String, name2: String, changes: Int? = 2): Boolean {
    val lengthDiff = abs(name1.length - name2.length)
    val diffs = countDifferentCharsInShorterBounds(name1, name2, false)
    return lengthDiff + diffs <= changes!!
}

private fun isTheSameFiltered(name1: String, name2: String): Boolean {
    return filterAndCallMatchMethod(name1, name2, ::isTheSame)
}

private fun containsWithChangedChars(name1: String, name2: String): Boolean {
    val changes = 2
    return if (countDifferentCharsInShorterBounds(name1, name2, false) <= changes) {
        true
    } else if (countDifferentCharsInShorterBounds(name1, name2, true) <= changes) {
        true
    } else {
        false
    }
}

private fun containsOrContainsPartFiltered(name1: String, name2: String): Boolean {
    return filterAndCallMatchMethod(name1, name2, ::containsOrContainsParts)
}

private fun isTheSameFilteredWithChangedChars(name1: String, name2: String): Boolean {
    return filterAndCallMatchMethod(name1, name2, ::isTheSameWithChangedChars)
}

private fun containsFilteredWithChangedChars(name1: String, name2: String): Boolean {
    return filterAndCallMatchMethod(name1, name2, ::containsWithChangedChars)
}

private fun countDifferentCharsInShorterBounds(name1: String, name2: String, reverseOrder: Boolean): Int {
    val len1 = name1.length
    val len2 = name2.length
    val min = min(len1, len2)
    var count = 0
    for (i in 0..< min) {
        val c1 = if (reverseOrder) name1[len1 - 1 - i] else name1[i]
        val c2 = if (reverseOrder) name2[len2 - 1 - i] else name2[i]
        if (c1 != c2) count++
    }
    return count
}

private fun filterName(name: String): String {
    return name.split(" ")
        .filter { it.length > 3 }
        .filter { it.toIntOrNull() == null }
        .map { it.replace("(", "").replace(")", "") }
        .joinToString(" ") { it.lowercase() }
}

private fun isItSpecialMatch(name1: String, name2: String): Boolean {
    val map = mapOf(
        "Inter Mediolan" to "FC Internazionale Milano",
        "Bayern Monachium" to "FC Bayern München",
        "FC Viktoria Pilzno" to "FC Viktoria Plzeň",
        "Vålerenga Fotball Elite" to "Vålerengens IF",
        "FC Kopenhaga" to "FC København",
        "FC Nordsjaelland" to "FC Nordsjælland",
        "Beitar Jerozolima" to "Beitar Jerusalem",
        "Omonia Nikozja" to "AS Omónia Lefkossías",
        "Aris Limassol" to "Áris Lemessoú",
        "Anorthosis Famagusta" to "AS Anórthossis Ammochóstou",
        "Zienit Sankt Petersburg" to "Zenit Petersburg",
        "ŠK Slovan Bratysława" to "Slovan Bratislava",
        "NK Olimpija (Lublana)" to "NK Olimpija Ljubljana",
        "Łudogorec Razgrad" to "Ludogorets Razgrad",
        "Arda Kyrdżali" to "Arda Kardzhali",
        "Botew Płowdiw" to "Botev Plovdiv",
        "Łokomotiw Płowdiw" to "Lokomotiv Plovdiv",
        "Neftçı Baku" to "Neftchi PFK",
        "Qəbələ FK" to "FK Qabala",
        "Araz Naxçıvan" to "Araz-Nachiczewan",
        "FC Shamakhi" to "Keşlə FK",
        "Milsami Orhei" to "FC Milsami Orgiejów",
        "Zimbru Chișinău" to "Zimbru Kiszyniów",
        "Stjarnan Gardabaer" to "Stjarnan (Garðabær)",
        "Alaszkert Erewan" to "Alashkert Yerevan",
        "RFS Ryga" to "FC RFS",
        "FK Liepāja" to "FK Lipawa",
        "Kyzył-Żar Petropawł" to "Qyzyljar Petropavlovsk",
        "FK Kauno Zalgiris" to "Žalgiris Kowno",
        "FC Shkupi" to "FK Škupi (Skopje)",
        "Makedonija Gjorce Petrov" to "FK Makedonija Ǵorče Petrov (Skopje)",
        "FK Akademija Pandev (Strumica)" to "AP Brera Strumica",
        "Torpiedo Żodzino" to "Torpedo-BelAZ Zhodino",
        "FK Brześć" to "Dynamo Brest",
        "NK Željezničar (Sarajewo)" to "FK Zeljeznicar Sarajevo",
    )
    return map[name1] == name2 || map[name2] == name1
}
// BŁĘDY
// Isłocz Minskij rajon - FK Minsk      ---> Isloch Minsk Region

// FK Podgorica - Buducnost Podgorica   ---> spadło (może dodanie drugiej ligi pomoże)
// FK Budu�nost (Podgorica) - NULL      ---> Buducnost Podgorica

// FC Haka - IFK Mariehamn              ---> FC HAKA spadło (może dodanie drugiej ligi pomoże) FIN

// SPADKOWICZE
// Leicester City FC            - spadło
// SBV Vitesse                  - spadło
// FC Pa�os de Ferreira         - spadło
// Sivasspor Kul�b�             - spadło
// Adana Demirspor Kul�b�       - spadło
// Saint Johnstone FC           - spadło
// �l�sk Wroc�aw                - spadło
// Wis�a Krak�w                 - spadło
// Maccabi Petach Tikwa         - spadło
// Dnipro-1 Dniepropietrowsk    - spadło/ klub przestał istnieć
// FC Feh�rv�r                  - spadło i zmieniło nazwę na Videoton FC
// Kecskem�ti TE                - spadło
// Sepsi OSK Sf�ntu Gheorghe    - spadło
// Corvinul Hunedoara           - spadło
// FC Sfîntul Gheorghe Suruceni - klub przestał istnieć
// FK Tuzla City (Simin Han)    - spadło
// Valmiera FC                  - spadło
// Tampere United               - spadło
// FC Honka                     - spadło
// Szachtior Karaganda          - spadło
// Balzan FC                    - spadło
// JK Tallinna Kalev            - spadło
// KF Laçi                      - spadło
// FK Iskra (Danilovgrad)       - spadło
// CS Fola Esch                 - spadło
// Newtown AFC                  - spadło
// Szachtior Soligorsk          - spadło
// UE Sant Julià               - nie istnieje