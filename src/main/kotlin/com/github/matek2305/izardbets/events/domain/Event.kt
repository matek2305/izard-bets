package com.github.matek2305.izardbets.events.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "events")
data class Event(
    @Id val id: String? = null,
    val homeTeamName: String,
    val awayTeamName: String,
    val description: String? = null,
    val date: LocalDateTime,
    val homeTeamScore: Int? = null,
    val awayTeamScore: Int? = null,
    @JsonIgnore val secret: String,
    val invitationCode: String,
    val status: Status = Status.CREATED,
    @CreatedDate val createdAt: LocalDateTime? = null,
    @LastModifiedDate val lastUpdateAt: LocalDateTime? = null,
    @Version val version: String? = null
) {
    fun updateScore(homeTeamScore: Int, awayTeamScore: Int): Event {
        return copy(homeTeamScore = homeTeamScore, awayTeamScore = awayTeamScore)
    }

    enum class Status {
        CREATED,
        BETTING,
        LOCKED,
        FINISHED
    }
}
