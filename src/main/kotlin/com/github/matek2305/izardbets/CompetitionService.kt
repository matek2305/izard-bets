package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.AddCompetitionCommand
import com.github.matek2305.izardbets.api.AddEventCommand
import com.github.matek2305.izardbets.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.domain.Competition
import com.github.matek2305.izardbets.exception.InvalidSecretException
import com.github.matek2305.izardbets.factory.CompetitionFactory
import com.github.matek2305.izardbets.factory.EventFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Function

@Service
class CompetitionService(
    private val competitionRepository: CompetitionRepository,
    private val competitionFactory: CompetitionFactory,
    private val eventFactory: EventFactory,
    private val secretEncoder: SecretEncoder
) {
    fun findAll(): Flux<Competition> = competitionRepository.findAll()

    fun findById(id: String): Mono<Competition> = competitionRepository.findById(id)

    fun create(command: AddCompetitionCommand): Mono<Competition> =
        competitionRepository.save(competitionFactory.build(command))

    fun addEvent(id: String, command: AddEventCommand): Mono<Competition> {
        return competitionRepository.findById(id)
            .map { it.addEvent(eventFactory.build(command)) }
            .flatMap { competitionRepository.save(it) }
    }

    fun updateEvent(competitionId: String, eventId: String, command: UpdateEventScoreCommand): Mono<Competition> {
        return competitionRepository.findById(competitionId)
            .map(validateSecret(command.competitionSecret))
            .map { it.updateEventScore(eventId, command.homeTeamScore, command.awayTeamScore) }
            .flatMap { competitionRepository.save(it) }
    }

    private fun validateSecret(secret: String) = Function { competition: Competition ->
        if (!secretEncoder.check(secret, competition.secret)) {
            throw InvalidSecretException("Provided secret is not valid, update forbidden!")
        }

        return@Function competition
    }
}
