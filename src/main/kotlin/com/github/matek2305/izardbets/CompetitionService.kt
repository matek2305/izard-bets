package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.AddCompetitionCommand
import org.springframework.stereotype.Service

@Service
class CompetitionService(private val competitionRepository: CompetitionRepository) {

    fun findAll() = competitionRepository.findAll()

    fun findById(id: String) = competitionRepository.findById(id)

    fun create(command: AddCompetitionCommand) {
        println(command)
    }
}
