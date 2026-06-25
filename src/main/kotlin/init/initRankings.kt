package org.example.init

import org.example.init.htmlParser.FederationRankingHtmlParser
import org.example.init.htmlParser.ClubRankingHtmlParser
import org.example.model.Club
import kotlin.math.abs
import kotlin.math.min


const val countryRank17to21Path = "http://www.90minut.pl/ranking_uefa.php?id_sezon=97"
const val countryRank22to26Path = "http://www.90minut.pl/ranking_uefa.php?id_sezon=107"
const val clubRank22to26Path = "http://www.90minut.pl/ranking_uefa.php?i=1&id_sezon=107"


fun initRankings() {
    getFederationsRank()
    getClubsRank()
}

private fun getFederationsRank() {
    getFederationsRankFromLink(countryRank17to21Path)
    getFederationsRankFromLink(countryRank22to26Path)
}

private fun getFederationsRankFromLink(link: String) {
    val parser = FederationRankingHtmlParser(link)
    val countryToRanksMap = parser.getFederationsRankMapFromLink()
    for (federation in initializedFederations) {
        countryToRanksMap[federation.name]?.let { rankMap ->
            for (rank in rankMap.entries) {
                federation.ranking.assignPointsForSeason(rank.key, rank.value)
            }
        }
    }
}

private fun getClubsRank() {
    val parser = ClubRankingHtmlParser(clubRank22to26Path)
    val countryToClubsToRankMap = parser.getCountryToClubsToRankMapFromLink()

    val allNotMatched = mutableListOf<Pair<String, HashMap<String, Double>>>()
    for (federation in initializedFederations) {
        val leagueClubs = federation.clubs.toMutableList()
        val rankingClubs = countryToClubsToRankMap[federation.name]!!.toList().toMutableList()

        var notMatched = matchClubNames(rankingClubs, leagueClubs, ::isItSpecialMatch)
        notMatched = matchClubNames(notMatched, leagueClubs, ::isTheSame)
        notMatched = matchClubNames(notMatched, leagueClubs, ::containsOrContainsParts)
        notMatched = matchClubNames(notMatched, leagueClubs, ::isTheSameWithChangedChars)
        notMatched = matchClubNames(notMatched, leagueClubs, ::isTheSameFiltered)
        notMatched = matchClubNames(notMatched, leagueClubs, ::containsWithChangedChars)
        notMatched = matchClubNames(notMatched, leagueClubs, ::containsOrContainsPartFiltered)
        notMatched = matchClubNames(notMatched, leagueClubs, ::isTheSameFilteredWithChangedChars)
        notMatched = matchClubNames(notMatched, leagueClubs, ::containsFilteredWithChangedChars)
        allNotMatched.addAll(notMatched)
    }
    println("${allNotMatched.size} klubów niedopasowanych z rankingiem")
}

private fun matchClubNames(
    rankClubs: MutableList<Pair<String, HashMap<String, Double>>>,
    leagueClubs: MutableList<Club>,
    matchMethod: (name1: String, name2: String) -> Boolean
): MutableList<Pair<String, HashMap<String, Double>>> {
    val rankingClubNotMatched = mutableListOf<Pair<String, HashMap<String, Double>>>()
    rankClubs.forEach { rankingClubPair ->
        val matches = leagueClubs.filter { matchMethod(it.name, rankingClubPair.first) }
        if (matches.size != 1) {
            rankingClubNotMatched.add(rankingClubPair)
        } else {
            leagueClubs.remove(matches[0])
            rankingClubPair.second.forEach {
                matches[0].ranking.assignPointsForSeason(it.key, it.value)
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
        // tu się zaczynają konflikty po dodaniu drugich lig
        "Club Brugge KV" to "FC Brügge",
        "FK Budućnost (Podgorica)" to "Buducnost Podgorica",
        "Isłocz Minskij rajon" to "Isloch Minsk Region",
        "FC Salzburg" to "Red Bull Salzburg",
        "Maccabi Hajfa" to "Maccabi Haifa",
        "KR (Reykjavík)" to "KR Reykjavík",
        // tu się zaczynają konflikty z drużyną rezerw
        "AZ (Alkmaar)" to "AZ Alkmaar",
        "AFC Ajax" to "Ajax Amsterdam",
        "SL e Benfica" to "Benfika Lizbona",
        "Olympiakós SFP (Pireus)" to "Olympiakos Pireus",
        "PAOK" to "PAOK Saloniki",
        "Maccabi Petach Tikwa" to "Maccabi Petah Tikva",
        "Noa Erewan" to "FC Noah Erewan",
        "Ararat-Armenia Erewan" to "FC Ararat-Armenia",
        "HJK" to "HJK Helsinki",
        "Seinäjoen JK" to "SJK Seinäjoki",
        "Hegelmann Kowno" to "FC Hegelmann",
        "Transinvest Wilno" to "FK TransINVEST",
    )
    return map[name1] == name2 || map[name2] == name1
}
// Vaduz?

// Dnipro-1 Dniepropietrowsk    - klub przestał istnieć
// FC Sfîntul Gheorghe Suruceni - klub przestał istnieć
// FK Tuzla City (Simin Han)    - spadło - liga pominięta (Bośnia)
// Valmiera FC                  - spadło - liga pominięta (Łotwa)
// Tampere United               - spadło - liga pominięta (3 poziom, Finlandia)
// FC Honka                     - spadło - liga pominięta (4 poziom, Finlandia)
// Balzan FC                    - spadło - liga pominięta (Malta)
// JK Tallinna Kalev            - spadło - liga pominięta (Estonia)
// FK Iskra (Danilovgrad)       - spadło - liga pominięta (Czarnogóra)
// FK Podgorica                 - spadło - liga pominięta (Czarnogóra)
// CS Fola Esch                 - spadło - liga pominięta (Luxemburg)
// Newtown AFC                  - spadło - liga pominięta (Walia)
// UE Sant Julià                - klub przestał istnieć