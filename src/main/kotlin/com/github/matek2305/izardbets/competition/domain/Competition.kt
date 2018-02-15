package com.github.matek2305.izardbets.competition.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "competitions")
data class Competition(
    @Id val id: String? = null,
    val name: String,
    val description: String? = null,
    val type: Type,
    val events: List<Event> = emptyList(),
    @JsonIgnore val secret: String,
    val invitationCode: String,
    @CreatedDate val createdAt: LocalDateTime? = null,
    @LastModifiedDate val lastUpdateAt: LocalDateTime? = null
) {
    fun addEvent(event: Event): Competition = copy(events = this.events + event)

    fun updateEventScore(id: String, homeTeamScore: Int, awayTeamScore: Int): Competition {
        return copy(events = events.map { it.updateScore(id, homeTeamScore, awayTeamScore) })
    }

    enum class Type {
        SINGLE_EVENT,
        LEAGUE,
        TOURNAMENT
    }
}
