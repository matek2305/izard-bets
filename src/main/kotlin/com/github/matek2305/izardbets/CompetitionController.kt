package com.github.matek2305.izardbets

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CompetitionController(private val competitionService: CompetitionService) {

    @GetMapping("/competitions")
    fun findAll() = competitionService.findAll()

    @GetMapping("/competitions/{id}")
    fun findById(@PathVariable id: String) = competitionService.findById(id)
}
