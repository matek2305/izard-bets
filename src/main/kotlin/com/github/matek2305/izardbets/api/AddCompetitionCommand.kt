package com.github.matek2305.izardbets.api

import com.github.matek2305.izardbets.domain.Competition

data class AddCompetitionCommand(
    val name: String,
    val description: String? = null,
    val type: Competition.Type,
    val events: List<AddEventCommand> = emptyList()
)
