package com.github.matek2305.izardbets.competitions.domain

import java.time.LocalDateTime

data class Event(
    val id: String,
    val homeTeamName: String,
    val awayTeamName: String,
    val date: LocalDateTime,
    val homeTeamScore: Int? = null,
    val awayTeamScore: Int? = null
) {
    fun updateScore(id: String, homeTeamScore: Int, awayTeamScore: Int): Event {
        return if (this.id == id) copy(homeTeamScore = homeTeamScore, awayTeamScore = awayTeamScore) else this
    }
}
