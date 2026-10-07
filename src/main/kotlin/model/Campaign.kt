package org.example.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany


@Entity
class Campaign(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,
    @ManyToOne
    var federation: Federation,
    var season: String,
    @OneToMany(mappedBy = "campaign")
    var leagues: MutableSet<League> = mutableSetOf(),
    @ManyToOne
    var cupWinner: Club? = null
) {

}
