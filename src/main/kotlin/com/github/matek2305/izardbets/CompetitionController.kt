package com.github.matek2305.izardbets

import com.github.matek2305.izardbets.api.AddCompetitionCommand
import com.github.matek2305.izardbets.api.AddEventCommand
import com.github.matek2305.izardbets.api.UpdateEventScoreCommand
import com.github.matek2305.izardbets.domain.Competition
import com.github.matek2305.izardbets.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class CompetitionController(private val competitionService: CompetitionService) {

    @GetMapping("/competitions")
    fun findAll(): Flux<Competition> = competitionService.findAll()

    @GetMapping("/competitions/{id}")
    fun findById(@PathVariable id: String): Mono<Competition> = competitionService
        .findById(id)
        .doOnSuccess { it ?: throw ResourceNotFoundException("Competition(id='$id') not found!") }

    @PostMapping("/competitions")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody body: AddCompetitionCommand) = competitionService.create(body)

    @PostMapping("/competitions/{id}/events")
    @ResponseStatus(HttpStatus.CREATED)
    fun addEvent(@PathVariable id: String, @RequestBody body: AddEventCommand) = competitionService.addEvent(id, body)

    @PatchMapping("/competitions/{competitionId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateEvent(
        @PathVariable competitionId: String,
        @PathVariable eventId: String,
        @RequestBody command: UpdateEventScoreCommand) {
        competitionService.updateEvent(competitionId, eventId, command)
    }
}
