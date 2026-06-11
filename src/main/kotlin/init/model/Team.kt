package org.example.init.model


class Team(val name: String, val link: String, val value: String) {

    override fun toString(): String {
        var line = this.name
        val defaultLength = 30
        for (i in 1..defaultLength - line.length) line = "$line "
        return "$line ${this.value}"
    }
}
