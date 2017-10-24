package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.AddCompetitionCommand
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CompetitionService(private val competitionRepository: CompetitionRepository) {

    fun findAll() = competitionRepository.findAll()

    fun findById(id: String) = competitionRepository.findById(id)

    fun create(command: AddCompetitionCommand) {
        competitionRepository.insert(buildCompetition(command)).subscribe()
    }

    private fun buildCompetition(command: AddCompetitionCommand): Competition {
        return Competition(
            name = command.name,
            description = command.description,
            events = command.events.map {
                Event(
                    id = ObjectId().toString(),
                    homeTeamName = it.homeTeamName,
                    awayTeamName = it.awayTeamName,
                    date = it.date)
            })
    }
}
