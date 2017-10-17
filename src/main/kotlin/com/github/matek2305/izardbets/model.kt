package com.github.matek2305.izardbets

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "competitions")
data class Competition(
    @Id
    var id: String? = null,
    var name: String,
    var description: String? = null,
    var events: Array<Event> = emptyArray()
)

data class Event(
    val id: String = ObjectId.get().toString(),
    val homeTeamName: String,
    val awayTeamName: String,
    val date: LocalDateTime,
    var homeTeamScore: Int? = null,
    var awayTeamScore: Int? = null
)

@Document(collection = "event_bets")
data class EventBet(
    @Id
    var id: String? = null,
    val eventId: String,
    val who: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int
)
