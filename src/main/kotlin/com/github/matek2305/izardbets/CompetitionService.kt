package com.github.matek2305.izardbets

import org.springframework.stereotype.Service

@Service
class CompetitionService(private val competitionRepository: CompetitionRepository) {

    fun findAll() = competitionRepository.findAll()
}
