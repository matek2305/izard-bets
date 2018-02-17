package com.github.matek2305.izardbets.events.api

data class UpdateEventScoreCommand(
    val secret: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int
)
