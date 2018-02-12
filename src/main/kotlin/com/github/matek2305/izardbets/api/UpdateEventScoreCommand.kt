package com.github.matek2305.izardbets.api

data class UpdateEventScoreCommand(
    val competitionSecret: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int
)
