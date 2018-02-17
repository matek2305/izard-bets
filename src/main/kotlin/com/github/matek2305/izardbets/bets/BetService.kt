package com.github.matek2305.izardbets.bets

import com.github.matek2305.izardbets.bets.api.CreateBetCommand
import com.github.matek2305.izardbets.bets.domain.Bet
import com.github.matek2305.izardbets.bets.domain.BetRepository
import com.github.matek2305.izardbets.common.SecretEncoder
import com.github.matek2305.izardbets.events.domain.EventRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BetService(
    private val eventRepository: EventRepository,
    private val betRepository: BetRepository,
    private val secretEncoder: SecretEncoder
) {
    fun create(command: CreateBetCommand): Mono<Bet> {
        return eventRepository
            .findById(command.eventId)
            .filter { it.invitationCode == command.competitionInvCode }
            .map { createBet(command) }
            .flatMap { betRepository.save(it) }
    }

    private fun createBet(command: CreateBetCommand) = Bet(
        eventId = command.eventId,
        who = command.who,
        homeTeamScore = command.homeTeamScore,
        awayTeamScore = command.awayTeamScore,
        secret = secretEncoder.encode(command.secret)
    )
}
