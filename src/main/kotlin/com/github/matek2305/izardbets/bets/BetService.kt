package com.github.matek2305.izardbets.bets

import com.github.matek2305.izardbets.bets.api.CreateBetCommand
import com.github.matek2305.izardbets.bets.domain.Bet
import com.github.matek2305.izardbets.bets.domain.BetRepository
import com.github.matek2305.izardbets.common.SecretEncoder
import com.github.matek2305.izardbets.competitions.domain.CompetitionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BetService(
    private val competitionRepository: CompetitionRepository,
    private val betRepository: BetRepository,
    private val secretEncoder: SecretEncoder
) {

    fun create(command: CreateBetCommand): Mono<Bet> {
        return competitionRepository
            .findByIdAndEventId(command.competitionId, command.eventId)
            .filter { it.invitationCode == command.competitionInvCode }
            .map { createBet(command) }
            .flatMap { betRepository.save(it) }
    }

    private fun createBet(command: CreateBetCommand) = Bet(
        competitionId = command.competitionId,
        eventId = command.eventId,
        who = command.who,
        homeTeamScore = command.homeTeamScore,
        awayTeamScore = command.awayTeamScore,
        secret = secretEncoder.encode(command.secret))
}
