package com.github.matek2305.izardbets.events.api

import java.time.LocalDateTime

data class CreateEventCommand(
    val homeTeamName: String,
    val awayTeamName: String,
    val description: String? = null,
    val date: LocalDateTime,
    val secret: String
)
