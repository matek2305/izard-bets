package com.github.matek2305.izardbets.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "event_bets")
data class EventBet(
    @Id val id: String? = null,
    val competitionId: String,
    val eventId: String,
    val who: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val lockCode: String,
    @CreatedDate val createdAt: LocalDateTime? = null,
    @LastModifiedDate val lastUpdateAt: LocalDateTime? = null
)
