package com.github.matek2305.izardbets.api

import java.time.LocalDateTime

data class AddEventCommand(
    val homeTeamName: String,
    val awayTeamName: String,
    val date: LocalDateTime
)
