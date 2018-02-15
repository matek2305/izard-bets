package com.github.matek2305.izardbets.competitions.api

import com.github.matek2305.izardbets.competitions.domain.Competition

data class CreateCompetitionCommand(
    val name: String,
    val description: String? = null,
    val type: Competition.Type,
    val secret: String,
    val events: List<CreateEventCommand> = emptyList()
)
