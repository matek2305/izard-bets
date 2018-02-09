package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.AddCompetitionCommand
import com.github.matek2305.izardbets.api.AddEventCommand
import com.github.matek2305.izardbets.domain.Competition
import com.github.matek2305.izardbets.domain.Event
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CompetitionService(private val competitionRepository: CompetitionRepository) {

    fun findAll(): Flux<Competition> = competitionRepository.findAll()

    fun findById(id: String): Mono<Competition> = competitionRepository.findById(id)

    fun create(command: AddCompetitionCommand): Mono<Competition> = competitionRepository.save(buildCompetition(command))

    fun addEvent(id: String, command: AddEventCommand): Mono<Competition> {
        return competitionRepository.findById(id)
            .map { it.addEvent(buildEvent(command)) }
            .flatMap { competitionRepository.save(it) }
    }

    private fun buildCompetition(command: AddCompetitionCommand): Competition {
        return Competition(
            name = command.name,
            description = command.description,
            type = command.type,
            events = command.events.map { buildEvent(it) })
    }

    private fun buildEvent(command: AddEventCommand): Event {
        return Event(
            id = ObjectId().toString(),
            homeTeamName = command.homeTeamName,
            awayTeamName = command.awayTeamName,
            date = command.date)
    }
}
