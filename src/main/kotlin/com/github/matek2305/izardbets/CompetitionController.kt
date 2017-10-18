package com.github.matek2305.izardbets

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CompetitionController(private val competitionService: CompetitionService) {

    @GetMapping("/competitions")
    fun findAll() = competitionService.findAll()
}
