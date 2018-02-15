package com.github.matek2305.izardbets.competitions.api

import java.time.LocalDateTime

data class CreateEventCommand(
    val homeTeamName: String,
    val awayTeamName: String,
    val date: LocalDateTime
)
