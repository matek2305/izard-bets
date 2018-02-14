package com.github.matek2305.izardbets.api

import com.github.matek2305.izardbets.domain.Competition

data class CreateCompetitionCommand(
    val name: String,
    val description: String? = null,
    val type: Competition.Type,
    val secret: String,
    val events: List<CreateEventCommand> = emptyList()
)
