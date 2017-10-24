package com.github.matek2305.izardbets.api

data class AddCompetitionCommand(
    val name: String,
    val description: String?,
    val events: List<NewEventData>
)
