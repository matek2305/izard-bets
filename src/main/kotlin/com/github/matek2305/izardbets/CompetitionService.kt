package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.AddCompetitionCommand
import com.github.matek2305.izardbets.api.AddEventCommand
import com.github.matek2305.izardbets.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.domain.Competition
import com.github.matek2305.izardbets.factory.CompetitionFactory
import com.github.matek2305.izardbets.factory.EventFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CompetitionService(
    private val competitionRepository: CompetitionRepository,
    private val competitionFactory: CompetitionFactory,
    private val eventFactory: EventFactory
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
