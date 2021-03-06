package com.github.matek2305.izardbets.bets.api

data class CreateBetCommand(
    val eventId: String,
    val competitionInvCode: String,
    val who: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val secret: String)
