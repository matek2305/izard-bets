package com.github.matek2305.izardbets.events.api

import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class CreateEventCommand(
    @field:NotBlank(message = "home team name is required") val homeTeamName: String,
    @field:NotBlank(message = "away team name is required") val awayTeamName: String,
    val description: String? = null,
    val date: LocalDateTime,
    @field:NotBlank(message = "secret is required") val secret: String
)
