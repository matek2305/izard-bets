package com.github.matek2305.izardbets

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
    val events: List<Event> = emptyList(),
    @CreatedDate val createdAt: LocalDateTime? = null,
    @LastModifiedDate val lastUpdateAt: LocalDateTime? = null
) {
    fun addEvent(event: Event): Competition = copy(events = this.events + event)
}

data class Event(
    @Id val id: String,
    val homeTeamName: String,
    val awayTeamName: String,
    val date: LocalDateTime,
    val homeTeamScore: Int? = null,
    val awayTeamScore: Int? = null
)

@Document(collection = "event_bets")
data class EventBet(
    @Id val id: String,
    val eventId: String,
    val who: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    @CreatedDate val createdAt: LocalDateTime? = null,
    @LastModifiedDate val lastUpdateAt: LocalDateTime? = null
)
