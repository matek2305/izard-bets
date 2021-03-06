package com.github.matek2305.izardbets.bets.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "bets")
data class Bet(
    @Id val id: String? = null,
    val eventId: String,
    val who: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    @JsonIgnore val secret: String,
    @CreatedDate val createdAt: LocalDateTime? = null,
    @LastModifiedDate val lastUpdateAt: LocalDateTime? = null
)
