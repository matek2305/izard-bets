package com.github.matek2305.izardbets.bets

data class CreateBetCommand(
    val competitionId: String,
    val eventId: String,
    val competitionInvCode: String,
    val who: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val secret: String)
