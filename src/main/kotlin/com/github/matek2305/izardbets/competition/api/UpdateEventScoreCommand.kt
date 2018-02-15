package com.github.matek2305.izardbets.competition.api

data class UpdateEventScoreCommand(
    val competitionSecret: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int
)
