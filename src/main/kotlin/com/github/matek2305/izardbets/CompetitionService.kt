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
import reactor.core.publisher.SynchronousSink

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

    fun updateEvent(competitionId: String, eventId: String, command: UpdateEventScoreCommand) {
        competitionRepository.findById(competitionId)
            .handle { competition, sink: SynchronousSink<Competition> ->

                if (!secretEncoder.check(command.competitionSecret, competition.secret)) {
                    sink.error(InvalidSecretException("Provided secret is not valid, update forbidden!"))
                    return@handle
                }

                sink.next(competition)
            }
            .subscribe({ println(it) }, { throw it })
    }
}
