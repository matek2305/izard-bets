package com.github.matek2305.izardbets.domain

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class Event(
    @Id val id: String,
    val homeTeamName: String,
    val awayTeamName: String,
    val date: LocalDateTime,
    val homeTeamScore: Int? = null,
    val awayTeamScore: Int? = null
)
