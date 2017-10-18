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
    var id: String = ObjectId.get().toString(),
    var homeTeamName: String,
    var awayTeamName: String,
    var date: LocalDateTime,
    var homeTeamScore: Int? = null,
    var awayTeamScore: Int? = null
)

@Document(collection = "event_bets")
data class EventBet(
    @Id
    var id: String? = null,
    var eventId: String,
    var who: String,
    var homeTeamScore: Int,
    var awayTeamScore: Int
)
